package Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.screens.MarioGame;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private Boolean setToDestroy;
    private Boolean destroyed;

    public Goomba(MarioGame screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16,16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(),getY(),16/DefaultValues.PPM,16/DefaultValues.PPM);//how big is it
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16)); //change image to destroyed goomba after getting destroyed
            stateTime = 0;//know how long have goomba has been dead
        }else if(!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2,b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ DefaultValues.PPM);
        fdef.filter.categoryBits = DefaultValues.ENEMY_BIT;
        //what can mario collide with
        fdef.filter.maskBits = DefaultValues.GROUND_BIT |
                DefaultValues.COIN_BIT |
                DefaultValues.BRICK_BIT |
                DefaultValues.ENEMY_BIT |
                DefaultValues.OBJECT_BIT|
                DefaultValues.MARIO_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //create the Head of goomba
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5,8).scl(1/DefaultValues.PPM);
        vertices[1] = new Vector2(5,8).scl(1/DefaultValues.PPM);
        vertices[2] = new Vector2(-3,3).scl(1/DefaultValues.PPM);
        vertices[3] = new Vector2(3,3).scl(1/DefaultValues.PPM);
        head.set(vertices);

        fdef.shape = head;
        //add bounce when mario jump on the head
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = DefaultValues.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }


    public void draw(Batch batch){
        if(!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        //what happends when goomba get hit on the head, remove the box2d body after getting hit
        setToDestroy = true;
        DungeonCoder.manager.get("Mario/sounds/stomp.wav", Sound.class).play();
    }
}
