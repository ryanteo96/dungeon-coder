import java.io.*;

public class StageTwo {
    private static PrintStream out;
    private static PrintStream console;
    public static void main(String[] args){
        try {
            out = new PrintStream(new File("code.txt"));
            System.setOut(out);

        }
        catch (Exception e) {
            // Shouldn't happen.
        }
        // USER WRITE CODE HERE
        String a = "No one can stop a Dungeon Coder!";
        //String a = "ARGHHHH";
        System.out.println(a);

        // DO NOT WRITE CODE PAST THIS POINT
        out.close();
    }


}
