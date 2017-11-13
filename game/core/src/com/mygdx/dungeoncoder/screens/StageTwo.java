package com.mygdx.dungeoncoder.screens;

public class StageTwo{
    private final TaskTwo taskTwo;

    public StageTwo(TaskTwo taskTwo){
        this.taskTwo = taskTwo;
        pleaseMoveRightIBegYouFFS();
    }

    public void pleaseMoveRightIBegYouFFS(){
        taskTwo.movedRight();
    }


}
