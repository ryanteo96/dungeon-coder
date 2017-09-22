import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int port = 37536;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		}
		catch(SocketTimeoutException s) {
			System.out.println("Socket timed out!");
		}
		while(true) {
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

		/*
		// Handle's only one connection at a time.
		while(true) {
			try {
				// Wait for client to connect to server
				System.out.println("Waiting for client on port " + port + "...");
				
				// Create new Server Socket for the server.
				ServerSocket serverSocket = new ServerSocket(port);
				
				// Accept incoming connection.
				Socket server = serverSocket.accept();
				System.out.println(server.getRemoteSocketAddress() + " just connected.");
				// Create a new Data Input Stream to recieve data from the client.
				DataInputStream toServer = new DataInputStream(server.getInputStream());
			System.out.println(toServer.readUTF());

				// Create a new Data Output Stream to send data to the client.
				DataOutputStream toClient = new DataOutputStream(server.getOutputStream());
				// Send data to the client.
				toClient.writeUTF("You have connected to " + server.getLocalSocketAddress());
			}
			catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
				break;
			}
		}
		*/
	}
}
