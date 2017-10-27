package com.mygdx.dungeoncoder.screens;

import Scenes.Hud;
import Sprites.Mario;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.B2WorldCreator;
import com.mygdx.dungeoncoder.values.DefaultValues;
import org.w3c.dom.css.Rect;

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.DungeonCoder.V_HEIGHT;
import static com.mygdx.dungeoncoder.DungeonCoder.V_WIDTH;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskThree implements Screen {
    //reference to our game, use to set screens
    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private TextureAtlas atlas;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //sprites
    private Mario player;

    public TaskThree(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
               new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("Mario/Mario_and_Enemies.pack");

        //create cam to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(V_WIDTH/DefaultValues.PPM, V_HEIGHT/DefaultValues.PPM, gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/DefaultValues.PPM);

        //set camera at center at the start of the map
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        //pass the world and map to B2WorldCreator.java
        new B2WorldCreator(world,map);
        //create mario in our world
        player = new Mario(world, this);

        //back button
        createBack();
    }

    public TextureAtlas getAtlas(){
        return atlas;
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
        g.setScreen(new InstructionalMode(g));
    }
    @Override
    public void show() {
        System.out.println("you are in stage three");
    }

    public void handleInput(float dt) {
        //control player using immediate impulses, use world center so the torque wont make the character fly around
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { // for quick tap
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            player.currentState = Mario.State.JUMPING;
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.previousState == Mario.State.JUMPING){
                player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) { //isKeyPressed for holding down keys
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)  {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);

        //takes 1 step in the physics simulation 60 times per second
        world.step(1/60f, 6,2);


        player.update(dt);

        //attach our gamecam to our player's x coordinate
        gamecam.position.x = player.b2body.getPosition().x;

        //update gamecam with correct corrdinates after changes
        gamecam.update();

        //tell renderer to draw only what our camera can see in our game world
        renderer.setView(gamecam);
    }


    @Override
    public void render(float delta) {
        update(delta);

        //clear the game screen
        Gdx.gl.glClearColor(172/255f, 115/255f, 57/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render game map
        renderer.render();

        //render our Box2Ddebuglines
        b2dr.render(world,gamecam.combined);

        //set batch to draw what the Hud camera sees.
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        gamePort.update(width,height);
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
        map.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        stage.dispose();
        renderer.dispose();

    }


}
