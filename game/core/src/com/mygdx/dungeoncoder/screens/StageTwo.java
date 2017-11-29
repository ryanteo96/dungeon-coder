package com.mygdx.dungeoncoder.screens;
import java.io.*;

public class StageTwo {
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

	private static void right() {
		out.print("right\n");
	}
	private static void left() {
		out.print("left\n");
	}
	private static void up() {
		out.print("up\n");
	}
	private static void down() {
		out.print("down\n");
	}
	private static void delay(int duration) {
		out.print("delay," + duration + "\n");	
	}
}
