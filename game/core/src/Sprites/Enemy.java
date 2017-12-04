package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dungeoncoder.screens.*;

public abstract class Enemy extends Sprite {
    protected World world;
    protected MarioGame screen;
    protected TaskTwo screen2;
    protected TaskThree screen3;
    protected TaskOne screen1;
    protected FreeBattleModeGameScreen freeBattleScreen;
    protected MainStory1 mainStory;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(MarioGame screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);//stay sleep until someone wakes them up
    }

    public Enemy(TaskOne screen1, float x, float y){
        this.world = screen1.getWorld();
        this.screen1 = screen1;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);//stay sleep until someone wakes them up
    }

    public Enemy(TaskTwo screen2, float x, float y){
        this.world = screen2.getWorld();
        this.screen2 = screen2;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);//stay sleep until someone wakes them up
    }

    public Enemy(TaskThree screen3, float x, float y){
        this.world = screen3.getWorld();
        this.screen3 = screen3;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);//stay sleep until someone wakes them up
    }

    public Enemy(FreeBattleModeGameScreen freeBattleScreen, float x, float y){
        this.world = freeBattleScreen.getWorld();
        this.freeBattleScreen = freeBattleScreen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);//stay sleep until someone wakes them up
    }

    public Enemy(MainStory1 mainStory, float x, float y){
        this.world = mainStory.getWorld();
        this.mainStory = mainStory;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);//stay sleep until someone wakes them up
    }

    protected abstract void defineEnemy();


    public abstract void hitOnHead(); //so world contact can access
    public abstract void update(float dt);

    public void reverseVelocity(boolean x, boolean y){
        if(x){
            velocity.x = -velocity.x;
        }
        if(y){
            velocity.y = -velocity.y;
        }
    }
}
