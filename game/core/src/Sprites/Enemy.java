package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.screens.TaskTwo;

public abstract class Enemy extends Sprite {
    protected World world;
    protected TaskThree screen;
    protected TaskTwo screen2;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(TaskThree screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
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
