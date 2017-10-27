package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.backgroundElements.Boxes;
import com.mygdx.dungeoncoder.backgroundElements.Land;

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskTwo extends ApplicationAdapter implements Screen, ContactListener {
    private DungeonCoder game;
    private Stage stage;
    Skin backButtonSkin;
    SpriteBatch batch;
    Texture bg;
    World world;
    Player player;
    float x;
    float y;
    Boxes box;
    Land land;
    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    Sound sound;
    Music song;

    public TaskTwo(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false,VIRTUAL_WIDTH/shareVariable.PPM,VIRTUAL_HEIGHT/shareVariable.PPM);
        box2DCamera.position.set(VIRTUAL_WIDTH/2f,VIRTUAL_HEIGHT/2f,0);
        debugRenderer = new Box2DDebugRenderer();
        x = Gdx.graphics.getWidth();
        y = Gdx.graphics.getHeight();
        world = new World(new Vector2(0,-9.8f),true);
        world.setContactListener(this);
        player = new Player(world,"stationaryninja.png",VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2 + 250);
        box = new Boxes(world);
        land = new Land(world);
        sound = Gdx.audio.newSound(Gdx.files.internal("footstep1.ogg"));
        song =  Gdx.audio.newMusic(Gdx.files.internal("spongebob.mp3"));
        createGame();
        createBack();
    }

    public void createGame(){
        song.play();
        batch = new SpriteBatch();
        bg = new Texture("gamebackground.png");
    }

    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                backToInstructionalMode(game);
            }
        });
        stage.addActor(btnBack);
    }


    private void backToInstructionalMode(DungeonCoder g) {
        song.stop();
        g.setScreen(new InstructionalMode(g));
    }
    @Override
    public void show() {
        System.out.println("you are in stage two");
    }

    void update(float dt){
       // long id = sound.play();
        //sound.setVolume(id, 1.0f);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            //player.getBody().applyLinearImpulse(new Vector2(-0.05f,0),player.getBody().getWorldCenter(),true);
            player.getBody().applyForce(new Vector2(-5f,0),player.getBody().getWorldCenter(),true);
            //sound.play();
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            //player.getBody().applyLinearImpulse(new Vector2(0.05f,0),player.getBody().getWorldCenter(),true);
            //first parameter, apply force, second parameter, apply the force from where, last param, wake the body
            player.getBody().applyForce(new Vector2(5f,0),player.getBody().getWorldCenter(),true);

        }else if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            player.getBody().applyForce(new Vector2(0,10f),player.getBody().getWorldCenter(),true);
        }

    }
    @Override
    public void render(float delta) {
        update(delta);
        player.updatePlayer();
        Gdx.gl.glClearColor(172/255f, 115/255f, 57/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(bg,0,0,x,y);
        batch.draw(player,player.getX() + player.getWidth() - 5,player.getY() - 45,50,100);
        batch.draw(box,box.getX() - 70,box.getY()-90,230,200);
        batch.end();
        debugRenderer.render(world,box2DCamera.combined);
        world.step(Gdx.graphics.getDeltaTime(),6,2);
        stage.act(delta);
        stage.draw();
    }



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.getTexture().dispose();
        stage.dispose();
        batch.dispose();
        sound.dispose();
        song.dispose();
    }

    private void createStageTwo(){
        Texture stage_Two = new Texture(Gdx.files.internal("UIElements/inprogress.png"));
        TextureRegion stage_TwoRegion = new TextureRegion(stage_Two);
        TextureRegionDrawable stage_TwoDrawable = new TextureRegionDrawable(stage_TwoRegion);
        Image stage_TwoImage = new Image(stage_TwoDrawable);
        stage_TwoImage.setSize(600,100);
        stage_TwoImage.setPosition(380,400);
        stage.addActor(stage_TwoImage);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture firstBody, secondBody;

        if(contact.getFixtureA().getUserData() == "Player"){
            firstBody = contact.getFixtureA(); //make sure that first body is always fixture A
            secondBody = contact.getFixtureB();
            //System.out.println("Fixture A is the Player");
        }else{
            //System.out.println("Fixture A is the Box");
            firstBody = contact.getFixtureB();
            secondBody = contact.getFixtureA();
        }
        System.out.println("The first body is " + firstBody.getUserData());
        System.out.println("The second body is " + secondBody.getUserData());
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
