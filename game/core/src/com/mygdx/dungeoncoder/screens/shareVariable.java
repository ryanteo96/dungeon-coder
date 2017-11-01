package com.mygdx.dungeoncoder.screens;
import com.mygdx.dungeoncoder.utils.ClientConnection;


public class shareVariable {
    	public static ClientConnection connect = null;
	public static Pinger ping = null;

    public static boolean connected = false;
    public static final int WIDTH = 650;
    public static final int HEIGHT = 500;
    public static final int PPM = 100; //1meter = 100pixels PPM = pixel per meter

	public static class Pinger extends Thread {
		public Pinger() {
		}

		public void run() {
			try {
				this.sleep(30000);
			}
			catch(InterruptedException e) {
			}
			shareVariable.connect.ping();
		}
	}
}
