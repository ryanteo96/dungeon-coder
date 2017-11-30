package com.mygdx.dungeoncoder.screens;

import java.io.*;
import Scenes.AdventurerHud;
import Sprites.Adventurer.Adventurer;
import Sprites.Adventurer.AdventurerContactListener;
import Sprites.Enemy;
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
import com.sun.org.apache.xpath.internal.SourceTree;

import static com.mygdx.dungeoncoder.DungeonCoder.V_HEIGHT;
import static com.mygdx.dungeoncoder.DungeonCoder.V_WIDTH;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskTwo implements Screen {
    //Write files
    CodeEvaluator codeevaluator;
    BufferedWriter bw = null;
    FileWriter fw = null;
    private TextButton runButton;

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
    private static Adventurer player;

    //textArea
    private TextArea textArea;
    private Skin skin;

    //buttons
    private TextButton codeButton;

    //boolean
    private boolean codeOn;


    public TaskTwo(DungeonCoder g) throws FileNotFoundException {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        codeOn = false;

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
        hud = new AdventurerHud(game.batch, this);

        world.setContactListener(new AdventurerContactListener());

        //back button
        createBack();
        createTextArea();
    }

    public DungeonCoder getGame(){
        return game;
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
        String fileName = "StageTwo.java";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            //FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            //BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/LCLY/Desktop/Dungeon/dungeon-coder/game/core/src/com/mygdx/dungeoncoder/screens/StageTwo.java"));
            BufferedReader bufferedReader = new BufferedReader(new FileReader("StageTwo.java"));

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append("\n");
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file when screen is loading '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file when screen is loading '"  + fileName + "'");
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

        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        codeButton = new TextButton("Code Here", skin);
        codeButton.setPosition(50, 10);
        codeButton.setSize(130, 50);
        stage.addActor(codeButton);

        final int percentage = Integer.parseInt(hud.getProgressInfo()); // to update the database

        skin = new Skin (Gdx.files.internal("UIElements/test.json"));
        runButton = new TextButton("Run", skin);
        runButton.setPosition(460, 10);
        runButton.setSize(100, 50);
        runButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String code = textArea.getText();
                //System.out.println("textarea:" + code);
                File file = new File("StageTwo.java");
                String path = file.getAbsolutePath();
                System.out.println("The file path of test file is "+path);

                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(code);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println("Code saved!");
                String fileName = "StageTwo.java";
                String className = "StageTwo.class";
                String runName = className.substring(0, className.length() - 6);
                File classFile = new File(className);
                String classPath = classFile.getAbsolutePath();
                classPath = classPath.substring(0, classPath.length() - (className.length()));
                System.out.println("classPath is: "+classPath);

                // This will reference one line at a time
                String line = "";

                try {
                    String filepath = file.getAbsolutePath();
                    System.out.println(filepath);
                    shareVariable.connect.requestUpdateProgress(file,"Task1",10);
                    if(codeevaluator.evaluate(filepath) == true){
                        System.out.println("it gets in the if statement");
                        codeevaluator.run(classPath, runName);
                    }
                    System.out.println(codeevaluator.evaluate(filepath));
                    FileReader fileReader = new FileReader("code.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while((line = bufferedReader.readLine()) != null) {
                        System.out.println("line is: " + line);

                        long start = System.currentTimeMillis();
                        long end = start;
                        while(end - start < 2000) {
                            end = System.currentTimeMillis();
                            System.out.println("here");
                            render(Gdx.graphics.getDeltaTime());
                        }

                        if(line.equals("right")){
                            movedRight();
                            //render(Gdx.graphics.getDeltaTime());
                            //update(Gdx.graphics.getDeltaTime());
                        }
                        if(line.equals("left")){
                            movedLeft();
                            //render(Gdx.graphics.getDeltaTime());
                        }
                        if(line.equals("up")){
                            DefaultValues.JUMP = true;
                        }
                        //testing code function
                    }
                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println("Unable to open file when run is being clicked'" + fileName + "'");
                }
                catch(IOException ex) {
                    System.out.println("Error reading file when run is being clicked'"  + fileName + "'");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                //shareVariable.connect.requestUpdateProgress(file,"Task1",percentage);

            }
        });
        codeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if(codeOn == false){
                    stage.addActor(textArea);
                    stage.addActor(runButton);
                    codeOn = true;
                }else{
                    textArea.remove();
                    runButton.remove();
                    codeOn = false;
                }

            }
        });

        //to find the path of the file
        //System.out.println("File path: " + new File("test.txt").getAbsolutePath());

    }

    public void movedRight(){
        DefaultValues.WALK_RIGHT = true;
        if (DefaultValues.WALK_RIGHT && player.b2body.getLinearVelocity().x <= 2 ) { //isKeyPressed for holding down keys
            player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
            DefaultValues.WALK_RIGHT = false;
            System.out.println("RIGHT METHOD CALLED");
            System.out.println("Your character moved right!");
        }
    }

    public void movedLeft(){
        DefaultValues.WALK_LEFT = true;
        if (DefaultValues.WALK_LEFT && player.b2body.getLinearVelocity().x >= -2)  {
            player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);
            DefaultValues.WALK_LEFT = false;
            System.out.println("LEFT METHOD CALLED");
            System.out.println("Your character moved left!");
        }

    }

    public void jump(){
        DefaultValues.JUMP = true;
        if (DefaultValues.JUMP) { // for quick tap
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            player.currentState = Adventurer.State.JUMPING;
            if(DefaultValues.JUMP && player.previousState == Adventurer.State.JUMPING){
                player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
            }
            DefaultValues.JUMP = false;
            System.out.println("Your character jumped!");
        }

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
        hud.stopMusic();
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

        /*if (DefaultValues.JUMP) { // for quick tap
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
        //System.out.println("update");
        handleinput(dt);
        //movedRight(dt);
        //takes 1 step in the physics simulation 60 times per second
        world.step(1/60f, 6,2);
        player.update(dt);

        //if character dies, freeze the camera right where he died
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
        //System.out.println("render");

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
