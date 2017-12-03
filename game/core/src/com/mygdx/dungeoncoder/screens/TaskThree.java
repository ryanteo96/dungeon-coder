package com.mygdx.dungeoncoder.screens;

import Scenes.Hud;
import Sprites.Enemy;
import Sprites.Goomba;
import Sprites.Mario;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.B2WorldCreator;
import com.mygdx.dungeoncoder.utils.GifRecorder;
import com.mygdx.dungeoncoder.utils.WorldContactListener;
import com.mygdx.dungeoncoder.values.DefaultValues;
import com.mygdx.dungeoncoder.values.Item;
import com.mygdx.dungeoncoder.values.ItemDef;
import com.mygdx.dungeoncoder.values.Mushroom;
import org.w3c.dom.css.Rect;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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
    private B2WorldCreator creator;

    //sprites
    private Mario player;

    //music
    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    public GifRecorder gifRecorder;

    public TaskThree(DungeonCoder g) {
        game = g;
        gifRecorder = new GifRecorder(game.batch);
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
        creator = new B2WorldCreator(this);

        //create mario in our world
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());
        //back button

        music = DungeonCoder.manager.get("Mario/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        createBack();
    }
    public boolean gameOver(){
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3){
            return true;
        }else{
            return false;
        }
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this,idef.position.x,idef.position.y));
            }
        }
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
        dispose();
        g.setScreen(new InstructionalMode(g));
        music.stop();
    }
    @Override
    public void show() {
        System.out.println("you are in stage three");
    }

    public void handleInput(float dt) {
        //if he's dead, dun get any input
        if(player.currentState != Mario.State.DEAD){
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
    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation 60 times per second
        world.step(1/60f, 6,2);

        player.update(dt);
        for(Enemy enemy : creator.getGoombas()){
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 220/DefaultValues.PPM){
                enemy.b2body.setActive(true);//activate goomba
            }
        }

        for(Item item : items){
            item.update(dt);
        }

        hud.update(dt);

        //if mario dies, freeze the camera right where he died
        if(player.currentState != Mario.State.DEAD){
            gamecam.position.x = player.b2body.getPosition().x;
        }

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
        for(Enemy enemy : creator.getGoombas()){
            enemy.draw(game.batch);
        }
        for(Item item : items){
            item.draw(game.batch);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    if(gameOver()){
        dispose();
        game.setScreen(new GameOverScreen(game));
    }
        stage.draw();
        stage.act(delta);
        gifRecorder.update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        gamePort.update(width,height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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
