package Sprites.Adventurer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.screens.TaskTwo;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class Adventurer extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion adventurerStand;

    public enum State{FALLING, JUMPING, STANDING, RUNNING, DEAD};
    public Adventurer.State currentState;
    public Adventurer.State previousState;
    private Animation <TextureRegion> adventurerRun;
    private Animation <TextureRegion> adventurerJump;
    private Animation <TextureRegion> adventurerDead;
    private float stateTimer;
    private boolean runningRight;
    private boolean adventurerIsDead;

    public Adventurer(TaskTwo screen){
        super(screen.getAtlas().findRegion("walk"));
        this.world = screen.getWorld();
        currentState = Adventurer.State.STANDING;
        previousState = Adventurer.State.STANDING;
        stateTimer = 0;
        runningRight = true;
        adventurerIsDead = false;
        //walk/run
        Array<TextureRegion> frames = new Array<TextureRegion>();
        Texture standTexture = new Texture(Gdx.files.internal("UIElements/Animation/stickstand.png"));
        Texture jumpTexture = new Texture(Gdx.files.internal("UIElements/Animation/stickjump.png"));

        Texture walkTexture1 = new Texture(Gdx.files.internal("UIElements/Animation/walk1.png"));
        Texture walkTexture2 = new Texture(Gdx.files.internal("UIElements/Animation/walk2.png"));
        Texture walkTexture3 = new Texture(Gdx.files.internal("UIElements/Animation/walk3.png"));
        Texture walkTexture4 = new Texture(Gdx.files.internal("UIElements/Animation/walk4.png"));
        Texture walkTexture5 = new Texture(Gdx.files.internal("UIElements/Animation/walk5.png"));
        Texture walkTexture6 = new Texture(Gdx.files.internal("UIElements/Animation/walk6.png"));

        Texture deadTexture1 = new Texture(Gdx.files.internal("UIElements/Animation/stickdie1.png"));
        Texture deadTexture2 = new Texture(Gdx.files.internal("UIElements/Animation/stickdie2.png"));
        Texture deadTexture3 = new Texture(Gdx.files.internal("UIElements/Animation/stickdie3.png"));


        frames.add(new TextureRegion(walkTexture1,0,0,256,256));
        frames.add(new TextureRegion(walkTexture2,0,0,256,256));
        frames.add(new TextureRegion(walkTexture3,0,0,256,256));
        frames.add(new TextureRegion(walkTexture4,0,0,256,256));
        frames.add(new TextureRegion(walkTexture5,0,0,256,256));
        frames.add(new TextureRegion(walkTexture6,0,0,256,256));

        //running animation
        adventurerRun = new Animation(0.1f, frames);
        frames.clear();


        frames.add(new TextureRegion(jumpTexture, 1 , 0,256,256));

        adventurerJump = new Animation(0.1f, frames);
        frames.clear();

        defineAdventurer();
        adventurerStand = new TextureRegion(standTexture,0,0,256,256);


        frames.add(new TextureRegion(deadTexture1,0,0,256,256));
        frames.add(new TextureRegion(deadTexture2,0,0,256,256));
        frames.add(new TextureRegion(deadTexture3,0,0,256,256));
        adventurerDead = new Animation(0.1f,frames);

        frames.clear();
        setBounds(0,0,30/DefaultValues.PPM,30/DefaultValues.PPM);
        setRegion(adventurerStand);

    }
    public boolean isDead(){
        return adventurerIsDead;
    }

    public void update(float dt){
        if(runningRight){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }else{
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }

        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = adventurerDead.getKeyFrame(stateTimer);
                break;
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
        if(adventurerIsDead) {
            return State.DEAD;
        }else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == Adventurer.State.JUMPING))
            return Adventurer.State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return Adventurer.State.FALLING;
        else if(b2body.getLinearVelocity().x != 0){
            return Adventurer.State.RUNNING;
        }
        else
            return Adventurer.State.STANDING;

    }

    public void getHit(){
        adventurerIsDead = true;
        DungeonCoder.manager.get("UIElements/Animation/pain.mp3", Sound.class).play();
        DungeonCoder.manager.get("UIElements/Animation/backgroundmusic.mp3", Music.class).stop();
    }

    public void gameCompleted(){
        DefaultValues.gameComplete = true;
    }

    public void questActivated(){
        DefaultValues.questActivated = true;
    }

    public float getStateTimer(){
        return stateTimer;
    }


    public void defineAdventurer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(15 / DefaultValues.PPM, 100 / DefaultValues.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        //CircleShape shape = new CircleShape();
        shape.setAsBox(5 / DefaultValues.PPM, 10 / DefaultValues.PPM);
        fdef.filter.categoryBits = DefaultValues.ADVENTURER_BIT;
        //what can adventurer collide with
        fdef.filter.maskBits = DefaultValues.GROUND_BIT |
                            DefaultValues.SKELETON_BIT |
                            DefaultValues.OBJECT_BIT|
                            DefaultValues.END_BIT|
                            DefaultValues.NPC_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        }


}
