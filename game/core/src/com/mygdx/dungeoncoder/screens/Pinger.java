package com.mygdx.dungeoncoder.screens;

public class Pinger extends Thread {
	
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
