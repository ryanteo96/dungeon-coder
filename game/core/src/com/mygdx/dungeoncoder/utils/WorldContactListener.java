package com.mygdx.dungeoncoder.utils;

import Sprites.Enemy;
import Sprites.InteractiveTileObject;
import Sprites.Mario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dungeoncoder.values.DefaultValues;
import com.mygdx.dungeoncoder.values.Item;

public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
      //Find out which is head
      Fixture fixA = contact.getFixtureA();
      Fixture fixB = contact.getFixtureB();

      int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

      switch(cDef){
          //if Mario collides with the enemy head bit, then execute
          case DefaultValues.MARIO_HEAD_BIT | DefaultValues.BRICK_BIT:
          case DefaultValues.MARIO_HEAD_BIT | DefaultValues.COIN_BIT:
              if(fixA.getFilterData().categoryBits == DefaultValues.MARIO_HEAD_BIT){
                  ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario)fixA.getUserData());
              }
              else{
                  ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario)fixB.getUserData());
              }
              break;
          case DefaultValues.ENEMY_HEAD_BIT | DefaultValues.MARIO_BIT:
              if(fixA.getFilterData().categoryBits == DefaultValues.ENEMY_HEAD_BIT)
                  ((Enemy)fixA.getUserData()).hitOnHead();
              else
                  ((Enemy)fixB.getUserData()).hitOnHead();
              break;
          case DefaultValues.ENEMY_BIT | DefaultValues.OBJECT_BIT:
              if(fixA.getFilterData().categoryBits == DefaultValues.ENEMY_BIT)
                  ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
              else
                  ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
              break;
          case DefaultValues.ENEMY_BIT | DefaultValues.ENEMY_BIT:
              ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
              ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
              break;
          case DefaultValues.ITEM_BIT | DefaultValues.OBJECT_BIT:
              if(fixA.getFilterData().categoryBits == DefaultValues.ITEM_BIT)
                  ((Item)fixA.getUserData()).reverseVelocity(true,false);
              else
                  ((Item)fixB.getUserData()).reverseVelocity(true,false);
              break;
          case DefaultValues.ITEM_BIT | DefaultValues.MARIO_BIT:
              //if fixture A is item then we use that item on mario which  must the fixture B vice versa
              if(fixA.getFilterData().categoryBits == DefaultValues.ITEM_BIT)
                  ((Item)fixA.getUserData()).use((Mario)fixB.getUserData());
              else
                  ((Item)fixB.getUserData()).use((Mario)fixA.getUserData());
              break;
          case DefaultValues.MARIO_BIT | DefaultValues.ENEMY_BIT:
              Gdx.app.log("MARIO", "DIED");
              break;
      }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
