package com.mygdx.dungeoncoder.utils;

import Sprites.Enemy;
import Sprites.InteractiveTileObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
      //Find out which is head
      Fixture fixA = contact.getFixtureA();
      Fixture fixB = contact.getFixtureB();

      int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

      if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
          Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
          Fixture object = head == fixA ? fixB : fixA;
          if(object.getUserData()instanceof InteractiveTileObject){
              ((InteractiveTileObject) object.getUserData()).onHeadHit();
          }
      }

      switch(cDef){
          //if Mario collides with the enemy head bit, then execute
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
          case DefaultValues.MARIO_BIT | DefaultValues.ENEMY_BIT:
              Gdx.app.log("MARIO", "DIED");
              break;
          case DefaultValues.ENEMY_BIT | DefaultValues.ENEMY_BIT:
              ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
              ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
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
