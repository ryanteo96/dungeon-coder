package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.ClientConnection;
import com.mygdx.dungeoncoder.values.DefaultValues;
import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.dungeoncoder.utils.SaveProcessor;
import javax.swing.Timer;

import javax.xml.soap.Text;
import java.io.File;
import java.io.IOException;

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskOne extends ApplicationAdapter implements Screen {
    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private Skin skin;
    private TextField attemptText;
    private String attempt;
    private TextField progressText;
    private String progress;
    private TextField moduleText;
    private String module;
    private TextArea textArea;
    private TextButton quitButton;
    private TextButton continueButton;
    private TextButton hintButton;
    private boolean paused = false;
    private Label fpslabel;
    private Image pauseImage;
    private Player player;
    private TextureAtlas walkingAtlas, ninjaAtlas;
    private float timePassed = 0;
    private Animation<TextureRegion> walkAnimation, ninjaAnimation;
    private SpriteBatch walkingBatch, ninjaBatch, backgroundBatch;
    private Texture bg;
    private Window window;
    private World world;
    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    Texture pic1;
    Sprite test;
    public SaveProcessor s;
    public Image popupImage;

    private TextureAtlas stickman;

    public TaskOne(DungeonCoder g){
        s = new SaveProcessor();
        Texture popup = new Texture(Gdx.files.internal("UIElements/Accomplished.png"));
        TextureRegion popupRegion = new TextureRegion(popup);
        TextureRegionDrawable popupDrawable = new TextureRegionDrawable(popupRegion);
        popupImage = new Image(popupDrawable);
        popupImage.setPosition(0, 0);
        SaveProcessor s = new SaveProcessor();
        pic1 = new Texture("stationaryninja.png");
        game = g;
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false,VIRTUAL_WIDTH/shareVariable.PPM,VIRTUAL_HEIGHT/shareVariable.PPM);
        box2DCamera.position.set(0,0,0);

        stickman = new TextureAtlas("chara/stickman.atlas");

        debugRenderer = new Box2DDebugRenderer();

        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);
        //set the gravity, true to let the body inside to sleep so its
        // more efficient, only calculate when the body move so wont stress the processor
        //world = new World(new Vector2(0,-9.8f),true);
        //player = new Player(world,"stationaryninja.png",VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2);
        //player.setSize(70,120);
        createBack();
        popup();
        //createAttempts();
        //createProgress();
        //createTextArea();
        //createHint();
        //createTaskOneTextImage();
        //createDeadline();
        createGame();
        createPause();
        createTest();
    }

   private void createDeadline(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        Label deadlineText = new Label("Deadline: "+
                shareVariable.connect.requestTaskInformation("Task1","Deadline"),skin); //display deadline from the database
       deadlineText.setFontScale(1f,1f);
       deadlineText.setPosition(50, 555);
        stage.addActor(deadlineText);
   }

   public TextureAtlas getAtlas (){
       return stickman;
   }

   private void createGame(){
       skin = new Skin(Gdx.files.internal("UIElements/test.json"));
       fpslabel = new Label("fps: ", skin);
       walkingBatch = new SpriteBatch();
       walkingAtlas = new TextureAtlas(Gdx.files.internal("walking.atlas"));
       walkAnimation = new Animation<TextureRegion>(1/5f,walkingAtlas.getRegions()); //getRegions get all the location for you
       ninjaBatch = new SpriteBatch();
       ninjaAtlas = new TextureAtlas(Gdx.files.internal("ninja.atlas"));
       ninjaAnimation = new Animation<TextureRegion>(1/5f,ninjaAtlas.getRegions());
       skin = new Skin(Gdx.files.internal("UIElements/test.json"));
       window = new Window("", skin);
       window.setPosition(580,50);
       window.setSize(650,500);
       window.setMovable(false);
       window.add(fpslabel).padRight(600).padBottom(455);
       //stage.addActor(window);
   }

   private void createTaskOneTextImage(){
       Texture task1 = new Texture(Gdx.files.internal("UIElements/task1text.png"));
       TextureRegion task1Region = new TextureRegion(task1);
       TextureRegionDrawable task1Drawable = new TextureRegionDrawable(task1Region);
       Image task1Image = new Image(task1Drawable);
       task1Image.setSize(300,55);
       task1Image.setPosition(100,655);
       stage.addActor(task1Image);
       stage.setDebugAll(false);
   }

    private void beat(DungeonCoder g) throws IOException {
        s.insClear();
        if (s.autoSave()) {
            s.Save();
        }
    }

    private void createTest() {
        Texture save = new Texture(Gdx.files.internal("UIElements/freeWin.png"));
        TextureRegion saveRegion = new TextureRegion(save);
        TextureRegionDrawable saveDrawable = new TextureRegionDrawable(saveRegion);
        Image main4Image = new Image(saveDrawable);
        main4Image.setPosition(500, 500);
        main4Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    beat(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s.checkAchievement()){
                    stage.addActor(popupImage);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        stage.addActor(main4Image);
    }

    private void createHint() {
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        hintButton = new TextButton("Hint", skin);
        hintButton.setPosition(50, 15);
        hintButton.setSize(100, 30);
        hintButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                new Dialog("Hint", skin,"dialog"){
                    protected void result (Object object){
                        System.out.println("Result: "+ object);
                        System.out.println("CLICKED");
                    }
                }.text("Scanner scan = new Scanner(System.in);  \n\nscan.next(); //returns the next token of " +
                        "input  \nscan.hasNext(); //returns true if there is another token of input  \nscan.nextLine();" +
                        " //returns the next LINE of input  \nscan.hasNextLine(); //return true if there is another " +
                        "line of input  ").button(" Ok ", true).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });
        stage.addActor(hintButton);
    }

    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                btnBackClicked(game);
            }
        });
        stage.addActor(btnBack);
    }

    private void createPause(){
        Texture pause = new Texture(Gdx.files.internal("UIElements/pause.png"));
        TextureRegion pauseRegion = new TextureRegion(pause);
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseRegion);
        pauseImage = new Image(pauseDrawable);
        pauseImage.setSize(50,50);
        pauseImage.setPosition(1180,550);
        quitButton = new TextButton("Quit", skin);
        continueButton = new TextButton("Continue", skin);
        pauseImage.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                paused = true;
                Gdx.app.getApplicationListener().pause();
                new Dialog("Paused", skin,"dialog"){
                    protected void result (Object object){
                        System.out.println("Result: "+ object);
                        System.out.println("CLICKED");
                    }
                }.text("    The game is paused.    ").button(continueButton, true).button(quitButton,false).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        continueButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
             paused = false;
            }
        });

        quitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                btnBackClicked(game);
            }
        });
        stage.addActor(pauseImage);
    }

    private void createProgress(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));

        moduleText = new TextField("",skin);
        moduleText.setSize(150,50);
        moduleText.setPosition(580, 600);
        moduleText.setAlignment(Align.center);
        moduleText.setMessageText("Type in here!");
        moduleText.setText("Task1");
        stage.addActor(moduleText);

        TextButton btnModule = new TextButton("Module: ", skin);
        btnModule.setPosition(450, 600);
        btnModule.setSize(100,50);
        btnModule.addListener(new ClickListener(){
           @Override
           public void clicked (InputEvent e, float x, float y){
               module = moduleText.getText();
               System.out.println("Module: " + module);

           }
        });

        stage.addActor(btnModule);

        TextButton btnGetProgress = new TextButton("Progress: ", skin);
        btnGetProgress.setPosition(760,600);
        btnGetProgress.setSize(100,50);
        btnGetProgress.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent e, float x, float y){
                progress = progressText.getText();
                System.out.println("Progress: " + progress);
            }
        });

        stage.addActor(btnGetProgress);

        progressText = new TextField("", skin);
        progressText.setMessageText("Type in here!");
        progressText.setPosition(880,600);
        progressText.setSize(150,50);
        progressText.setText(shareVariable.connect.requestTaskInformation("Task1","Completion"));
        progressText.setAlignment(Align.center);
        stage.addActor(progressText);

        TextButton btnSend = new TextButton("Update Database: ", skin);
        btnSend.setPosition(1050,600);
        btnSend.setSize(150,50);
        btnSend.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent e, float x, float y){
                File file = new File("values/file.txt");
                progress = progressText.getText();
                int progress_Percent = 0;
                try{
                  progress_Percent = Integer.parseInt(progress);
                }catch(NumberFormatException ex){

                }
                /*if(shareVariable.connect.requestUpdateProgress(file,module,progress_Percent)){
                    System.out.println("Connected");
                }else{
                    System.out.println("Not Connected");
                }*/
            }
        });

        stage.addActor(btnSend);

    }

    private void createTextArea(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));

        textArea = new TextArea(" public class Solution  {\n " +
                "   public static void main(String[] args) " +
                "  {\n      /*Enter your code here...*/\n   }\n }", skin);
        textArea.setX(50);
        textArea.setY(50);
        textArea.setWidth(500);
        textArea.setHeight(500);
        System.out.println(textArea.getText());
        stage.addActor(textArea);
    }

    private void createAttempts(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        attemptText = new TextField("",skin);
        attemptText.setMessageText("Type in here!");
        attemptText.setPosition(270,600);
        attemptText.setSize(150,50);
        attemptText.setText(shareVariable.connect.requestTaskInformation("Task1","Attempts"));
        attemptText.setAlignment(Align.center);
        stage.addActor(attemptText);
        TextButton btnGetAttempt = new TextButton("Attempts: ", skin);
        btnGetAttempt.setPosition(150,600);
        btnGetAttempt.setSize(100,50);
        btnGetAttempt.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent e, float x, float y){
                attempt = attemptText.getText();
                attemptText.setText(shareVariable.connect.requestTaskInformation("Task1","Attempts"));
                System.out.println("Attempts: " + attempt);

            }
        });
        stage.addActor(btnGetAttempt);
    }

    private void btnBackClicked(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
    }
    @Override
    public void show() {
        System.out.println("yes you are in stage one");
    }

    @Override
    public void render(float delta) {
        //elapsedTime += Gdx.graphics.getDeltaTime(); //if wna make use of pause can stop the time here
        //player.updatePlayer();
        Gdx.gl.glClearColor(172/255f, 115/255f, 57/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bg = new Texture("gamebackground.png");
                //player.setPosition(player.getX(),player.getY());
        float x = Gdx.graphics.getWidth();
        float y = Gdx.graphics.getHeight();
        test = new Sprite(bg);
        test.setPosition(0,0);
        test.setSize(x ,y);
        backgroundBatch = new SpriteBatch();
        backgroundBatch.begin();
        test.draw(backgroundBatch);
        //backgroundBatch.draw(player,player.getX(),player.getY());
        backgroundBatch.end();

        //debugRenderer.render(world,box2DCamera.combined);//return proj matrix of the camera, what we see from the camera
        //world.step(Gdx.graphics.getDeltaTime(),6,2); //delta time the time between each frame
        //velocityiterations and positioniterations calculate how the bodies collide with each other

        fpslabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

        stage.draw();
        stage.act(delta);
        //begin walk

        if(paused){
            walkingBatch.begin();
            //walkingBatch.draw(walkAnimation.getKeyFrame(timePassed,true),VIRTUAL_WIDTH/2-60/2,VIRTUAL_HEIGHT/2-60/2,60,60);
            walkingBatch.end();
        }else{
            walkingBatch.begin();
            timePassed += Gdx.graphics.getDeltaTime();
            //walkingBatch.draw(walkAnimation.getKeyFrame(timePassed,true),VIRTUAL_WIDTH/2-60/2,VIRTUAL_HEIGHT/2-60/2,60,60);
            walkingBatch.end();
        }

        //begin ninja
        //ninjaBatch.begin();
        //ninjaBatch.draw(ninjaAnimation.getKeyFrame(timePassed,true),590,130,70,70);
        //ninjaBatch.end();
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
        stage.dispose();
        ninjaAtlas.dispose();
        walkingAtlas.dispose();
        backButtonSkin.dispose();
        backgroundBatch.dispose();
        walkingBatch.dispose();
        skin.dispose();
        bg.dispose();
        world.dispose();
        pic1.dispose();
        player.getTexture().dispose();
    }

    public void popup(){
            try {
                Thread.sleep(100);
                popupImage.moveBy(0,5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void popdown(){
        try {
            Thread.sleep(100);
            popupImage.moveBy(0,-5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
