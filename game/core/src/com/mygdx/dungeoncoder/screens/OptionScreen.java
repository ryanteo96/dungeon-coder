package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.SaveProcessor;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class OptionScreen implements Screen{

    private DungeonCoder game;
    private Stage stage;
    public SaveProcessor s = new SaveProcessor();

    public OptionScreen (DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        createBack();
        createAutoSave();
        createOff();
        createOn();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255/255f, 255/255f, 255/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

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

    private void createBack() {
        Texture back = new Texture(Gdx.files.internal("UIElements/back.png"));
        TextureRegion backRegion = new TextureRegion(back);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(backRegion);
        Image backImage = new Image(backDrawable);
        backImage.setPosition(0,580);
        backImage.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                back();
            }
        }
        );

        stage.addActor(backImage);
    }

    private void back() {
        dispose();
        game.setScreen(new MainMenuScreen(game));
    }

    private void createInProgress() {
        Texture inProgress = new Texture(Gdx.files.internal("UIElements/inProgress4.png"));
        TextureRegion inProgressRegion = new TextureRegion(inProgress);
        TextureRegionDrawable inProgressDrawable = new TextureRegionDrawable(inProgressRegion);
        Image inProgressImage = new Image(inProgressDrawable);
        inProgressImage.setPosition(450,300);

        stage.addActor(inProgressImage);
    }

    private void createAutoSave(){
        Texture autosave = new Texture(Gdx.files.internal("UIElements/Autosave.png"));
        TextureRegion autosaveR= new TextureRegion(autosave);
        TextureRegionDrawable autosaveD = new TextureRegionDrawable(autosaveR);
        Image autosaveP = new Image(autosaveD);
        autosaveP.setPosition(400, 385);
        stage.addActor(autosaveP);
    }

    private void createOn() {
        Texture back = new Texture(Gdx.files.internal("UIElements/On.png"));
        TextureRegion backRegion = new TextureRegion(back);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(backRegion);
        Image backImage = new Image(backDrawable);
        backImage.setPosition(670, 400);
        backImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.setAutoSave(1);
                s.Save();
            }
        });
        stage.addActor(backImage);
    }
    private void createOff() {
        Texture back = new Texture(Gdx.files.internal("UIElements/Off.png"));
        TextureRegion backRegion = new TextureRegion(back);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(backRegion);
        Image backImage = new Image(backDrawable);
        backImage.setPosition(800, 400);
        backImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.setAutoSave(0);
                s.Save();
            }
        });
        stage.addActor(backImage);
    }

}
