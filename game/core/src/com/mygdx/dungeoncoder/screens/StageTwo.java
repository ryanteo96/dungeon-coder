package com.mygdx.dungeoncoder.screens;

import com.mygdx.dungeoncoder.DungeonCoder;
import java.io.FileNotFoundException;

public class StageTwo{
    private final TaskTwo taskTwo;

    public StageTwo(DungeonCoder g) throws FileNotFoundException {
       taskTwo = new TaskTwo(g);
        pleaseMoveRightFFS();
    }

    public void pleaseMoveRightFFS(){
        getTaskTwo().movedRight();
    }


    public TaskTwo getTaskTwo() {
        return taskTwo;
    }
}
