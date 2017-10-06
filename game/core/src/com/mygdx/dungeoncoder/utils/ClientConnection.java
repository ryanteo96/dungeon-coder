package com.mygdx.dungeoncoder.utils;
import java.net.*;
import java.io.*;

/*	CLIENT CONNECTION
 * Creates a new connection to the server via sockets.
 * A new connection is instantiated when a client attempts to login.
 * Handles all requests and responses to and from the server.
 */
public class ClientConnection {
	public String ip = "13.59.183.75";
	int port = 37536;
	Socket client;
	DataOutputStream outgoing;
	DataInputStream incoming;

	// Initialize new Client Connection
	public ClientConnection(String username, String password) {
		try {
			Socket client = new Socket(ip, port);
			
			OutputStream toServer = client.getOutputStream();
			outgoing = new DataOutputStream(toServer);
			
			InputStream fromServer = client.getInputStream();
			incoming = new DataInputStream(fromServer);
		}
		catch(IOException e) {
			// Connection failed. Do Something
		}
	}

	private void sendCode(byte code) {
		try {
			outgoing.write(code);
		}
		catch (IOException e) {
			// Do Something
		}
	}

	private byte recieveCode() {
		byte code = (byte)(0xEE);
		try {
			code = (byte)(incoming.read());
		}
		catch (IOException e) {
			// Do Something
		}
		return code;
	}

	// Should be called right after a new client connection is created
	public boolean requestLogin(String username, String password) {
		try {
			sendCode((byte)(0x01));
			if (recieveCode() == 0x10) {
				outgoing.writeUTF(username);
				outgoing.writeUTF(password);
				byte outcome = recieveCode();
				if (outcome == 0x10) {
					return true;
				}
				// Database failure.
				else if (outcome == 0x60) {
					return false;
				}
				// Invalid credantials
				else if (outcome == 0x40) {
					return false;
				}
			}
			else {
				// Server refused login attempt.
				return false;
			}
		}
		catch (IOException e) {
			// Do nothing.
		}
		return false;
	}

	// Can also be called right after a new client connection is created
	public boolean requestAccountCreation(String username, String password) {
		try {
			sendCode((byte)(0x02));
			if (recieveCode() == 0x10) {
				outgoing.writeUTF(username);
				outgoing.writeUTF(password);
				byte outcome = recieveCode();
				if (outcome == 0x10) {
					return true;
				}
				else if (outcome == 0x40) {
					// Account exists with that username
					return false;
				}
				else if (outcome == 0x60) {
					// Database failure
					return false;
				}
			}
			else {
				// Server refused account creation.
				return false;
			}
		}
		catch (IOException e) {
			// Do nothing.
		}
		return false;
	}

	// updateFields should be in the format "field1,field2,field3"
	// newInfo should be ordered username, email, password
	public boolean requestAccountUpdate(String username, String password, String updateFields, String token, String[] newInfo) {
		try {
			sendCode((byte)(0x03));
			if (recieveCode() == 0x10) {
				outgoing.writeUTF(updateFields);
				outgoing.writeUTF(token);
				for (int i = 0; i < newInfo.length; i++) {
					outgoing.writeUTF(newInfo[i]);
				}
				outgoing.writeUTF(username);
				outgoing.writeUTF(password);
				byte outcome = recieveCode();
				if (outcome == 0x10) {
					return true;
				}
				else if (outcome == 0x60) {
					// Database failure
					return false;
				}
				else if (outcome == 0x40) {
					// Account authentication failed
					return false;
				}
			}
			else {
				// Server refused account update
				return false;
			}
		}
		catch (IOException e) {
			// Do nothing
		}	
		return false;
	}

	public boolean requestUpdateProgress(File file, String task, int percentage) {
		try {
			sendCode((byte)(0x04));
			if (recieveCode() == 0x10) {
				outgoing.writeUTF(task);
				outgoing.writeInt(percentage);
				sendFile(file, "");
				byte outcome = recieveCode();
				if (outcome == 0x10) {
					return true;
				}
				else if (outcome == 0x60) {
					// Database failure
					return false;
				}
			}
			else {
				// Server refused update progress
				return false;
			}
		}
		catch (IOException e) {
			// Do nothing
		}
		return false;
	}

	private boolean sendFile(File file, String fileName) {
		sendCode((byte)(0x07));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(fileName);
				long length = file.length();
				outgoing.writeLong(length);
				byte[] buffer = new byte[16 * 1024];
				FileInputStream in = new FileInputStream(file);
				int count;
				while ((count = in.read(buffer)) > 0) {
					outgoing.write(buffer, 0, count);
				}
				in.close();
				byte outcome = recieveCode();
				if (outcome == 0x10) {
					return true;
				}
				else if (outcome == 0x40) {
					// Something failed (probably io)
					return false;
				}
				
			}
			catch (IOException e) {
				// Do Nothing
			}
		}
		else {
			// Server refused file transfer
			return false;
		}
		return false;
	}

	private boolean recieveFile(String fileName) {
		try {
			String givenFileName = incoming.readUTF();
			if (fileName.equals("")) {
				fileName = givenFileName;
			}
			OutputStream out = new FileOutputStream(fileName);

			long fileSize = incoming.readLong();
			byte[] buffer = new byte[16 * 1024];
			int count;

			while (fileSize > 0 && (count = incoming.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
				out.write(buffer, 0, count);
				fileSize -= count;
			}
			out.close();
			sendCode((byte)(0x10));
		}
		catch(IOException e) {
			// Do Nothing
		}
		return false;
	}

	// Request the specific information for the user on the specific task
	public String requestTaskInformation(String task, String information) { 
		sendCode((byte)(0x0A));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(task);
				outgoing.writeUTF(information);
				byte outcome = recieveCode();
				if (outcome == 0x10) {
					return incoming.readUTF();
				}
				else if (outcome == 0x60) {
					// Database error.
					return "";
				}
			}
			catch (IOException e) {
				// Do Nothing
			}
		}
		else {
			// Server refused information request
			return "";
		}
		return "";
	}

	private boolean requestCodeFile(String levelName) {
		sendCode((byte)(0x09));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(levelName);
				recieveFile("");
				return true;
			}
			catch (IOException e) {
				// Do Nothing
			}
		}
		else {
			// Server refused file request
			return false;
		}
		return false;
	}
}
