package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.values.DefaultValues;

import java.text.ParseException;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class MainStoryMode implements Screen {

    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private Image bgImg;
    private boolean hover;
    private Image quitImage;
    private Image startImage;
    private Image aboutImage;


    public MainStoryMode (DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        hover = false;
        Gdx.input.setInputProcessor(stage);
        createMainMenu();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(172/255f, 115/255f, 57/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();
    }

    private void createMainMenu(){
        Texture start = new Texture("StoryMode/start.png");
        TextureRegion startRegion = new TextureRegion(start);
        TextureRegionDrawable startDrawable = new TextureRegionDrawable(startRegion);
        startImage = new Image(startDrawable);
        startImage.setPosition(180,450);
        startImage.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new MainStory1(game));
            }
        });

        Texture about = new Texture("StoryMode/About.png");
        TextureRegion aboutRegion = new TextureRegion(about);
        TextureRegionDrawable aboutDrawable = new TextureRegionDrawable(aboutRegion);
        aboutImage = new Image(aboutDrawable);
        aboutImage.setPosition(180,400);
        aboutImage.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new AboutScreen(game));
            }
        });

        Texture quit = new Texture("StoryMode/quit.png");
        TextureRegion quitRegion = new TextureRegion(quit);
        TextureRegionDrawable quitDrawable = new TextureRegionDrawable(quitRegion);
        quitImage = new Image(quitDrawable);
        quitImage.setPosition(180,350);
        quitImage.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                try {
                    back();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Texture bg = new Texture(Gdx.files.internal("StoryMode/storymodebg.png"));
        TextureRegion bgRegion = new TextureRegion(bg);
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(bgRegion);
        bgImg = new Image(bgDrawable);
        bgImg.setPosition(0,0);
        bgImg.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        stage.addActor(bgImg);
        stage.addActor(startImage);
        stage.addActor(quitImage);
        stage.addActor(aboutImage);
    }

    private void back() throws ParseException {
        game.setScreen(new MainMenuScreen(game));
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
        backButtonSkin.dispose();
    }




}
