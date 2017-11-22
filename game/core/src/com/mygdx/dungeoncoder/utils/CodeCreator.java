import java.io.*;

public class CodeCreator {
	
	private static String filePath;
	private static File userCode;

	public CodeCreator(String filePath, File userCode) {
		this.filePath = filePath;
		this.userCode = userCode;
	}

	public static void main(String[] args) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(filePath));
			
			partOne(out);

			FileReader in = new FileReader(userCode);
			BufferedReader bufferedReader = new BufferedReader(in);
			String line;
			while((line = bufferedReader.readLine()) != null) {
				out.print(line + "\n");
			}
			in.close();

			partTwo(out);
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void partOne(PrintWriter out) {
		out.print("import java.io.*;\n");
		out.print("public class StageTwo {\n");
		out.print("\tpublic static PrintWriter out;\n");
		out.print("\tpublic static void main(String args[]) {\n");
		out.print("\t\ttry {\n");
		out.print("\t\t\tout = new PrintWriter(new FileWriter(\"code.txt\"));\n");
		out.print("\t\t}\n");
		out.print("\t\tcatch (Exception e) {\n");
		out.print("\t\t}");
	}

	private static void partTwo(PrintWriter out) {
		out.print("\t\tout.close()\n");
		out.print("\t}\n");
		out.print("\tprivate static void right() {\n");
		out.print("\t\tout.print(\"right\\n\")\n");
		out.print("\t}\n");
		out.print("\tprivate static void left() {\n");
		out.print("\t\tout.print(\"left\\n\")\n");
		out.print("\t}\n");
		out.print("\tprivate static void up() {\n");
		out.print("\t\tout.print(\"up\\n\")\n");
		out.print("\t}\n");
		out.print("\tprivate static void down() {\n");
		out.print("\t\tout.print(\"down\\n\")\n");
		out.print("\t}\n");
		out.print("\tprivate static void delay(int duration) {\n");
		out.print("\t\tout.print(\"delay,\" + duration + \"\\n\"");
		out.print("\t}\n");
		out.print("}\n");
	}
}
