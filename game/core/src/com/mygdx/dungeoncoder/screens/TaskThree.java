package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class TaskThree implements Screen {
    private DungeonCoder game;
    private Stage stage;

    public TaskThree(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        createStageThree();
        createBack();
    }

    private void createBack() {
        Texture backicon = new Texture(Gdx.files.internal("UIElements/back.png"));
        TextureRegion backiconRegion = new TextureRegion(backicon);
        TextureRegionDrawable backiconDrawable = new TextureRegionDrawable(backiconRegion);
        Image backiconImage = new Image(backiconDrawable);
        backiconImage.setPosition(0,680);
        backiconImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToInstructionalMode(game);
            }
        });
        stage.addActor(backiconImage);
    }

    private void backToInstructionalMode(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
    }
    @Override
    public void show() {
        System.out.println("you are in stage three");
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

    }

    private void createStageThree(){

        Texture stage_Three = new Texture(Gdx.files.internal("UIElements/a6c.jpeg"));
        TextureRegion stage_ThreeRegion = new TextureRegion(stage_Three);
        TextureRegionDrawable stage_ThreeDrawable = new TextureRegionDrawable(stage_ThreeRegion);
        Image stage_ThreeImage = new Image(stage_ThreeDrawable);
        stage_ThreeImage.setSize(400,500);
        stage_ThreeImage.setPosition(450,150);
        stage.addActor(stage_ThreeImage);
    }
}
