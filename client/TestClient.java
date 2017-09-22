import java.net.*;
import java.io.*;

public class TestClient {
	public static void main(String[] args) throws IOException {
	//	String ip = "ec2-18-221-46-132.us-east-2.compute.amazonaws.com";
		//String ip = "18.221.46.132";
		String ip = "13.59.183.75";
		int port = 37536;
		
		try {
			System.out.println("Connecting to " + ip + " on port " + port + "...");
			Socket client = new Socket(ip, port);
			System.out.println("Connected to " + client.getRemoteSocketAddress());
			OutputStream toServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(toServer);
			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream fromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);
			System.out.println(in.readUTF());
			client.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
