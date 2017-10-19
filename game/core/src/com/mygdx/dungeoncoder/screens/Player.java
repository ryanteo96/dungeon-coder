package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {
    private World world; //actual physics world for player
    private Body body; //actual body of the player, manipulate body to move the player

    public Player(String name, float x, float y){
        super(new Texture(name));
        setPosition(x-getWidth()/2,y-getHeight()/2);


    }
}
