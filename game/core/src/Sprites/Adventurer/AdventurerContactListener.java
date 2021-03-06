package Sprites.Adventurer;

import Sprites.Enemy;
import Sprites.Adventurer.Adventurer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dungeoncoder.screens.TaskTwo;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class AdventurerContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        //Find out which is head
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef){
            case DefaultValues.SKELETON_BIT | DefaultValues.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == DefaultValues.SKELETON_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case DefaultValues.SKELETON_BIT | DefaultValues.SKELETON_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case DefaultValues.ADVENTURER_BIT | DefaultValues.SKELETON_BIT:
                if(fixA.getFilterData().categoryBits == DefaultValues.ADVENTURER_BIT){
                    ((Adventurer)fixA.getUserData()).getHit();
                }else{
                    ((Adventurer)fixB.getUserData()).getHit();
                }
                break;
            case DefaultValues.ADVENTURER_BIT | DefaultValues.END_BIT:
                if(fixA.getFilterData().categoryBits == DefaultValues.ADVENTURER_BIT){
                    ((Adventurer)fixA.getUserData()).gameCompleted();
                    Gdx.app.log("ADVENTURER", "GAME COMPLETED");
                }else{
                    ((Adventurer)fixB.getUserData()).gameCompleted();
                    Gdx.app.log("ADVENTURER", "GAME COMPLETED");
                }
                break;
            case DefaultValues.ADVENTURER_BIT | DefaultValues.NPC_BIT:
                if(fixA.getFilterData().categoryBits == DefaultValues.ADVENTURER_BIT){
                    ((Adventurer)fixA.getUserData()).questActivated();
                    DefaultValues.questActivated = true;
                    Gdx.app.log("NPC", "TRIGGERED QUEST 1");
                }else{
                    ((Adventurer)fixB.getUserData()).questActivated();
                    DefaultValues.questActivated = true;
                    Gdx.app.log("NPC", "TRIGGERED QUEST 1");
                }
                break;
            case DefaultValues.ADVENTURER_BIT | DefaultValues.NPC2_BIT:
                if(fixA.getFilterData().categoryBits == DefaultValues.ADVENTURER_BIT){
                    ((Adventurer)fixA.getUserData()).quest2Activated();
                    DefaultValues.quest2Activated = true;
                    Gdx.app.log("NPC", "TRIGGERED QUEST 2");
                }else{
                    ((Adventurer)fixB.getUserData()).quest2Activated();
                    DefaultValues.quest2Activated = true;
                    Gdx.app.log("NPC", "TRIGGERED QUEST 2");
                }
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
