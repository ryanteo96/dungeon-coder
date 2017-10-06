import java.util.Arrays;
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
			e.printStackTrace();
		}
	}

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

	// Send data in the form af a String to the client.
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

	private void getUserLevelCode() {
		String levelName = recieveString();
		String fileName = connectedUser + levelName;
		File file = new File(fileName);
		sendFile(file, fileName);
		sendCode((byte)(0x10));
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
			sendCode((byte)(0x60));
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
					//System.out.println("known hash is " + knownHash);
					byte[] salt = hexToSalt(hexSalt);
					String checkHash = hash(password, salt);
					//System.out.println("check hash is " + checkHash);
					if (knownHash.equals(checkHash)) {
						goodCheck = true;
						System.out.println("password is good");
						break;
					}
				}
			}
		}
		catch (SQLException e) {
			sendCode((byte)(0x60));
			shutdown(false);
		}
		catch (Exception e) {
			sendCode((byte)(0x60));
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
		boolean changeUsername = false;
		boolean changePassword = false;
		boolean changeEmail = false;
		
		String list = recieveString();
		String token = recieveString();
		String[] updateList = parseList(list, token);
		
		String newUsername = "";
		String newEmail = "";
		String newPass = "";

      		if (Arrays.asList(updateList).contains("username")) {
			changeUsername = true;
			newUsername = recieveString();
		}
		if (Arrays.asList(updateList).contains("email")) {
			changeEmail = true;
			newEmail = recieveString();
		}
		if (Arrays.asList(updateList).contains("password")) {
			changePassword = true;
			newPass = recieveString();
		}
		String username = recieveString();
		String password = recieveString();
		boolean update = checkAccount(username, password);
		System.out.println(update);
		if (update == false) {
			sendCode((byte)(0x40));
			return;
		}
		else {
			if (conn == null) {
				connectDB();		
			}
			try {
				if (changeEmail) {
					stmt.executeUpdate("UPDATE Users SET email='" + newEmail + "' WHERE username='" + username + "'");
				}
				if (changePassword) {
					byte[] salt = salt();
					String hash = hash(newPass, salt);
					String hexSalt = saltyString(salt);
					stmt.executeUpdate("UPDATE Users SET hash='" + hash + "' WHERE username='" + username + "'");
					stmt.executeUpdate("UPDATE Users SET salt='" + hexSalt + "' WHERE username='" + username + "'");
				}
				if (changeUsername) {
					stmt.executeUpdate("UPDATE Users SET username='" + newUsername + "' WHERE username='" + username + "'");
					connectedUser = newUsername;
				}
				sendCode((byte)(0x10));
			}
			catch (SQLException e) {
				e.printStackTrace();
				sendCode((byte)(0x60));
			}
			catch (Exception e) {
				e.printStackTrace();
				sendCode((byte)(0x60));
			}
		}
	}

	private void updateProgress() {
		String task = recieveString();
		int percentage = recieveInt();
		if (conn == null) {
			connectDB();
		}
		try {
			System.out.println("updating user progress");
			stmt.executeUpdate("UPDATE " + task + " SET Completion='" + percentage + "' WHERE Student='" + connectedUser + "'");
			updateUserCode(task);
			sendCode((byte)(0x10));
			return;
		}
		catch (SQLException e) {
			e.printStackTrace();
			sendCode((byte)(0x60));
		}
		catch (Exception e) {
			e.printStackTrace();
			sendCode((byte)(0x60));
		}
	}

	private void updateUserCode(String task) {
		try {
			String fileName = connectedUser + task;
			//recieveFile(fileName);
			if (conn == null) {
				connectDB();
			}
			rs = stmt.executeQuery("SELECT Attempts FROM " + task + " WHERE Student='" + connectedUser + "'");
			rs.next();
			int currentAttempts = rs.getInt("Attempts");
			currentAttempts++;
			stmt.executeUpdate("UPDATE " + task + " SET Attempts='" + currentAttempts + "' WHERE Student='" + connectedUser + "'");
			stmt.executeUpdate("UPDATE " + task + " Set Code='" + fileName + "' WHERE Student='" + connectedUser + "'");
			sendCode((byte)(0x10));
		}
		catch(SQLException e) {
			e.printStackTrace();
			sendCode((byte)(0x60));
		}
	}

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
			sendCode((byte)(0x60));
		}
	}

	private String[] parseList(String list, String token) {
		String[] listItems = list.split(token);
		for (int i = 0; i < listItems.length; i++) {
			System.out.println(listItems[i]);
		}
		return listItems;
	}

	// To Be Implemented
	private void sendFile(File file, String fileName) {
		try {
			outgoing.writeUTF(fileName);
			long length = file.length();
			outgoing.writeLong(length);
			byte[] buffer = new byte[16 * 1024];
			FileInputStream in = new FileInputStream(file);
	
			int count;
			while ((count = in.read(buffer)) > 0) {
				outgoing.write(buffer, 0, count);
			}
			in.close();
			sendCode((byte)(0x10));
		}
		catch (IOException e) {
			e.printStackTrace();
			sendCode((byte)(0x40));
		}
	}

	// Recieve data for a file from the client and write it.
	private void recieveFile(String fileName) {
		try {
			String givenFileName = incoming.readUTF();
			if (fileName.equals("")) {
				fileName = givenFileName;
			}
			OutputStream out = new FileOutputStream(fileName);
			
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
			sendCode((byte)(0x40));
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

	public void run() {
		// Set socket to timeout after a second if no login/account creation request
		//byte[] code = new byte[1];
		
		// Wait a max of 1 second for login or account creation instruction
		try {
			socket.setSoTimeout(5000);
		}
		catch (IOException e) {
			// Shouldn't happen. Do Nothing
		}
		// Read the code sent by the client
		//incoming.read(code)
		
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
			// Set timeout time to 1 minute
			socket.setSoTimeout(60000);
		}
		catch (IOException e) {
			// Shouldn't happen. Do Nothing
		}
		
		// TO BE UPDATED WITH FULL DUPLEX
		while(run) {
			byte request = recieveCode();
			
			switch (request) {
				// UPDATEACCOUNT
				case 0x03 :
					sendCode((byte)(0x10));
					updateAccount();
				break;
				// UPDATEMODULECOMPLETION
				case 0x04 :
					sendCode((byte)(0x10));
					updateProgress();	
				break;
				// RECIEVEFILE
				case 0x07 :
					sendCode((byte)(0x10));
					recieveFile("");
				break;
				// FETCHCODEFILE
				case 0x08 :
					sendCode((byte)(0x10));
					getUserLevelCode();
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
				// INVALIDREQUEST
				default:
					sendCode((byte)(0xF0));
				break;
			}
		}
	}
}
