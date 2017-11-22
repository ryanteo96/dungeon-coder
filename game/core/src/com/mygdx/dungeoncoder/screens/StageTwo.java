package com.mygdx.dungeoncoder.screens;
import java.io.*;

public class StageTwo{
	private static PrintWriter out;
	
	public static void main(String args[]){
		try {
			out = new PrintWriter(new FileWriter("code.txt"));
		}
		catch (Exception e) {
			// Shouldn't happen.
		}
		// USER WRITE CODE HERE



		// DO NOT WRITE CODE PAST THIS POINT
		out.close();
	}

	private void right() {
		out.print("right\n");
	}
	private void left() {
		out.print("left\n");
	}
	private void up() {
		out.print("up\n");
	}
	private void down() {
		out.print("down\n");
	}
	private void delay(int duration) {
		out.print("wait," + duration + "\n");	
	}
}
