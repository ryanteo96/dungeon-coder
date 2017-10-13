import java.io.*;

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

      	private static boolean compile(String command) throws Exception {
        	Process pro = Runtime.getRuntime().exec(command);
        	pro.waitFor();

      		if (pro.exitValue() == 0) {
			return true;
		}
		else {
			return false;
		}
	}
}
