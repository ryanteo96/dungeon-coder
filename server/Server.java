import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("Server ip is : " + ip.getHostAddress());	
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
		String ip2 = in.readLine();
		System.out.println("What's my ip : " + ip2);

		int port = 37536;
		while(true) {
			try {
				System.out.println("Waiting for client on port " + port + "...");
				ServerSocket serverSocket = new ServerSocket(port);
				Socket server = serverSocket.accept();
				System.out.println(server.getRemoteSocketAddress() + " just connected.");
				DataInputStream toServer = new DataInputStream(server.getInputStream());
			System.out.println(toServer.readUTF());
				DataOutputStream toClient = new DataOutputStream(server.getOutputStream());
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
	}
}
