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
			
			// Create an input stream for data from server.
			InputStream fromServer = client.getInputStream();
			DataInputStream incoming = new DataInputStream(fromServer);

			// Attempt to login to an existing account		
			outgoing.write((byte)(0x01));
			
			System.out.println("Attempting to login to account");
			if (incoming.read() == 0x10) {
				outgoing.writeUTF("TestUser");
				outgoing.writeUTF("NewPass");
				if (incoming.read() == 0x10) {
					System.out.println("login successful");
				}
				else {
					System.out.println("username or password incorrect");
				}

			}
			else {
				System.out.println("Server refused login attempt");
			}
			
			System.out.println("Attempting to update acccount information");
			outgoing.write((byte)(0x03));
			if (incoming.read() == 0x10) {
				outgoing.writeUTF("username,email,class,password");
				outgoing.writeUTF(",");
				outgoing.writeUTF("TestUser");
				outgoing.writeUTF("test@gmail.com");
				outgoing.writeUTF("cs99");
				outgoing.writeUTF("NewPass");
				if (incoming.read() == 0x30) {
					outgoing.writeUTF("TestUser");
					outgoing.writeUTF("pass");
					if (incoming.read() == 0x40) {
						System.out.println("Account verification failed");
					}
					else {
						System.out.println("Update complete");
					}
				}
				else {
					System.out.println("Something went wrong.");
				}
			}
			else {
				System.out.println("Server refused account update");
			}
			

			// TEMP! Loop forever recieving data from server
			// Used to test multi-thread server
			//=======================================
			// Code for generating random files for transfer
			/*
			while(true) {
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
				int byteCount = (int)(Math.random() * (Math.pow(2, 24) - Math.pow(2, 20) +Math.pow(2, 20)));
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
				
			}*/
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
