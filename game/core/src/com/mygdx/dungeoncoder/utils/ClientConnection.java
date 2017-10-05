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
		catch (IOException i) {
			// Do nothing
		}	
		return false;
	}
 
}
