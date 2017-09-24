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
		
		try {
			// Get driver for mysql
			Class.forName("com.mysql.jdbc.Driver");
			// Connect to database
			Connection con = DriverManager.getConnection("jbdc:mysql://localhost:3306/NAME", "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("query");
			while(rs.next()) {
				
			}
		}
		catch (Exception e) {
			System.out.println("failed to connect to database");
			//e.printStackTrace();
		}
		
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
