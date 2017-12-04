package com.mygdx.dungeoncoder.screens;

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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.ClientConnection;


import java.io.FileNotFoundException;
import java.text.ParseException;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class SplashScreen implements Screen {
    private DungeonCoder game;
    private Stage stage;
    private Skin skin;
    private TextButton okButton;

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
        stage.dispose();
        skin.dispose();
    }

    private void createAchievement(){
        Texture main4 = new Texture(Gdx.files.internal("UIElements/tro.png"));
        TextureRegion main4Region = new TextureRegion(main4);
        TextureRegionDrawable main4Drawable = new TextureRegionDrawable(main4Region);
        Image main4Image = new Image(main4Drawable);
        main4Image.setPosition(0,580);
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
        main4Image.setPosition(1160,580);
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
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        Texture main4 = new Texture(Gdx.files.internal("UIElements/Splash.png"));
        TextureRegion main4Region = new TextureRegion(main4);
        TextureRegionDrawable main4Drawable = new TextureRegionDrawable(main4Region);
        okButton = new TextButton("  Ok  ", skin);
        Image main4Image = new Image(main4Drawable);
        main4Image.setPosition(0,0);
        main4Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(shareVariable.connected == true){
                    try {
                        Mainmenu(game);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    skin = new Skin(Gdx.files.internal("UIElements/test.json"));
                    new Dialog("Dungeon Coder", skin,"dialog"){
                        protected void result (Object object){
                            System.out.println("Result: "+ object);
                            System.out.println("Try to go to main menu page");
                        }
                    }.text("\n          You are not logged in!          \n").button( okButton, true).button("  Cancel  ",false).
                            key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
                }
            }
        });

        okButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LoginScreen(game));
            }
        });

        stage.addActor(main4Image);
    }
    private void Mainmenu(DungeonCoder g) throws ParseException {
        g.setScreen(new MainMenuScreen(g));
    }
}
