package com.mygdx.dungeoncoder.utils;

import Sprites.Brick;
import Sprites.Coin;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map){
//create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ DefaultValues.PPM , (rect.getY() + rect.getHeight()/2)/DefaultValues.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth()/2)/DefaultValues.PPM,(rect.getHeight()/2)/DefaultValues.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create pipe bodies/fixtures
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/DefaultValues.PPM , (rect.getY() + rect.getHeight()/2)/DefaultValues.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth()/2)/DefaultValues.PPM,(rect.getHeight()/2)/DefaultValues.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create brick bodies/fixtures
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
           new Brick(world, map ,rect);
        }

        //create coin bodies/fixtures
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){ // look into the Tiled
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Coin(world, map, rect);
        }
    }

}
