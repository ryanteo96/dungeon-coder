package com.mygdx.dungeoncoder.utils;
import com.mygdx.dungeoncoder.utils.ClientConnection;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class Test {
	private static ClientConnection conn = null;
	
	public static void main(String[] args) {
		conn = new ClientConnection();
		conn.requestAccountCreation("Devon1", "Password");
		conn.requestLogin("Devon1", "Password");
		incorrectUsername();
		incorrectPassword();
		accountUpdateSingle();
		accountUpdateDouble();
		accountUpdateTriple();
		codeTransfer();
		specificCodeTransfer();
		levelTransfer();
		levelListTransfer();
	}

	private static void incorrectUsername() {
		ClientConnection newConn = new ClientConnection();
		if (newConn.requestLogin("BADUSER", "Password")) {
			System.out.println("Incorrect Username Failed");
		}
		else {
			System.out.println("Incorrect Username Passed");
		}
	}

	private static void incorrectPassword() {
		ClientConnection newConn = new ClientConnection();
		if (newConn.requestLogin("Devon1", "BADPASS")) {
			System.out.println("Incorrect Password Failed");
		}
		else {
			System.out.println("Incorrect Username Passed");
		}
	}	

	private static void accountUpdateSingle() {
		String[] newInfo = new String[1];
		newInfo[0] = "pa55w0rd";
		conn.requestAccountUpdate("Devon1", "Password", "password", ",", newInfo);
		ClientConnection newConn = new ClientConnection();
		if (newConn.requestLogin("Devon1", "pa55w0rd")) {
			System.out.println("Update only Password Passed");
			accountReset("Devon1", newInfo[0]);
		}
		else {
			System.out.println("Upadate only Password Failed");
		}
	}

	private static void accountUpdateDouble() {
		String[] newInfo = new String[2];
		newInfo[0] = "Noved";
		newInfo[1] = "AnotherPass";
		conn.requestAccountUpdate("Devon1", "Password", "username,password", ",", newInfo);
		ClientConnection newConn = new ClientConnection();
		if (newConn.requestLogin("Noved", "AnotherPass")) {
			System.out.println("Update Username and Password Passed");
			accountReset(newInfo[0], newInfo[1]);
		}
		else {
			System.out.println("Update Username and Password Failed");
		}
	}

	private static void accountUpdateTriple() {
		String[] newInfo = new String[3];
		newInfo[0] = "CoolCatz";
		newInfo[1] = "CoolCat83xx@catzRus.com";
		newInfo[2] = "KATZRULE!";
		conn.requestAccountUpdate("Devon1", "Password", "username,email,password", ",", newInfo);
		ClientConnection newConn = new ClientConnection();
		if (newConn.requestLogin("CoolCatz", "KATZRULE!")) {
			String returnedEmail = newConn.requestAccountEmail();
			if (returnedEmail.equals("CoolCat83xx@catzRus.com")) {
				System.out.println("Update Username, Password, and Email Passed");
			}
			else {
				System.out.println("Update Username, Password, and Email Failed");
			}
			accountReset(newInfo[0], newInfo[2]);
		}
		else {
			System.out.println("Update Username, Password, and Email Failed");
		}	
	}
	
	private static void accountReset(String username, String password) {
		String[] newInfo = new String[3];
		newInfo[0] = "Devon1";
		newInfo[2] = "Password";
		newInfo[1] = "Devon@dungeoncoder.com";
		conn.requestAccountUpdate(username, password, "username,email,password", ",", newInfo);
	}

	private static void codeTransfer() {
		File temp = new File("original.txt");
		byte[] data = new byte[128];
		new Random().nextBytes(data);
		try {
			FileOutputStream fos = new FileOutputStream(temp);
			fos.write(data);
			fos.close();
		}
		catch (IOException e) {
			System.out.println("Failed to create file");
			return;
		}
		conn.requestUpdateProgress(temp, "Task1", 100);
		conn.requestMostRecentLevelCode("Task1", "returned.txt");
	}

	private static void specificCodeTransfer() {
		conn.requestCodeFile("SpecificB.txt");
	}

	private static void levelTransfer() {
		File temp = new File("original2.txt");
		byte[] data = new byte[128];
		new Random().nextBytes(data);
		try {
			FileOutputStream fos = new FileOutputStream(temp);
			fos.write(data);
			fos.close();
		}
		catch (IOException e) {
			System.out.println("Failed to create file");
			return;
		}
		conn.uploadLevel("Devon's Level", temp);
		conn.requestCustomLevel("Devon's Level");
	}

	private static void levelListTransfer() {
		ArrayList<String> levels = conn.requestLevelList();
		for (int i = 0; i < levels.size(); i++) {
			System.out.println(levels.get(i));
		}
	}
}
