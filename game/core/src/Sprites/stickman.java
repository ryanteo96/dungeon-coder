package Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.screens.TaskOne;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class stickman extends Sprite{
    public enum State {RUNNING, FALLING, STANDING, ATTACKING};
    public State currentState;
    public State prevState;
    public World world;
    public Body b2body;
    private TextureRegion stickmanStand;
    private Animation <TextureRegion> stickmanRun;
    private Animation <TextureRegion> stickmanJump;
    public boolean runningRight;
    private float stateTimer;

    public stickman(TaskOne screen){
        super(screen.getAtlas().findRegion("stickman"));
        this.world = world;
        stateTimer = 0;
        stickmanStand = new TextureRegion(getTexture(), 640, 0, 720, 100);
        setRegion(stickmanStand);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 8; i++){
            frames.add(new TextureRegion(getTexture(), i * 80 , 0,80,100));
        }
        //running animation
        stickmanRun = new Animation(0.1f, frames);
        frames.clear();

//        for(int i = 4; i < 6; i++){
        frames.add(new TextureRegion(getTexture(), 3 * 80 , 0,80,100));
//        }
        stickmanJump = new Animation(0.1f, frames);
        frames.clear();
        defineStickman();

        setBounds(0,0,16/DefaultValues.PPM,16/DefaultValues.PPM);
        setRegion(stickmanStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2 , b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case FALLING:
                region = stickmanJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = stickmanRun.getKeyFrame(stateTimer,true);
                break;
            case STANDING:
            default:
                region = stickmanStand;
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
        stateTimer = currentState == prevState ? stateTimer + dt : 0;
        prevState = currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && prevState == State.FALLING))
            return State.FALLING;
        else if(b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }

    public void defineStickman(){
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
                DefaultValues.ENEMY_HEAD_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef);

        //create sensor on mario head
        //edgeshape line between 2 diff points
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / DefaultValues.PPM, 7 /DefaultValues.PPM), new Vector2(2 / DefaultValues.PPM, 7 /DefaultValues.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }

}
