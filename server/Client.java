import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) throws IOException {
		String ip = "ec2-18-221-46-132.us-east-2.compute.amazonaws.com";
		int port = 37536;
		
		try {
			System.out.println("Connecting to " + ip + " on port " + port);
			Socket client = new Socket(ip, port);
			System.out.println("Connected to " + client.getRemoteSocketAddress());
			OutputStream toServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(toServer);
			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream fromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);
			client.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
