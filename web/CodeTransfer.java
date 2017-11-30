import java.net.*;
import java.io.*;
public class CodeTransfer {
	private static String ip = "18.216.178.229";
	private static int port = 37536;
	private static Socket webClient;
	private static DataOutputStream outgoing;
	private static DataInputStream incoming;

	public static void main(String[] args) {
		String fileName = args[0];
		String username = args[1];
		String password = args[2];
		try {
			webClient = new Socket(ip, port);
			webClient.setSoTimeout(1000);
			
			OutputStream toServer = webClient.getOutputStream();
			outgoing = new DataOutputStream(toServer);

			InputStream fromServer = webClient.getInputStream();
			incoming = new DataInputStream(fromServer);
		
			sendCode((byte)(0x01));
			if (recieveCode() == 0x10) {
				outgoing.writeUTF(username);
				outgoing.writeUTF(password);
				byte outcome = recieveCode();
				if (outcome != 0x10) {
					return;
				}
			}
			sendCode((byte)(0x08));
			if (recieveCode() != 0x10) {
				return;
			}
			outgoing.writeUTF(fileName);
			incoming.readUTF();
			OutputStream out = new FileOutputStream("files/" + fileName);
			long fileSize = incoming.readLong();
			byte[] buffer = new byte[16 * 1024];
			int count;
			while (fileSize > 0 && (count = incoming.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
				out.write(buffer, 0, count);
				fileSize -= count;
			}
			out.close();
		}
		catch (IOException e) {
			// Do Nothing
		}
	}

	private static void sendCode(byte code) {
		try {
			outgoing.write(code);
		}
		catch (IOException e) {
			// Do Nothing
		}
	}
	
	private static byte recieveCode() {
		byte code = (byte)(0xEE);
		try {
			code = (byte)(incoming.read());
		}
		catch (SocketTimeoutException e) {
			// Do Nothing
		}
		catch (IOException e) {
			// Do Nothing
		}
		return code;
	}
}
