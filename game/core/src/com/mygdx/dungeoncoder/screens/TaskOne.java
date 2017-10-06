package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.ClientConnection;

import java.io.File;

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskOne extends ApplicationAdapter implements Screen {
    private ClientConnection clientConnection;
    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private Skin skin;
    TextField attemptText;
    String attempt;
    TextField progressText;
    String progress;
    TextField moduleText;
    String module;
    TextArea textArea;
    ScrollPane scrollPane;
    TextButton quitButton;
    TextButton hintButton;

    public TaskOne(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        create();
        createBack();
        createAttempts();
        createProgress();
        createTextArea();
        createDemoImage();
        createPause();
        createHint();
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
                }.text("Scanner scan = new Scanner(System.in);  \nscan.next(); //returns the next token of " +
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
        Image pauseImage = new Image(pauseDrawable);
        pauseImage.setSize(50,50);
        pauseImage.setPosition(1180,500);
        quitButton = new TextButton("Quit", skin);
        pauseImage.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                new Dialog("Paused", skin,"dialog"){
                    protected void result (Object object){
                        System.out.println("Result: "+ object);
                        System.out.println("CLICKED");
                    }
                }.text("    The game is paused.    ").button("Continue", true).button(quitButton,false).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnBackClicked(game);
            }
        });
        stage.addActor(pauseImage);
    }

    private void createDemoImage(){
        Texture demo = new Texture(Gdx.files.internal("UIElements/demo.png"));
        TextureRegion demoRegion = new TextureRegion(demo);
        TextureRegionDrawable demoDrawable = new TextureRegionDrawable(demoRegion);
        Image demoImage = new Image(demoDrawable);
        demoImage.setSize(650,500);
        demoImage.setPosition(580,50);
        stage.addActor(demoImage);
    }

    private void createProgress(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));

        moduleText = new TextField("",skin);
        moduleText.setSize(150,50);
        moduleText.setPosition(580, 600);
        moduleText.setAlignment(Align.center);
        moduleText.setMessageText("Type in here!");
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

        progressText = new TextField("", skin);
        progressText.setMessageText("Type in here!");
        progressText.setPosition(900,600);
        progressText.setSize(150,50);
        progressText.setAlignment(Align.center);
        stage.addActor(progressText);

        TextButton btnGetProgress = new TextButton("Progress: ", skin);
        btnGetProgress.setPosition(760,600);
        btnGetProgress.setSize(100,50);
        btnGetProgress.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent e, float x, float y){
                File file = new File("values/file.txt");
                progress = progressText.getText();
                System.out.println("Progress: " + progress);
               /* int progress_Percent = 0;
                try{
                  progress_Percent = Integer.parseInt(progress);
                }catch(NumberFormatException ex){

                }
                if(clientConnection.requestUpdateProgress(file,module,progress_Percent)){
                    System.out.println("Connected");
                }else{
                    System.out.println("Not Connected");
                }*/
            }
        });


        stage.addActor(btnGetProgress);

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
        stage.addActor(textArea);
    }

    private void createAttempts(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        attemptText = new TextField("",skin);
        attemptText.setMessageText("Type in here!");
        attemptText.setPosition(270,600);
        attemptText.setSize(150,50);
        attemptText.setAlignment(Align.center);
        stage.addActor(attemptText);
        TextButton btnGetAttempt = new TextButton("Attempts: ", skin);
        btnGetAttempt.setPosition(150,600);
        btnGetAttempt.setSize(100,50);
        btnGetAttempt.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent e, float x, float y){
                attempt = attemptText.getText();
                System.out.println("Attempts: " + attempt);
            }
        });
        stage.addActor(btnGetAttempt);
    }

    public void create(){

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
        Gdx.gl.glClearColor(172/255f, 115/255f, 57/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
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
    }


}
