package com.mygdx.dungeoncoder.screens;

import java.io.*;

import Scenes.AdventurerHud;
import Sprites.Adventurer.Adventurer;
import Sprites.Adventurer.AdventurerContactListener;
import Sprites.Enemy;
import Sprites.Mario;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.*;
import com.mygdx.dungeoncoder.values.DefaultValues;

import static com.mygdx.dungeoncoder.DungeonCoder.V_HEIGHT;
import static com.mygdx.dungeoncoder.DungeonCoder.V_WIDTH;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskTwo implements Screen {
    //Write files
    CodeEvaluator codeevaluator;
    BufferedWriter bw = null;
    FileWriter fw = null;
    private TextButton saveButton;

    private DungeonCoder game;
    private Stage stage;
    Skin backButtonSkin;
    private TextureAtlas atlas;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private AdventurerHud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Adventurer player;

    //textArea
    private TextArea textArea;
    private Skin skin;

    //music
    private Music music;


    public TaskTwo(DungeonCoder g) throws FileNotFoundException {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("Dungeon/Adventurer.pack");

        //create cam to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(V_WIDTH/ DefaultValues.PPM, V_HEIGHT/DefaultValues.PPM, gamecam);

        //get compiling
        codeevaluator = new CodeEvaluator();

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Dungeon/test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/DefaultValues.PPM);

        //set camera at center at the start of the map
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        //pass the world and map to B2WorldCreator.java
        creator = new B2WorldCreator(this);

        //create adventurer in our world
        player = new Adventurer(this);

        //create our game HUD for scores/timers/level info
        hud = new AdventurerHud(game.batch);

        world.setContactListener(new AdventurerContactListener());

        music = DungeonCoder.manager.get("UIElements/Animation/backgroundmusic.mp3", Music.class);
        music.setLooping(true);
        music.play();
        //back button
        createBack();
        createTextArea();
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

    private void createTextArea() throws FileNotFoundException {
        //to build string into the text file
        StringBuilder sb = new StringBuilder();
        // The name of the file to open.
        String fileName = "TaskTwo.java";
        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);


            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
                sb.append("\n");
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"  + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        String textFileString = sb.toString();
        textArea = new TextArea(textFileString,skin);
        textArea.setX(50);
        textArea.setY(70);
        textArea.setWidth(500);
        textArea.setHeight(500);
        stage.addActor(textArea);

        skin = new Skin (Gdx.files.internal("UIElements/test.json"));
        saveButton = new TextButton("Run", skin);
        saveButton.setPosition(460, 10);
        saveButton.setSize(100, 50);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String code = textArea.getText();
                File file = new File("TaskTwo.java");
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(code);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Code saved!");
                String fileName = "TaskTwo.java";
                // This will reference one line at a time
                String line = null;

                try {
                    shareVariable.connect.requestUpdateProgress(file,"Task1",10);
                    System.out.println("File path: " + new File("TaskTwo.java").getAbsolutePath());
                    String filepath = new File("TaskTwo.java").getAbsolutePath();
                    System.out.println(codeevaluator.evaluate(filepath));
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while((line = bufferedReader.readLine()) != null) {
                        //System.out.println(line);
                        //testing code function
                       if(code.contains("moveRight();")){
                           movedRight();
                           DefaultValues.WALK_RIGHT = true;
                       }
                       if(code.contains("moveLeft();")){
                           DefaultValues.WALK_LEFT = true;
                       }
                       if(code.contains("jump();")){
                           DefaultValues.JUMP = true;
                       }

                    }
                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + fileName + "'");
                }
                catch(IOException ex) {
                    System.out.println("Error reading file '"  + fileName + "'");
                }


            }
        });


        //to find the path of the file
        //System.out.println("File path: " + new File("test.txt").getAbsolutePath());
        stage.addActor(saveButton);
    }

    public void movedRight(){

    }

    public boolean gameOver(){
        if(player.currentState == Adventurer.State.DEAD && player.getStateTimer() > 3){
            return true;
        }else{
            return false;
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    private void backToInstructionalMode(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
        music.stop();
    }
    @Override
    public void show() {
        System.out.println("you are in stage two");
    }

    public void handleinput(float dt){
        //control player using immediate impulses, use world center so the torque wont make the character fly around
        if(player.currentState != Adventurer.State.DEAD){
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { // for quick tap
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
                player.currentState = Adventurer.State.JUMPING;
                if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.previousState == Adventurer.State.JUMPING){
                    player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) { //isKeyPressed for holding down keys
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                DungeonCoder.manager.get("UIElements/Animation/footstep.wav", Music.class).play();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)  {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                DungeonCoder.manager.get("UIElements/Animation/footstep.wav", Music.class).play();
            }
        }



       /* if (DefaultValues.JUMP) { // for quick tap
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            player.currentState = Adventurer.State.JUMPING;
            if(DefaultValues.JUMP && player.previousState == Adventurer.State.JUMPING){
                player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
            }
            DefaultValues.JUMP = false;
            System.out.println("Your character jumped!");
        }

        if (DefaultValues.WALK_RIGHT && player.b2body.getLinearVelocity().x <= 2 ) { //isKeyPressed for holding down keys
            player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
            DefaultValues.WALK_RIGHT = false;
            System.out.println("Your character moved right!");
        }

        if (DefaultValues.WALK_LEFT && player.b2body.getLinearVelocity().x >= -2)  {
            player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);
            DefaultValues.WALK_LEFT = false;
            System.out.println("Your character moved left!");
        }*/
    }

    public void update(float dt){
        handleinput(dt);
        //takes 1 step in the physics simulation 60 times per second
        world.step(1/60f, 6,2);
        player.update(dt);

        //if mario dies, freeze the camera right where he died
        if(player.currentState != Adventurer.State.DEAD){
            gamecam.position.x = player.b2body.getPosition().x;
        }

        hud.update(dt);

        for(Enemy enemy : creator.getDungeonMonster()){
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 220/DefaultValues.PPM){
                enemy.b2body.setActive(true);//activate goomba
            }
        }

        //update gamecam with correct corrdinates after changes
        gamecam.update();

        //tell renderer to draw only what our camera can see in our game world
        renderer.setView(gamecam);

    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void render(float delta) {
        update(delta);
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
        for(Enemy enemy : creator.getDungeonMonster()){
            enemy.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            game.setScreen(new AdventurerGameOver(game));
            dispose();
        }

        stage.act(delta);
        stage.draw();
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
        world.dispose();
        map.dispose();
        hud.dispose();
        stage.dispose();
        b2dr.dispose();
        renderer.dispose();

    }

}
