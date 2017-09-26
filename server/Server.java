import java.sql.*;
import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int port = 37536;
		
		// Create a new socket for the server.
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		}
		catch(SocketTimeoutException s) {
			System.out.println("Socket timed out!");
		}
		
		//==============================================
		//SQL DB testing
		System.out.println("Loading Mysql Driver");	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver Loaded");
		}
		catch (ClassNotFoundException e) { 
			System.out.println("Driver not found");
		}

		String url = "jdbc:mysql://dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com/userAccounts";
		String username = "dungeoncoder";
		String password = "DungeonCoder23";
	
		try {
		// Connect to database
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected to Database");
			Statement stmt = conn.createStatement();
			
			System.out.println("Querying for user info");
			ResultSet rs=stmt.executeQuery("SELECT USERNAME, PASSWORD FROM UserAccounts");
		
			while(rs.next()) {
				System.out.println(rs.getString("USERNAME") + ", " + rs.getString("PASSWORD"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//============================================

		System.out.println("\nListening for new connections.");		
		while(true) {
			// Accept a new connection and spawn a a new thread to handle it.
			try {
				Socket socket = serverSocket.accept();
				System.out.println(socket.getRemoteSocketAddress() + " just connected.");
				ServerThread serverThread = new ServerThread(socket);
				serverThread.start();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
