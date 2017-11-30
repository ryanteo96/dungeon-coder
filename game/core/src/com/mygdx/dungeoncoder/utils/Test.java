package com.mygdx.dungeoncoder.utils;

import com.mygdx.dungeoncoder.utils.ClientConnection;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class Test {
	private static ClientConnection conn = null;
	
	public static void main(String[] args) {
		conn = new ClientConnection();
		conn.requestLogin("Devon", "Password");
		codeTransfer();
		levelTransfer();
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
}
