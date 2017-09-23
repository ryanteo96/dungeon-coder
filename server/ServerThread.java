import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
	private Socket socket;
	DataInputStream incoming;
	DataOutputStream outgoing;
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

	public void sendData(String data) {
		try {
			outgoing.writeUTF(data);	
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("something went wrong. STOPPING");
			this.stop();
		}
	}

	// To Be Implemented
	public void sendFile(File file) {
		
	}

	public void recieveFile() {

	}	

	public void run() {
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
					sendData("y");
					try {
						System.out.println("recieving file name");
						String fileName = incoming.readUTF();
						OutputStream out = new FileOutputStream(fileName);
						System.out.println("recieving file size");
						long fileSize = incoming.readLong();

						byte[] buffer = new byte[16 * 1024];
						int count;
						System.out.println("writing bytes to file");
						while (fileSize > 0 && (count = incoming.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
							out.write(buffer, 0, count);
							fileSize -= count;
						}
						out.close();
					}
					catch (IOException ex) {
						ex.printStackTrace();
					}
				break;

				default:
					sendData("n");
				break;
			}
		}
	}
}
