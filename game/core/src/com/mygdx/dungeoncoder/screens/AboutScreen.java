package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class AboutScreen implements Screen {
    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private Label titleLabel;
    private Label infoLabel;
    private Label creatorLabel;

    public AboutScreen (DungeonCoder g){
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        createBack();

        titleLabel = new Label("About DungeonCoder",new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        titleLabel.setFontScale(3);
        infoLabel = new Label("This is an educational game aimed towards students and coding beginners.\n\n" +
                "Learning how to code is rarely considered easy and interesting and\nit is one of the biggest " +
                "problem for learners.\n\nThis game will provide an interesting way for beginner and experienced" +
                "\nprogrammers to apply their skills and idea as well.",new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        infoLabel.setFontScale(2);
        creatorLabel = new Label("Game Creators:\nRyan, Henry, Devon, Gary",new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        table.add(titleLabel).padTop(50).padBottom(20);
        table.row();
        table.add(infoLabel).left();
        table.row();
        table.add(creatorLabel).right().padTop(250);
        table.setDebug(false);
        stage.addActor(table);
    }


    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new MainStoryMode(game));;
            }
        });
        stage.addActor(btnBack);
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
