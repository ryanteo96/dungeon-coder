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

	// Send data in the form af a String to the client.
	private void sendData(String data) {
		try {
			outgoing.writeUTF(data);	
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("something went wrong. STOPPING");
			this.stop();
		}
	}

	private String recieveData() {
		String data = "";
		try {
			data = incoming.readUTF();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return data;
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

	// Check if the user exists in the database.
	private boolean userExists(String username) {
		if (conn == null) {
			connectDB();
		}
		try {
			rs = stmt.executeQuery("SELECT USERNAME FROM UserAccounts");
			while(rs.next()) {
				if (username.equals(rs.getString("USERNAME"))) {
					return true;
				}
			}
		}
		catch(SQLException e) {
			System.out.println("Something went wrong while fetching users");
			e.printStackTrace();
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
			rs = stmt.executeQuery("SELECT USERNAME, hash, salt From UserAccounts");
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
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return goodCheck;
	}

	// Attempt to log the client in with the given username and password.
	private void login() {
		String username = "";
		String password = "";
		try {
			username = incoming.readUTF();
			password = incoming.readUTF();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if (conn == null) {
			connectDB();
		}

		if (checkAccount(username, password)) {
			try {
				outgoing.write(0x10);
			}
			catch(IOException e) {
			}
			return;
		}
		try {
			outgoing.write(0x40);
		}
		catch(IOException e) {
		}
	}

	// Attempt to create a new user with given username and password.
	private void createAccount() {
		String username = "";
		String password = "";
		
		try {
			username = incoming.readUTF();
			password = incoming.readUTF();
			if (userExists(username)) {
				outgoing.write(0x40);
				return;
			}
		}
		catch (IOException e) {
		}

		byte[] salt = salt();
		String hash = hash(password, salt);
		String hexSalt = saltyString(salt);
		if (conn == null) {
			connectDB();
		}
		try {
			stmt.executeUpdate("INSERT INTO UserAccounts (USERNAME, HASH, SALT)" + "VALUES ('" + username + "','" + hash + "','" + hexSalt + "')");
		}
		catch (SQLException e) {
			try {
				outgoing.write(0x60);
			}
			catch (IOException ex) {
			}
			return;
		}
		try {
			outgoing.write(0x10);
		}
		catch (IOException e) {
		}
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

	private void updateAccount() {
		sendData("y");
		boolean changeUsername = false;
		boolean changePassword = false;
		boolean changeEmail = false;
		boolean changeClass = false;
		String list = recieveData();
		String token = recieveData();
		String[] updateList = parseList(list, token);
		if (Arrays.asList(updateList).contains("username")) {
			changeUsername = true;
		}
		if (Arrays.asList(updateList).contains("email")) {
			changeEmail = true;
		}
		if (Arrays.asList(updateList).contains("class")) {
			changeClass = true;
		}
		if (Arrays.asList(updateList).contains("password")) {
			changePassword = true;
		}
	}

	private void updateProgress() {
		sendData("y");
		int module;
		int percentage;
	}

	private String[] parseList(String list, String token) {
		String[] listItems = list.split(token);
		for (int i = 0; i < listItems.length; i++) {
			System.out.println(listItems[i]);
		}
		return listItems;
	}

	// To Be Implemented
	private void sendFile(File file) {
		
	}

	// Recieve data for a file from the client and write it.
	private void recieveFile() {
		sendData("y");
		try {
			System.out.println("recieving file name...");
			String fileName = incoming.readUTF();
			OutputStream out =  new FileOutputStream(fileName);
			System.out.println("recieving file size...");
			long fileSize = incoming.readLong();
			byte[] buffer = new byte[16 * 1024];
			int count;
			System.out.println("writing bytes to file...");
			while (fileSize > 0 && (count = incoming.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
				out.write(buffer, 0, count);
				fileSize -= count;
			}
			out.close();
		}
		catch(IOException e) {
			e.printStackTrace();
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

	private void shutdown() {
		try {
			outgoing.write(0x00);
			socket.close();
			disconnectDB();
		}
		catch(IOException e) {
		}
	}

	public void run() {
		// Set socket to timeout after a second if no login/account creation request
		byte[] code = new byte[1];
	
		try {
			socket.setSoTimeout(1000);
			incoming.read(code);
			if (code[0] == 0x01 ) {
				System.out.println("requested login");	
				outgoing.write(0x10);
				login();
			}
			else if (code[0] == 0x02 ) {
				System.out.println("requested account creation");
				outgoing.write(0x10);
				createAccount();
			}
			else {
				shutdown();
				return;
			}
		}
		catch(SocketTimeoutException e) {
			shutdown();
			return;	
		}
		catch(IOException e) {
		}

		try {
			socket.setSoTimeout(0);
		}
		catch(IOException e) {
		}
		
		//============
		//To Be updated Later
		while(true) {
			String request = "";
		
			try {
				// Wait for a request from the client.	
				System.out.println("waiting for request");
				request = incoming.readUTF();		
			}
			catch(IOException e) {
				e.printStackTrace();
				System.out.println("something went wrong. STOPPING");
			this.stop();
			}
				
			switch (request) {
				case "SendString" :
					sendData("y");
					try {
						String recieveData = incoming.readUTF();
					}
					catch (IOException ex) {
						ex.printStackTrace();
					}
				break;
			
				case "SendFile" :
					recieveFile();
				break;

				case "CreateAccount" :
					createAccount();
				break;

				case "login" :
				break;

				default:
					sendData("n");
				break;
			}
		}
	}
}
