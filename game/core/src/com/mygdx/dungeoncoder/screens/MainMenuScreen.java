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

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class MainMenuScreen implements Screen {

    private DungeonCoder game;
    private Stage stage;

    public MainMenuScreen(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);

        createSignOutButton();
        createInstructional();
        createMainStory();
        createFreeBattle();
        createPlay();

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

    private void createSignOutButton() {
        Texture signOut = new Texture(Gdx.files.internal("UIElements/signout.png"));
        TextureRegion signOutRegion = new TextureRegion(signOut);
        TextureRegionDrawable signOutDrawable = new TextureRegionDrawable(signOutRegion);
        Image signOutImage = new Image(signOutDrawable);
        signOutImage.setPosition(0,680);
        signOutImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                signOut(game);
            }
        });

        stage.addActor(signOutImage);
    }

    private void signOut(DungeonCoder g) {
        g.setScreen(new LoginScreen(g));
    }

    private void createPlay() {
        Texture playTitle = new Texture(Gdx.files.internal("UIElements/playTitle.png"));
        TextureRegion playTitleRegion = new TextureRegion(playTitle);
        TextureRegionDrawable playTitleDrawable = new TextureRegionDrawable(playTitleRegion);
        Image playTitleImage = new Image(playTitleDrawable);
        playTitleImage.setPosition(550,600);
        stage.addActor(playTitleImage);
    }

    private void createInstructional() {
        Texture instructionalMode = new Texture(Gdx.files.internal("UIElements/instructional.png"));
        TextureRegion instructionalModeRegion = new TextureRegion(instructionalMode);
        TextureRegionDrawable instructionalModeDrawable = new TextureRegionDrawable(instructionalModeRegion);
        Image instructionalModeImage = new Image(instructionalModeDrawable);
        instructionalModeImage.setPosition(100,100);
        instructionalModeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instructionalMode(game);
            }
        });

        stage.addActor(instructionalModeImage);

    }

    private void instructionalMode(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
    }

    private void createMainStory() {
        Texture mainStoryMode = new Texture(Gdx.files.internal("UIElements/mainStory.png"));
        TextureRegion mainStoryModeRegion = new TextureRegion(mainStoryMode);
        TextureRegionDrawable mainStoryModeDrawable = new TextureRegionDrawable(mainStoryModeRegion);
        Image mainStoryModeImage = new Image(mainStoryModeDrawable);
        mainStoryModeImage.setPosition(470,100);
        mainStoryModeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainStoryMode(game);
            }
        });

        stage.addActor(mainStoryModeImage);
    }

    private void mainStoryMode(DungeonCoder g) {
        g.setScreen(new MainStoryMode(g));
    }

    private void createFreeBattle() {
        Texture freeBattleMode = new Texture(Gdx.files.internal("UIElements/freeBattle.png"));
        TextureRegion freeBattleModeRegion = new TextureRegion(freeBattleMode);
        TextureRegionDrawable freeBattleModeDrawable = new TextureRegionDrawable(freeBattleModeRegion);
        Image freeBattleModeImage = new Image(freeBattleModeDrawable);
        freeBattleModeImage.setPosition(840,100);
        freeBattleModeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                freeBattleMode(game);
            }
        });

        stage.addActor(freeBattleModeImage);
    }

    private void freeBattleMode(DungeonCoder g) {
        g.setScreen(new FreeBattleMode(g));
    }


}
