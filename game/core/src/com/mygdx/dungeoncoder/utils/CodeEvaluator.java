package com.mygdx.dungeoncoder.utils;

import java.io.*;
import java.nio.Buffer;

public class CodeEvaluator {

	public CodeEvaluator() {
	}

    	public static boolean evaluate(String filePath) {
        	boolean compiled = false;
		try {
        	    compiled = compile("javac " + filePath);
        	} catch (Exception e) {
        	}
		return compiled;
    	}

	private static void printLines(String name, InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			System.out.println(name + " " + line);
		}
	}

      	private static boolean compile(String command) throws Exception {
        	Process pro = Runtime.getRuntime().exec(command);
		printLines(command + " stdout:", pro.getInputStream());
		printLines(command + " stderr:", pro.getErrorStream());
        	pro.waitFor();

      		if (pro.exitValue() == 0) {
			return true;
		}
		return false;
	}

	private static boolean run(String filePath) throws Exception {
		String command = "java " + filePath;
		Process pro = Runtime.getRuntime().exec(command);
		printLines(command + " stdout: ", pro.getInputStream());
		printLines(command + " stderr: ", pro.getErrorStream());
		pro.waitFor();
		if (pro.exitValue() == 0) {
			return true;
		}
		return false;
	}
}
