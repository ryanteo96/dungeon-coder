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

	public void run() {
		while(true) {
			try {	
				System.out.println(incoming.readUTF());		
			}	
			catch(IOException e) {
				e.printStackTrace();
				System.out.println("something went wrong. STOPPING");
			this.stop();
			}
			sendData("pong");	
		}
	}
}
