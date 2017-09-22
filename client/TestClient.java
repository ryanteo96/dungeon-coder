import java.net.*;
import java.io.*;

public class TestClient {
	public static void main(String[] args) throws IOException {
		String ip = "13.59.183.75";
		int port = 37536;
		
		// Attempt to connect to the server and send/recieve raw data.	
		try {
			// Create a new socket to connect to the server.
			System.out.println("Connecting to " + ip + " on port " + port + "...");
			Socket client = new Socket(ip, port);
			System.out.println("Connected to " + client.getRemoteSocketAddress());
			
			// Create an output stream for data going to the server.
			OutputStream toServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(toServer);
			
			// Send data to server.
			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			
			// Create an input stream for data from server.
			InputStream fromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);
			
			// TEMP! Loop forever recieving data from server
			// Used to test multi-thread server		
			while(true) {
				// Read recieved data.
				System.out.println(in.readUTF());
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
