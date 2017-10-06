package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

import java.io.FileNotFoundException;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class SplashScreen implements Screen {
    private DungeonCoder game;
    private Stage stage;

    public SplashScreen(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);

        createBackground();
        createAchievement();
        createAccount();
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

    private void createAchievement(){
        Texture main4 = new Texture(Gdx.files.internal("UIElements/tro.png"));
        TextureRegion main4Region = new TextureRegion(main4);
        TextureRegionDrawable main4Drawable = new TextureRegionDrawable(main4Region);
        Image main4Image = new Image(main4Drawable);
        main4Image.setPosition(0,600);
        main4Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    AchievementPage(game);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.addActor(main4Image);
    }
    private void AchievementPage(DungeonCoder g) throws FileNotFoundException {
        g.setScreen(new AchievementPage(g));
    }

    private void createAccount(){
        Texture main4 = new Texture(Gdx.files.internal("UIElements/person.png"));
        TextureRegion main4Region = new TextureRegion(main4);
        TextureRegionDrawable main4Drawable = new TextureRegionDrawable(main4Region);
        Image main4Image = new Image(main4Drawable);
        main4Image.setPosition(1160,600);
        main4Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LoginScreen(game);
            }
        });
        stage.addActor(main4Image);
    }
    private void LoginScreen(DungeonCoder g) {
        g.setScreen(new LoginScreen(g));
    }

    private void createBackground(){
        Texture main4 = new Texture(Gdx.files.internal("UIElements/Splash.png"));
        TextureRegion main4Region = new TextureRegion(main4);
        TextureRegionDrawable main4Drawable = new TextureRegionDrawable(main4Region);
        Image main4Image = new Image(main4Drawable);
        main4Image.setPosition(0,0);
        main4Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Mainmenu(game);
            }
        });
        stage.addActor(main4Image);
    }
    private void Mainmenu(DungeonCoder g) {
        g.setScreen(new MainMenuScreen(g));
    }
}
