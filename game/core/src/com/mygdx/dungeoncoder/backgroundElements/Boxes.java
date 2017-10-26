package com.mygdx.dungeoncoder.backgroundElements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dungeoncoder.screens.shareVariable;
import com.mygdx.dungeoncoder.values.DefaultValues;

import java.awt.image.VolatileImage;

public class Boxes extends Sprite {
    private World world;
    private Body body;

    public Boxes(World world){
        super(new Texture("UIElements/crate.png"));
        this.world = world;
        setPosition(DefaultValues.VIRTUAL_WIDTH/2f, DefaultValues.VIRTUAL_HEIGHT/2f - 130);
        createBody();
    }

    void createBody(){
        BodyDef bodyDef = new BodyDef();
        //staticbody not affected by gravity or other forces
        //Kinematicbody not affected by gravity but other forces
        //Dynamic body is affected by gravity and other forces
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(getX()/ shareVariable.PPM,getY()/shareVariable.PPM); //must use the same position as the player
        body = world.createBody(bodyDef);//this will create the body in the world with the dynamic body definition

        PolygonShape shape = new PolygonShape(); //collider
        shape.setAsBox(100/shareVariable.PPM,100/shareVariable.PPM);

        FixtureDef fixtureDef = new FixtureDef(); //define the shape gravity mass of the body
        fixtureDef.shape = shape;
        fixtureDef.density = 1; //mass

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Box");
        fixture.setSensor(false); //example is when collecting coins or hit enemies
        shape.dispose();
    }


}
