import java.util.Arrays;
import java.util.Date;
import java.security.*;
import java.sql.*;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
	private Socket socket;
	private DataInputStream incoming;
	private DataOutputStream outgoing;
	
	private String dburl = "jdbc:mysql://dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com/userAccounts";
	private String dbUsername = "dungeoncoder";
	private String dbPassword = "DungeonCoder23";
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	private boolean run = true;

	private String connectedUser = "";

	public ServerThread(Socket socket) {
		this.socket = socket;		
		try {
			incoming = new DataInputStream(socket.getInputStream());
			outgoing = new DataOutputStream(socket.getOutputStream());	
		}
		catch(IOException e) {
		}
	}

	// Send an integer value to the client.
	private void sendInt(int data) {
		try {
			outgoing.writeInt(data);
		}
		catch (SocketTimeoutException e) {
			shutdown(true);
		}
		catch (IOException e) {
			// IO failure shut down
			shutdown(false);
		}
	}

	// Recieve and integer value from the client.
	private int recieveInt() {
		int data = -1;
		try {
			data = incoming.readInt();
		}
		catch (SocketTimeoutException e) {
			shutdown(true);
		}
		catch (IOException e) {
			// IO failure. Shut down
			shutdown(false);
		}
		return data;
	}

	// Send a String to the client.
	private void sendString(String data) {
		try {
			outgoing.writeUTF(data);	
		}
		catch (SocketTimeoutException e) {
			shutdown(true);
		}
		catch (IOException e) {
			// IO failure. Shut down
			shutdown(false);
		}
	}

	// Recieve a String from the client.
	private String recieveString() {
		String data = "";
		try {
			data = incoming.readUTF();
		}
		catch (SocketTimeoutException e) {
			shutdown(true);
		}
		catch (IOException e) {
			// IO failure. Shut down
			shutdown(false);
		}
		return data;
	}

	// Send an OPCODE to the client
	private void sendCode(byte code) {
		try {
			outgoing.write(code);
		}
		catch (SocketTimeoutException e) {
			shutdown(true);
		}
		catch (IOException e) {
			// IO failure. Shut down
			shutdown(false);
		}
	}	

	// Recieve an OPCODE from the client
	private byte recieveCode() {
		byte code = (byte)(0xEE);
		try {
			code = (byte)(incoming.read());
		}
		catch (SocketTimeoutException e) {
			shutdown(true);
		}
		catch (IOException e) {
			// IO failure. Shut down
			shutdown(false);
		}
		return code;
	}

	// Connect this thread to the database.
	private void connectDB() {
		try {
			conn = DriverManager.getConnection(dburl, dbUsername, dbPassword);
			stmt = conn.createStatement();
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());	
			shutdown(false);
		}
	}

	// Disconnect this thread from the database.
	private void disconnectDB() {
		if (rs != null) {
			try {
				rs.close();
			}
			catch(SQLException e) {
				// Do Nothing
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch(SQLException e) {
				// Do Nothing
			}
		}
		if (conn != null) {
			try {
				conn.close();
			}
			catch(SQLException e) {
				// Do Nothing
			}
		}
	}

	private void getMostRecentCodeFile() {
		System.out.println("getting most recent code file");
		String levelName = recieveString();
		String fileName;
		try {
			if (conn == null) {
				connectDB();
			}
			rs = stmt.executeQuery("SELECT Code FROM " + levelName + " WHERE Student='" + connectedUser + "'");
			rs.next();
			fileName = rs.getString("Code");	
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("database failure");
			sendCode((byte)(0x60));
			return;
		}
		System.out.println("most recent code file is " + fileName);
		File file = new File("Files/" + fileName);
		sendFile(file, fileName);
		//sendCode((byte)(0x10));
	}

	// Get the user's code for a specific level
	private void getUserLevelCode() {
		String fileName = recieveString();
		File file = new File("Files/" + fileName);
		sendFile(file, fileName);
		//sendCode((byte)(0x10));
	}

	private void getLevelFile() {
		String leveName = recieveString();
	}

	// Check if the user exists in the database.
	private boolean userExists(String username) {
		if (conn == null) {
			connectDB();
		}
		try {
			rs = stmt.executeQuery("SELECT USERNAME FROM Users");
			while(rs.next()) {
				if (username.equals(rs.getString("USERNAME"))) {
					return true;
				}
			}
		}
		catch(SQLException e) {
			System.out.println("Something went wrong while fetching users");
			e.printStackTrace();
			//sendCode((byte)(0x60));
		}
		return false;
	}
	
	// Check the database if account exists and the password in correct.
	private boolean checkAccount(String username, String password) {
		boolean goodCheck = false;
		if (conn == null) {
			connectDB();
		}
		try {
			rs = stmt.executeQuery("SELECT USERNAME, hash, salt From Users");
			while (rs.next()) {
				if (username.equals(rs.getString("username"))) {
					String knownHash = rs.getString("hash");
					String hexSalt = rs.getString("salt");
					byte[] salt = hexToSalt(hexSalt);
					String checkHash = hash(password, salt);
					if (knownHash.equals(checkHash)) {
						goodCheck = true;
						System.out.println("password is good");
						break;
					}
				}
			}
		}
		catch (SQLException e) {
			//sendCode((byte)(0x60));
			shutdown(false);
		}
		catch (Exception e) {
			//sendCode((byte)(0x60));
			shutdown(false);
		}
		return goodCheck;
	}

	// Attempt to log the client in with the given username and password.
	private void login() {
		
		String username = "";
		String password = "";
	
		username = recieveString();
		password = recieveString();
		System.out.println("attempted login with username: " + username + " | password : " + password);

		if (conn == null) {
			connectDB();
		}

		if (checkAccount(username, password)) {
			sendCode((byte)(0x10));
			System.out.println("Attempted login successful.");
			connectedUser = username;
			return;
		}
		sendCode((byte)(0x40));
	}

	// Attempt to create a new user with given username and password.
	private void createAccount() {
		String username = "";
		String password = "";
		
		username = recieveString();
		password = recieveString();
		System.out.println("Attempting to create account with " + username + " and " + password);	
		if (userExists(username)) {
			System.out.println("User exists");
			sendCode((byte)(0x40));
			return;
		}
	
		byte[] salt = salt();
		String hash = hash(password, salt);
		String hexSalt = saltyString(salt);

		if (conn == null) {
			connectDB();
		}
		try {
			stmt.executeUpdate("INSERT INTO Users (Username, Hash, Salt)" + "VALUES ('" + username + "','" + hash + "','" + hexSalt + "')");
			stmt.executeUpdate("INSERT INTO Task1 (Student)" + "VALUES ('" + username + "')");
			stmt.executeUpdate("INSERT INTO Task2 (Student)" + "VALUES ('" + username + "')");
			stmt.executeUpdate("INSERT INTO Task3 (Student)" + "VALUES ('" + username + "')");
		}
		catch (SQLException e) {
			e.printStackTrace();
			sendCode((byte)(0x60));
			System.out.println("DB error");
			return;
		}
		sendCode((byte)(0x10));
		connectedUser = username;
	}
	
	// Convert the byte[] salt to a hex string for storage.
	private String saltyString(byte[] salt) {
		char[] hex = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[salt.length * 2];
		for (int i = 0; i < salt.length; i++) {
			int j = salt[i] & 0xFF;
			hexChars[i * 2] = hex[j >>> 4];
			hexChars[i * 2 + 1] = hex[j & 0x0f];
		}
		return new String(hexChars);
	}

	// Convert the hex string to byte[] for hashing.
	private byte[] hexToSalt(String hex) {
		int length = hex.length();
		byte[] salt = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			salt[i / 2] = (byte)((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return salt;
	}

	// Allow users to update their account information.
	private void updateAccount() {
		System.out.println("User requested account update");
		boolean changeUsername = false;
		boolean changePassword = false;
		boolean changeEmail = false;
		
		String list = recieveString();
		System.out.println("recieved list: " + list);
		String token = recieveString();
		String[] updateList = parseList(list, token);
		String newUsername = "";
		String newEmail = "";
		String newPass = "";

      		if (Arrays.asList(updateList).contains("username")) {
			changeUsername = true;
			newUsername = recieveString();
			System.out.println("new username: " + newUsername);
		}
		if (Arrays.asList(updateList).contains("email")) {
			changeEmail = true;
			newEmail = recieveString();
			System.out.println("new email: " + newEmail);
		}
		if (Arrays.asList(updateList).contains("password")) {
			changePassword = true;
			newPass = recieveString();
			System.out.println("new password: " + newPass);
		}
		String username = recieveString();
		String password = recieveString();
		System.out.println("User is attempting to log in with username: " + username + " and password: " + password);
		boolean update = checkAccount(username, password);
		System.out.println("account verified: " + update);
		if (update == false) {
			sendCode((byte)(0x40));
			return;
		}
		else {
			sendCode((byte)(0x10));
			if (conn == null) {
				connectDB();		
			}
			try {
				if (changeEmail) {
					System.out.println("updating email to " + newEmail);
					stmt.executeUpdate("UPDATE Users SET email='" + newEmail + "' WHERE Username='" + username + "'");
				}
				if (changePassword) {
					System.out.println("updating password to " + newPass);
					byte[] salt = salt();
					String hash = hash(newPass, salt);
					String hexSalt = saltyString(salt);
					stmt.executeUpdate("UPDATE Users SET hash='" + hash + "' WHERE Username='" + username + "'");
					stmt.executeUpdate("UPDATE Users SET salt='" + hexSalt + "' WHERE Username='" + username + "'");
				}
				if (changeUsername) {
					System.out.println("updating username to " + newUsername);
					stmt.executeUpdate("UPDATE Users SET Username='" + newUsername + "' WHERE Username='" + username + "'");
					stmt.executeUpdate("UPDATE Task1 SET Student='" + newUsername + "' WHERE Student='" + username + "'");
					stmt.executeUpdate("UPDATE Task2 SET Student='" + newUsername + "' WHERE Student='" + username + "'");
					stmt.executeUpdate("UPDATE Task3 SET Student='" + newUsername + "' WHERE Student='" + username + "'");
					
					rs = stmt.executeQuery("SELECT Attempts FROM Task1 WHERE Student='" + newUsername + "'");
					rs.next();
					int attempts1 = rs.getInt("Attempts");
					for (int i = 0; i < (attempts1 - 1); i++) {
						File file = new File("Files/" + username + "Task1|" + Integer.toString(i));
						File newFile = new File("Files/" + newUsername + "Task1|" + Integer.toString(i));
						file.renameTo(newFile);
					}

					rs = stmt.executeQuery("SELECT Attempts FROM Task2 WHERE Student='" + newUsername + "'");
					rs.next();
					int attempts2 = rs.getInt("Attempts");
					for (int i = 0; i < (attempts2 - 1); i++) {
						File file = new File("Files/" + username + "Task2|" + Integer.toString(i));
						File newFile = new File("Files/" + newUsername + "Task2|" + Integer.toString(i));
						file.renameTo(newFile);
					}

					rs = stmt.executeQuery("SELECT Attempts FROM Task3 WHERE Student='" + newUsername + "'");
					rs.next();
					int attempts3 = rs.getInt("Attempts");
					for (int i = 0; i < (attempts3 - 1); i++) {
						File file = new File("Files/" + username + "Task3|" + Integer.toString(i));
						File newFile = new File("Files/" + newUsername + "Task3|" + Integer.toString(i));
						file.renameTo(newFile);
					}
					
					connectedUser = newUsername;
				}
				//sendCode((byte)(0x10));
			}
			catch (SQLException e) {
				e.printStackTrace();
				//sendCode((byte)(0x60));
			}
			catch (Exception e) {
				e.printStackTrace();
				//sendCode((byte)(0x60));
			}
		}
	}

	private void sendUserEmailAddress() {
		try {
			System.out.println("getting email for " + connectedUser);
			rs = stmt.executeQuery("SELECT Email From Users WHERE Username='" + connectedUser + "'");
			rs.next();
			String emailAddress = rs.getString("Email");
			System.out.println("Sending email address: " + emailAddress);
			sendString(emailAddress);
		}
		catch (SQLException e) {
			// Do nothing
		}
	}

	// Update the user's progress for a task.
	private void updateProgress() {
		String task = recieveString();
		int percentage = recieveInt();
		if (conn == null) {
			connectDB();
		}
		try {
			System.out.println("updating user progress");
			stmt.executeUpdate("UPDATE " + task + " SET Completion='" + percentage + "' WHERE Student='" + connectedUser + "'");
			sendCode(updateUserCode(task));
			return;
		}
		catch (SQLException e) {
			e.printStackTrace();
			//sendCode((byte)(0x60));
		}
		catch (Exception e) {
			e.printStackTrace();
			//sendCode((byte)(0x60));
		}
	}

	// Update the user's saved code and attempts.
	private byte updateUserCode(String task) {
		try {
			if (conn == null) {
				connectDB();
			}
			rs = stmt.executeQuery("SELECT Attempts FROM " + task + " WHERE Student='" + connectedUser + "'");
			rs.next();
			int currentAttempts = rs.getInt("Attempts");
			
			String attempts = Integer.toString(currentAttempts);
			String fileName = connectedUser + task + "|" + attempts;
			recieveFile(fileName, false);

			currentAttempts++;
			stmt.executeUpdate("UPDATE " + task + " SET Attempts='" + currentAttempts + "' WHERE Student='" + connectedUser + "'");
			stmt.executeUpdate("UPDATE " + task + " Set Code='" + fileName + "' WHERE Student='" + connectedUser + "'");
			return((byte)(0x10));
		}
		catch(SQLException e) {
			e.printStackTrace();
			return((byte)(0x60));
		}
	}

	// Get the relative info for the task of the current Student.
	private void getTaskInfo() {
		try {
			String task = recieveString();
			String information = recieveString();
			if (conn == null) {
				connectDB();
			}
			rs = stmt.executeQuery("SELECT " + information + " FROM " + task + " WHERE Student='" + connectedUser + "'");
			rs.next();
			if (information.equals("Attempts") || information.equals("PointValue")) {
				sendCode((byte)(0x10));
				sendString(Integer.toString(rs.getInt(information)));
				return;
			}
			else {
				sendCode((byte)(0x10));
				sendString(rs.getString(information));
				return;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			//sendCode((byte)(0x60));
		}
	}

	// Split the given string with the given token.
	private String[] parseList(String list, String token) {
		String[] listItems = list.split(token);
		return listItems;
	}

	// To Be Implemented
	private void sendFile(File file, String fileName) {
		System.out.println("sending file " + fileName);
		try {
			System.out.println("sending file name");
			sendString(fileName);
			System.out.println("sending file length");
			long length = file.length();
			outgoing.writeLong(length);
			byte[] buffer = new byte[16 * 1024];
			System.out.println("sending file contents");
			FileInputStream in = new FileInputStream(file);
	
			int count;
			while ((count = in.read(buffer)) > 0) {
				outgoing.write(buffer, 0, count);
			}
			in.close();
			//sendCode((byte)(0x10));
		}
		catch (IOException e) {
			e.printStackTrace();
			//sendCode((byte)(0x40));
		}
	}

	// Recieve data for a file from the client and write it.
	private void recieveFile(String fileName, boolean level) {
		System.out.println("recieving file");
		try {
			String givenFileName = incoming.readUTF();
			if (fileName.equals("")) {
				fileName = givenFileName;
			}
			File f = null;
			if (!level) {
				System.out.println("Saving to Files/" + fileName);
				f = new File("Files/" + fileName);
			}
			else {
				System.out.println("Saving to Files/Levels/" + fileName);
				f = new File("Files/Levels/" + fileName);
			}
			f.createNewFile();

			OutputStream out = new FileOutputStream(f);

			long fileSize = incoming.readLong();
			byte[] buffer = new byte[16 * 1024];
			int count;
			
			while (fileSize > 0 && (count = incoming.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
				out.write(buffer, 0, count);
				fileSize -= count;
			}
			out.close();
			sendCode((byte)(0x10));
		}
		catch(IOException e) {
			e.printStackTrace();
			//sendCode((byte)(0x40));
			shutdown(false);
		}
	}

	// Generate a new salt value.
	private byte[] salt() {
		byte[] salt = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}

	// Hash a password with a given salt.
	private String hash(String password, byte[] salt) {
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(password.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			hash = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	// Check if the given date is past the deadline for the given task.
	private void DeadlinePassed(String task, Date date) {
		Date dueDate = getDeadline(task);
		sendInt(compareDates(date, dueDate));
	}

	// Query the database for the deadline of the given task.
	private Date getDeadline(String task) {
		if (conn == null) {
			connectDB();
		}
		Date deadline = null;
		try {
			rs = stmt.executeQuery("SELECT Deadline FROM " + task + " WHERE Student = '" + connectedUser + "'");
			rs.next();
			deadline = rs.getDate("Deadline");
		}
		catch (SQLException e) {
		}
		return deadline;
	}

	private void levelUpload() {
		String name = recieveString();
		recieveFile(name, true);
		if (!levelExists(name)) {
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Files/Levels/LevelList.txt", true)));
				out.print(name + "\n");
				out.close();
			}
			catch (IOException e) {
				// Shouldn't happen
			}
		}
	}

	private boolean levelExists(String levelName) {
		try {
			File levels = new File("Files/Levels/LevelList.txt");
			BufferedReader br = new BufferedReader(new FileReader(levels));
			String level;
			while((level = br.readLine()) != null) {
				if (level.equals(levelName)) {
					return true;
				}
			}
		}
		catch(IOException e) {
			// Shouldn't happen
		}
		return false;
	}

	private void sendCustomLevel() {
		String levelName = recieveString();
		File level = new File("Files/Levels/" + levelName);
		if (!level.exists()) {
			// Level does not exist
			sendCode((byte)(0x60));
			return;
		}
		System.out.println("Requested level " + levelName);
		sendFile(level, levelName);
	}

	private void sendLevelList() {
		try {
			File levels = new File("Files/Levels/LevelList.txt");
			BufferedReader br = new BufferedReader(new FileReader(levels));
			String level;
			while((level = br.readLine()) != null) {
				sendString(level);
			}
			sendString("\r\n");
		}
		catch (IOException e) {
			// Shouldn't happen
		}
	}

	// Check to see if date1 is before, after, or on date2.
	private int compareDates(Date date1, Date date2) {
		if (date1.before(date2)) {
			return -1;
		}
		return 1;
	}

	// Shutdown the connection.
	private void shutdown(boolean timedout) {
		try {
			if (timedout) {
				outgoing.write((byte)(0x11));
				System.out.println("timed out");
			}
			socket.close();
			disconnectDB();
		}
		catch(IOException e) {
		}
		System.out.println("There was an error. Terminating connection.");
		run = false;
	}

	private void pong() {
		try {
			if (conn == null) {
				connectDB();
			}
			rs = stmt.executeQuery("SELECT Announcement FROM Announcements");
			while(rs.next()) {
				sendString(rs.getString("Announcement"));
			}			
		}
		catch(SQLException e) {
		}

		sendCode((byte)(0xFF));
	}

	public void run() {
		// Set socket to timeout after a second if no login/account creation request	
		// Wait a max of 5 seconds for login or account creation instruction
		try {
			socket.setSoTimeout(5000);
		}
		catch (IOException e) {
			// Shouldn't happen. Do Nothing
		}
		// Read the code sent by the client
		
		byte code = recieveCode();
		// Recieved LOGIN code
		if (code == 0x01) {
			System.out.println("requested login");	
			sendCode((byte)(0x10));
			login();
		}
		// Recieved CREATEACCOUNT code
		else if (code == 0x02 ) {
			System.out.println("requested account creation");
			sendCode((byte)(0x10));
			createAccount();
		}
		// Recieved other or corrupted code
		else {
			shutdown(false);
		}

		// Stop thread if no code is recieved in the given time frame

		try {
			// Set timeout time to 30 seconds
			socket.setSoTimeout(30000);
		}
		catch (IOException e) {
			// Shouldn't happen. Do Nothing
		}
		
		while(run) {
			byte request = recieveCode();
			
			switch (request) {
				// UPDATEACCOUNT
				case 0x03 :
					sendCode((byte)(0x10));
					updateAccount();
					break;
				
				// SENDUSERAMILADDRESS
				case 0x13 :
					sendCode((byte)(0x10));
					sendUserEmailAddress();
					break;

				// UPDATEMODULECOMPLETION
				case 0x04 :
					sendCode((byte)(0x10));
					updateProgress();	
					break;
				
				// FETCHCODEFILE
				case 0x08 :
					sendCode((byte)(0x10));
					getUserLevelCode();
					break;
				
				// FETCHMOSTRECENTATTENPT
				case 0x18 :
					sendCode((byte)(0x10));
					getMostRecentCodeFile();
					break;
				
				// FETCHLEVELFILE
				case 0x09 :
					sendCode((byte)(0x10));
					getLevelFile();
					break;
				
				// FETCHTASKINFO
				case 0x0A :
					sendCode((byte)(0x10));
					getTaskInfo();
					break;
				
				// UPLOADLEVEL
				case 0x0B :
					sendCode((byte)(0x10));
					levelUpload();
					break;
				
				// FETCHCUSTOMLEVELLIST
				case 0x0C :
					sendCode((byte)(0x10));
					sendLevelList();
					break;
				
				// FETCHCUSTOMLEVEL
				case 0x0D :
					sendCode((byte)(0x10));
					sendCustomLevel();
					break;
				
				// PING
				case (byte)(0xFF) :
					sendCode((byte)(0x10));
					break;
				
				// INVALIDREQUEST
				default:
					sendCode((byte)(0xF0));
				break;
			}
		}
	}
}
