package com.mygdx.dungeoncoder.utils;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

/*	CLIENT CONNECTION
 * Creates a new connection to the server via sockets.
 * A new connection is instantiated when a client attempts to login.
 * Handles all requests and responses to and from the server.
 */
public class ClientConnection {
	private String ip = "18.216.178.229";
	private int port = 37536;
	private Socket client;
	private DataOutputStream outgoing;
	private DataInputStream incoming;
	private ArrayList<String> messages = new ArrayList<String>();
	
	// Initialize new Client Connection
	public ClientConnection() {
		try {
			client = new Socket(ip, port);
			client.setSoTimeout(1000);	
			OutputStream toServer = client.getOutputStream();
			outgoing = new DataOutputStream(toServer);
			
			InputStream fromServer = client.getInputStream();
			incoming = new DataInputStream(fromServer);
		}
		catch(IOException e) {
			// Connection failed. Do Something
		}
	}

	private synchronized void sendCode(byte code) {
		try {
			outgoing.write(code);
		}
		catch (IOException e) {
			// Do Something
		}
	}

	private synchronized byte recieveCode() {
		byte code = (byte)(0xEE);
		try {
			code = (byte)(incoming.read());
		}
		catch (SocketTimeoutException e) {
			// Close Connection
		}
		catch (IOException e) {
			// Do Something
		}
		return code;
	}

	// Should be called right after a new client connection is created
	public synchronized boolean requestLogin(String username, String password) {
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
	public synchronized boolean requestAccountCreation(String username, String password) {
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
	public synchronized boolean requestAccountUpdate(String username, String password, String updateFields, String token, String[] newInfo) {
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
				if (recieveCode() == 0x40) {
					return false;
				}
				//byte outcome = recieveCode();
				if (recieveCode() == 0x10) {
					return true;
				}
				/*else if (outcome == 0x60) {
					// Database failure
					return false;
				}
				else if (outcome == 0x40) {
					// Account authentication failed
					return false;
				}*/
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

	public synchronized String requestAccountEmail() {
		try {
			sendCode((byte)(0x13));
			if (recieveCode() == 0x10) {
				String emailAddress = incoming.readUTF();
				return emailAddress;
			}
			else {
				return "";
			}
		}
		catch (IOException e) {
			// Do nothing
		}
		return "";
	}

	public synchronized boolean requestLockStatus() {
		try {
			sendCode((byte)(0x23));
			if (recieveCode() == 0x10) {
				String result = incoming.readUTF();
				if (result.equals("false")) {
					return false;
				}
				else if (result.equals("true")) {
					return true;
				} 
			}
		}
		catch (IOException e) {
			// Do nothing
		}
		return true;
	}

	public synchronized boolean requestUpdateProgress(File file, String task, int percentage) {
		try {
			sendCode((byte)(0x04));
			if (recieveCode() == 0x10) {
				outgoing.writeUTF(task);
				outgoing.writeInt(percentage);
				sendFile(file, "");
				//byte outcome = recieveCode();
				if (recieveCode() == 0x10) {
					return true;
				}/*
				else if (outcome == 0x60) {
					// Database failure
					return false;
				}*/
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

	public synchronized boolean sendFile(File file, String fileName) {
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
			//byte outcome = recieveCode();
			if (recieveCode() == 0x10) {
				return true;
			}/*
			else if (outcome == 0x40) {
				// Something failed (probably io)
				return false;
			}*/				
		}
		catch (IOException e) {
			// Do Nothing
		}
		return false;
	}

	private synchronized void recieveFile(String fileName) {
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
		}
		catch(IOException e) {
			// Do Nothing
			e.printStackTrace();
		}
	}

	// Request the specific information for the user on the specific task
	public synchronized String requestTaskInformation(String task, String information) { 
		sendCode((byte)(0x0A));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(task);
				outgoing.writeUTF(information);
				//byte outcome = recieveCode();
				if (recieveCode() == 0x10) {
					return incoming.readUTF();
				}/*
				else if (outcome == 0x60) {
					// Database error.
					return "";
				}*/
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

	public synchronized boolean requestCodeFile(String fileName) {
		sendCode((byte)(0x08));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(fileName);
				recieveFile(fileName);
				return true;
			}
			catch (IOException e) {
				e.printStackTrace();
				// Do Nothing
			}
		}
		// Server refused file request
		return false;
	}

	public synchronized boolean requestMostRecentLevelCode(String levelName, String fileName) {
		sendCode((byte)(0x18));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(levelName);
				recieveFile(fileName);
				return true;
			}
			catch (IOException e) {
				// Do Nothing
			}
		}
		// Server refused file request
		return false;
	}

	public synchronized void ping() {
		sendCode((byte)(0xFF));
		recieveCode();
		//while(recieveCode() != 0xFF) {
			//try {
				//messages.add(incoming.readUTF());
			//}
			//catch(IOException e) {
				// Do Nothing
			//}
	}


	public synchronized boolean uploadLevel(String levelName, File level) {
		sendCode((byte)(0x0B));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(levelName);
				sendFile(level, "");
			}
			catch (IOException e) {
				// Do Nothing
			}
		}
		//Server refused level upload
		return false;
	}

	public synchronized ArrayList<String> requestLevelList() {
		ArrayList<String> levels = new ArrayList<String>();
		sendCode((byte)(0x0C));
		if (recieveCode() == 0x10) {
			String levelName = "";
			while(levelName.equals("\r\n") == false) {
				try {
					levelName = incoming.readUTF();
				}
				catch (IOException e) {
				// Do Nothing
				}
				levels.add(levelName);
			}
		}
		else {
		}
		levels.remove(levels.size() - 1);
		return levels;
	}

	public synchronized boolean requestCustomLevel(String levelName) {
		sendCode((byte)(0x0D));
		if (recieveCode() == 0x10) {
			try {
				outgoing.writeUTF(levelName);
			}
			catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			recieveFile(levelName);
			return true;
		}
		// Server refused level transfer
		return false;
	}

	public synchronized ArrayList<String> getMessages() {
		return messages;
	}
}
