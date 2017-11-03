package com.mygdx.dungeoncoder.utils;

import Sprites.Brick;
import Sprites.Coin;
import Sprites.Goomba;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.screens.TaskTwo;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class B2WorldCreator {
    public Array<Goomba> getGoombas() {
        return goombas;
    }

    private Array<Goomba> goombas;
    public B2WorldCreator(TaskThree screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
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
            fdef.filter.categoryBits = DefaultValues.OBJECT_BIT; //when enemy collide pipe, it will turn around
            body.createFixture(fdef);
        }

        //create brick bodies/fixtures
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen ,object);
        }

        //create coin bodies/fixtures
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){ // look into the Tiled
            new Coin(screen, object);
        }
        //create all goombas
        goombas = new Array<Goomba>();
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){ // look into the Tiled
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX()/DefaultValues.PPM, rect.getY() /DefaultValues.PPM));
        }
    }

    public B2WorldCreator(TaskTwo screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / DefaultValues.PPM, (rect.getY() + rect.getHeight() / 2) / DefaultValues.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / DefaultValues.PPM, (rect.getHeight() / 2) / DefaultValues.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        /*
        //create pipe bodies/fixtures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / DefaultValues.PPM, (rect.getY() + rect.getHeight() / 2) / DefaultValues.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / DefaultValues.PPM, (rect.getHeight() / 2) / DefaultValues.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = DefaultValues.OBJECT_BIT; //when enemy collide pipe, it will turn around
            body.createFixture(fdef);
        }
        */
    }

}
