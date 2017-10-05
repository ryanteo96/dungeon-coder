package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskOne implements Screen {
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

    public TaskOne(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        create();
        createBack();
        createAttempts();
        createProgress();
        createRequestAccountCreation();
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

    private void createProgress(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));

        moduleText = new TextField("",skin);
        moduleText.setSize(150,50);
        moduleText.setPosition(270, 500);
        moduleText.setAlignment(Align.center);
        moduleText.setMessageText("Type in here!");
        stage.addActor(moduleText);

        TextButton btnModule = new TextButton("Module: ", skin);
        btnModule.setPosition(150, 500);
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
        progressText.setPosition(270,400);
        progressText.setSize(150,50);
        progressText.setAlignment(Align.center);
        stage.addActor(progressText);

        TextButton btnGetProgress = new TextButton("Progress: ", skin);
        btnGetProgress.setPosition(150,400);
        btnGetProgress.setSize(100,50);
        btnGetProgress.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent e, float x, float y){
                progress = progressText.getText();
                System.out.println("Progress: " + progress);
            }
        });

        stage.addActor(btnGetProgress);

    }

    private void createRequestAccountCreation(){

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
