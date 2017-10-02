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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class InstructionalMode implements Screen {

    private DungeonCoder game;
    private Stage stage;

    public InstructionalMode (DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);

        createBack();
        createStages();

    }

    @Override
    public void show() {

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

    }

    private void createBack() {
        Texture back = new Texture(Gdx.files.internal("UIElements/back.png"));
        TextureRegion backRegion = new TextureRegion(back);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(backRegion);
        Image backImage = new Image(backDrawable);
        backImage.setPosition(0,680);
        backImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                back();
            }
        });

        stage.addActor(backImage);
    }

    private void back() {
        game.setScreen(new MainMenuScreen(game));
    }


    private void createStages(){
        Texture stage_One = new Texture(Gdx.files.internal("UIElements/stage1.png"));
        TextureRegion stage_OneRegion = new TextureRegion(stage_One);
        TextureRegionDrawable stage_OneDrawable = new TextureRegionDrawable(stage_OneRegion);
        Image stage_OneImage = new Image(stage_OneDrawable);
        stage_OneImage.setPosition(100,400);
        stage_OneImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage_One(game);
            }
        });

        stage.addActor(stage_OneImage);
    }

    private void stage_One(DungeonCoder g) {
        g.setScreen(new StageOne(g));
    }
}
