package com.mygdx.dungeoncoder.utils;
import java.net.*;
import java.io.*;

public class ClientConnection {
	public String ip = "13.59.183.75";
	int port = 37536;
	Socket client;
	DataOutputStream outgoing;
	DataInputStream incoming;
	public ClientConnection() {
		try {
			Socket client = new Socket(ip, port);
			OutputStream toServer = client.getOutputStream();
			outgoing = new DataOutputStream(toServer);
			InputStream fromServer = client.getInputStream();
			outgoing.writeUTF("Hello from " + client.getLocalSocketAddress());
			incoming = new DataInputStream(fromServer);
			incoming.readUTF();
		}
		catch(IOException e) {
			// Do Nothing
		}
	}

	public boolean requestLogin(String username, String password) {
		boolean login = false;
		try {
			outgoing.writeUTF("login");
			if (incoming.readUTF().equals("y")) {
				outgoing.writeUTF(username);
				outgoing.writeUTF(password);
				if (incoming.readUTF().equals("login successful")) {
					login = true;
				}
			}
		}
		catch(IOException e) {
			// Do nothing.
		}
		return login;
	}   
}

