package Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.screens.TaskTwo;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class Adventurer extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion adventurerStand;
    public enum State{FALLING, JUMPING, STANDING, RUNNING};
    public Adventurer.State currentState;
    public Adventurer.State previousState;
    private Animation <TextureRegion> adventurerRun;
    private Animation <TextureRegion> adventurerJump;
    private float stateTimer;
    private boolean runningRight;

    public Adventurer(TaskTwo screen){
        super(screen.getAtlas().findRegion("walk"));
        this.world = screen.getWorld();
        currentState = Adventurer.State.STANDING;
        previousState = Adventurer.State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(getTexture(), 1 , 50,64,45));
        frames.add(new TextureRegion(getTexture(), 27 , 50,64,44));
        frames.add(new TextureRegion(getTexture(), 50 , 50,64,45));
        frames.add(new TextureRegion(getTexture(), 66 , 50,64,44));
        frames.add(new TextureRegion(getTexture(), 84 , 50,64,43));
        frames.add(new TextureRegion(getTexture(), 110 , 50,64,44));
        frames.add(new TextureRegion(getTexture(), 129 , 50,64,45));
        frames.add(new TextureRegion(getTexture(), 148 , 50,64,44));

        //running animation
        adventurerRun = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 1; i < 2; i++){
            frames.add(new TextureRegion(getTexture(), 1 , 0,100,45));
        }
        adventurerJump = new Animation(0.1f, frames);
        frames.clear();

        defineAdventurer();

        adventurerStand = new TextureRegion(getTexture(),1,0,64,52);
        setBounds(0,0,20/DefaultValues.PPM,20/DefaultValues.PPM);
        setRegion(adventurerStand);

    }

    public void update(float dt){
        if(runningRight){
            setPosition(b2body.getPosition().x - getWidth() / 2 + 5/DefaultValues.PPM, b2body.getPosition().y - getHeight() / 2);
        }else{
            setPosition(b2body.getPosition().x - getWidth() / 2 - 5/DefaultValues.PPM, b2body.getPosition().y - getHeight() / 2);
        }

        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = adventurerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = adventurerRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = adventurerStand;
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

    public Adventurer.State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == Adventurer.State.JUMPING))
            return Adventurer.State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return Adventurer.State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return Adventurer.State.RUNNING;
        else
            return Adventurer.State.STANDING;

    }


    public void defineAdventurer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(10/DefaultValues.PPM,100/DefaultValues.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        //CircleShape shape = new CircleShape();
        shape.setAsBox(5/ DefaultValues.PPM,10/DefaultValues.PPM);
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
