package com.mygdx.dungeoncoder.utils;


import com.mygdx.dungeoncoder.DungeonCoder;

import java.io.*;
import java.util.Scanner;

public class SaveProcessor {
    int insCleared, mainCleared, freeCleared;
    int totalCleared;
    int autoSave;

    public SaveProcessor (){
        File save = new File("saveData/save.txt");
        boolean exists = save.exists();
        if (exists) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(save);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            insCleared = scanner.nextInt();
            mainCleared = scanner.nextInt();
            freeCleared = scanner.nextInt();
            totalCleared = insCleared + mainCleared + freeCleared;
            autoSave = scanner.nextInt();
            scanner.close();
        } else {
            insCleared = 0;
            mainCleared = 0;
            freeCleared = 0;
            totalCleared = 0;
            autoSave = 0;
        }
    }

    public boolean autoSave (){
        if (autoSave == 1){
            return true;
        } else {
            return false;
        }
    }

    public int getInsCleared(){
        return insCleared;
    }

    public int getMainCleared(){
        return mainCleared;
    }

    public int getFreeCleared(){
        return freeCleared;
    }

    public int getTotalCleared(){
        return totalCleared;
    }

    public void insClear(){
        insCleared++;
        totalCleared++;
    }

    public void mainClear(){
        mainCleared++;
        totalCleared++;
    }

    public void freeClear(){
        freeCleared++;
        totalCleared++;
    }

    public void setAutoSave(int i){
        autoSave = i;
    }

    public void Save(){
        try {
            Writer w = new FileWriter("saveData/save.txt");
            w.write(insCleared + "\n");
            w.write(mainCleared + "\n");
            w.write(freeCleared + "\n");
            w.write(autoSave + "\n");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
