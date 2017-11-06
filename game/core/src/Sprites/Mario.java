package Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class Mario extends Sprite {
    public enum State{FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD};

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioJump;
    private TextureRegion marioDead;

    public State currentState;
    public State previousState;

    private Animation <TextureRegion> marioRun;
    private Animation <TextureRegion> bigMarioRun;
    private Animation <TextureRegion> growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineBigMario;
    private boolean marioIsDead;

    private TaskThree screen;

    public Mario(TaskThree screen){
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //little ` animation
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16 , 0,16,16));
        }
        //running animation
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        //bigmario animation
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16 , 0,16,32));
        }
        //running animation
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        //set animation for growing mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),15*16,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),15*16,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        growMario = new Animation(0.2f, frames);

        //create jump mario texture region
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"),80,0,16,16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);

        //create stand mario texture region
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);

        //create dead mario texture region
        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"),96,0,16,16);// add 16 pixels into X to get the dead animation

        //set mario in box2d
        defineMario();

        setBounds(0,0,16/DefaultValues.PPM,16/DefaultValues.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        if(marioIsBig){
            setPosition(b2body.getPosition().x - getWidth() / 2 , b2body.getPosition().y - getHeight() / 2 - 6 /DefaultValues.PPM);
        }else{
            setPosition(b2body.getPosition().x - getWidth() / 2 , b2body.getPosition().y - getHeight() / 2);
        }

        setRegion(getFrame(dt));
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToRedefineBigMario){
            redefineMario();
        }
    }

    public void grow(){
        if(!isBig()){
            runGrowAnimation = true;
            marioIsBig = true;
            timeToDefineBigMario = true;
            setBounds(getX(),getY(),getWidth(),getHeight() * 2);
            DungeonCoder.manager.get("Mario/sounds/powerup.wav", Sound.class).play();
        }
    }

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }


    public void hit(){
        if(marioIsBig){
            marioIsBig = false;
            timeToRedefineBigMario = true;
            setBounds(getX(),getY(),getWidth(),getHeight() / 2); //go back to original height
            DungeonCoder.manager.get("Mario/sounds/powerdown.wav", Sound.class).play();
        }else{
            DungeonCoder.manager.get("Mario/music/mario_music.ogg", Music.class).stop();
            DungeonCoder.manager.get("Mario/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            //no fixture can collide with mario
            filter.maskBits = DefaultValues.NOTHING_BIT;
            for(Fixture fixture : b2body.getFixtureList()){
                fixture.setFilterData(filter);
            }
            b2body.applyLinearImpulse(new Vector2(0,4f),b2body.getWorldCenter(),true);//apply impulse in Y direction
        }
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);//not true coz not loopable
                if(growMario.isAnimationFinished(stateTimer)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                //if mario is big then bigmario jump otherwise regular mario jump
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer,true) : marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }
        //if hes running left and region isnt facing left
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        //if currentstate is equal to our previous state, then statetimer + dt else it will equal 0
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public boolean isBig(){
        return marioIsBig;
    }

    public State getState(){
        if(marioIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;

    }


    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/DefaultValues.PPM,32/DefaultValues.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ DefaultValues.PPM);
        fdef.filter.categoryBits = DefaultValues.MARIO_BIT;
        //what can mario collide with
        fdef.filter.maskBits = DefaultValues.GROUND_BIT |
                DefaultValues.COIN_BIT |
                DefaultValues.BRICK_BIT |
                DefaultValues.ENEMY_BIT |
                DefaultValues.OBJECT_BIT|
                DefaultValues.ENEMY_HEAD_BIT|
                DefaultValues.ITEM_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //create sensor on mario head
        //edgeshape line between 2 diff points
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / DefaultValues.PPM, 7 /DefaultValues.PPM), new Vector2(2 / DefaultValues.PPM, 7 /DefaultValues.PPM));
        fdef.filter.categoryBits = DefaultValues.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);//this is the MARIO HEAD BIT
    }

    public void defineBigMario(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);//destroy old body and recreate a new body
        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0,10/DefaultValues.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ DefaultValues.PPM);
        fdef.filter.categoryBits = DefaultValues.MARIO_BIT;
        //what can mario collide with
        fdef.filter.maskBits = DefaultValues.GROUND_BIT |
                DefaultValues.COIN_BIT |
                DefaultValues.BRICK_BIT |
                DefaultValues.ENEMY_BIT |
                DefaultValues.OBJECT_BIT|
                DefaultValues.ENEMY_HEAD_BIT|
                DefaultValues.ITEM_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0,-14/DefaultValues.PPM));
        b2body.createFixture(fdef).setUserData(this);

        //create sensor on mario head
        //edgeshape line between 2 diff points
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / DefaultValues.PPM, 7 /DefaultValues.PPM), new Vector2(2 / DefaultValues.PPM, 7 /DefaultValues.PPM));
        fdef.filter.categoryBits = DefaultValues.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);//this is the MARIO HEAD BIT
        timeToDefineBigMario = false;
    }

    public void redefineMario(){
        Vector2 position = b2body.getPosition();//get the current position
        world.destroyBody(b2body);//destroy big mario

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);//create it at the current position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ DefaultValues.PPM);
        fdef.filter.categoryBits = DefaultValues.MARIO_BIT;
        //what can mario collide with
        fdef.filter.maskBits = DefaultValues.GROUND_BIT |
                DefaultValues.COIN_BIT |
                DefaultValues.BRICK_BIT |
                DefaultValues.ENEMY_BIT |
                DefaultValues.OBJECT_BIT|
                DefaultValues.ENEMY_HEAD_BIT|
                DefaultValues.ITEM_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //create sensor on mario head
        //edgeshape line between 2 diff points
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / DefaultValues.PPM, 7 /DefaultValues.PPM), new Vector2(2 / DefaultValues.PPM, 7 /DefaultValues.PPM));
        fdef.filter.categoryBits = DefaultValues.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);//this is the MARIO HEAD BIT
        timeToRedefineBigMario = false;
    }


}
