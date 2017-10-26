package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {
    private World world; //actual physics world for player
    private Body body; //actual body of the player, manipulate body to move the player

    public Player(World world, String name, float x, float y){
        super(new Texture(name));
        this.world = world;
        setPosition(x - getWidth()/2f ,y - getHeight()/2f);
        System.out.println(x);
        System.out.println(y);
        createBody();
    }

    void createBody(){
        BodyDef bodyDef = new BodyDef();
        //staticbody not affected by gravity or other forces
        //Kinematicbody not affected by gravity but other forces
        //Dynamic body is affected by gravity and other forces
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX()/shareVariable.PPM,getY()/shareVariable.PPM); //must use the same position as the player
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);//this will create the body in the world with the dynamic body definition

        PolygonShape shape = new PolygonShape(); //collider
        float width = getWidth() + 10;
        float height = getHeight() + 10;
        shape.setAsBox(width/shareVariable.PPM,height/shareVariable.PPM);

        FixtureDef fixtureDef = new FixtureDef(); //define the shape gravity mass of the body
        fixtureDef.shape = shape;
        fixtureDef.density = 1; //mass

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");

        shape.dispose();
    }

    //update the player position according to its body
    public void updatePlayer(){
        //when body moves, sprite will follow
        this.setPosition(body.getPosition().x * shareVariable.PPM, body.getPosition().y * shareVariable.PPM);
    }

    public Body getBody(){
        return this.body;
    }

}
