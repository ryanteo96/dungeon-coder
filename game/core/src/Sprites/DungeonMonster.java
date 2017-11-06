package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dungeoncoder.screens.TaskThree;
import com.mygdx.dungeoncoder.screens.TaskTwo;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class DungeonMonster extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private Boolean setToDestroy;
    private Boolean destroyed;


    public DungeonMonster(TaskTwo screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        Texture texture1 = new Texture(Gdx.files.internal("Dungeon/Enemies/1.png"));
        Texture texture2 = new Texture(Gdx.files.internal("Dungeon/Enemies/2.png"));
        Texture texture3 = new Texture(Gdx.files.internal("Dungeon/Enemies/3.png"));
        Texture texture4 = new Texture(Gdx.files.internal("Dungeon/Enemies/4.png"));
        frames.add(new TextureRegion(texture1,0,0,63,40));
        frames.add(new TextureRegion(texture2,0,0,63,40));
        frames.add(new TextureRegion(texture3,0,0,63,40));
        frames.add(new TextureRegion(texture4,0,0,63,40));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(),getY(),20/DefaultValues.PPM,20/DefaultValues.PPM);//how big is it
        setToDestroy = false;
        destroyed = false;
    }

    @Override
    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16)); //change image to destroyed goomba after getting destroyed
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
                DefaultValues.ENEMY_BIT |
                DefaultValues.OBJECT_BIT|
                DefaultValues.MARIO_BIT; // | is or

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hitOnHead() {

    }

}
