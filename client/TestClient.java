import java.net.*;
import java.io.*;

public class TestClient {
	public static void main(String[] args) {
		String ip = "13.59.183.75";
		int port = 37536;
		int numPings = 0;
		double avgResponseTime=0;	
		// Attempt to connect to the server and send/recieve raw data.	
		try {
			// Create a new socket to connect to the server.
			System.out.println("Connecting to " + ip + " on port " + port + "...");
			Socket client = new Socket(ip, port);
			System.out.println("Connected to " + client.getRemoteSocketAddress());
			
			// Create an output stream for data going to the server.
			OutputStream toServer = client.getOutputStream();
			DataOutputStream outgoing = new DataOutputStream(toServer);
			
			// Send data to server.
			outgoing.writeUTF("Hello from " + client.getLocalSocketAddress());
			
			// Create an input stream for data from server.
			InputStream fromServer = client.getInputStream();
			DataInputStream incoming = new DataInputStream(fromServer);
			incoming.readUTF();
			// TEMP! Loop forever recieving data from server
			// Used to test multi-thread server		
			while(true) {
				System.out.println("loop");	
				// Generate a random number of
				int wait = (int)(Math.random() * 600 + 1);
				wait *= 1000; // convert wait to seconds
				try {
					Thread.sleep(wait);
				}
				catch(InterruptedException i) {
					System.out.println("sleep interrupted");
				}

				// Generate a string of between 512 kB to 1 mB.
				int byteCount = (int)(Math.random() * (Math.pow(2, 20) - Math.pow(2, 19) +Math.pow(2, 19)));
				System.out.println(byteCount/2);
				
				// Write the string to a file.
				try {
					PrintWriter writer = new PrintWriter("Data.txt", "UTF-8");
					for (int i = 0; i < byteCount; i+=2) {
						writer.print("A");
					}
					writer.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
				try {
				outgoing.writeUTF("SendFile");
					String response = incoming.readUTF();
					if (response.equals("y")) {
						File file = new File("Data.txt");
						outgoing.writeUTF("Data.txt");
						long length = file.length();
						outgoing.writeLong(length);
						byte[] buffer = new byte[16 * 1024];
						FileInputStream in = new FileInputStream(file);
						int count;
						while((count = in.read(buffer)) > 0) {
							outgoing.write(buffer, 0, count);
						}
						in.close();
					}
					else {
						System.out.println("Server Refused File Transefer \n");
					}
					numPings++;
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
