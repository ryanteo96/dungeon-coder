package com.mygdx.dungeoncoder.values;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class DefaultValues {
    public static final int VIRTUAL_WIDTH = 1280;
    public static final int VIRTUAL_HEIGHT = 720;
    public static final float PPM = 100;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32; //for pipes and stuffs
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_HEAD_BIT = 512;

    //for adventurer
    public static final short ADVENTURER_BIT = 2;
    public static final short NPC_BIT = 4;
    public static final short END_BIT = 8;
    public static final short SKELETON_BIT = 64;

    public static boolean WALK_RIGHT = false;
    public static boolean WALK_LEFT = false;
    public static boolean JUMP = false;
    public static boolean gameComplete = false;
    public static boolean questActivated = false;
}
