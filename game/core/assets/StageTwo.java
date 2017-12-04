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
         //USER WRITE CODE HERE
         
        int x = 5;
        int y = 10;
        int z = 3;
        if( x < z){
           //write your code here
           //print something here
          System.out.println("x is less than z");
        }else{
          System.out.println("x is not less than z");
        }
        // DO NOT WRITE CODE PAST THIS POINT
        out.close();
    }


}
