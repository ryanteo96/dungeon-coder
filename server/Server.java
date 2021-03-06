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
		
		while(true) {
			// Accept a new connection and spawn a a new thread to handle it.
			System.out.println("Listening for new connection...");
			try {
				Socket socket = serverSocket.accept();
				System.out.println(socket.getRemoteSocketAddress() + " just connected.");
				ServerThread serverThread = new ServerThread(socket);
				serverThread.start();
			}
			catch(IOException e) {
			}
		}
	}
}
