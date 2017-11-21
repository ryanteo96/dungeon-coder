import java.io.FileWriter;
import java.io.IOException;
public class StageTwo{

    public static void main(String[] args){
        movedRight();

    }

    private void movedRight(){
        File file = new File("code.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("movedRight();");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void movedLeft(){
        File file = new File("code.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("movedLeft();");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void jump(){
        File file = new File("code.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("jump();");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
