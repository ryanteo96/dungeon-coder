import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int portNumber = 37536;
		try {
			ServerSocket server = new ServerSocket(portNumber);
			Socket client = server.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
