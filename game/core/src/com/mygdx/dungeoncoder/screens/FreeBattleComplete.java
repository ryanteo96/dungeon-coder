package com.mygdx.dungeoncoder.screens;

import Sprites.Adventurer.Adventurer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.SaveProcessor;
import com.mygdx.dungeoncoder.values.DefaultValues;

public class FreeBattleComplete implements Screen {
    private Viewport viewport;
    private Stage stage;
    private DungeonCoder game;
    public Image popupImage;
    SaveProcessor saveProcessor;
    Adventurer player;

    public FreeBattleComplete(DungeonCoder game){
        this.game = game;
        viewport = new FitViewport(DefaultValues.VIRTUAL_WIDTH,DefaultValues.VIRTUAL_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport, ((DungeonCoder) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        saveProcessor = new SaveProcessor();
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("STAGE COMPLETED!", font);
        Label playAgainLabel = new Label("Click to return to Free Battle Mode", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            game.setScreen(new FreeBattleMode((DungeonCoder)game));
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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
}
