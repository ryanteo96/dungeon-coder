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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private int mode = 0;

    public MainMenuScreen(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        createInstructional();
        createMainStory();
        createFreeBattle();
        createBack();
        createMenu1();
        createMenu2();
        createMenu3();
        createMenu4();
        createCharacter();
    }

    public void btnBackClicked(DungeonCoder g){
        g.setScreen(new SplashScreen(g));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(240/255f, 240/255f, 240/255f, 1);
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
/*
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

*/
/*
    private void createPlay() {
        Texture playTitle = new Texture(Gdx.files.internal("UIElements/playTitle.png"));
        TextureRegion playTitleRegion = new TextureRegion(playTitle);
        TextureRegionDrawable playTitleDrawable = new TextureRegionDrawable(playTitleRegion);
        Image playTitleImage = new Image(playTitleDrawable);
        playTitleImage.setPosition(550,600);
        stage.addActor(playTitleImage);
    }
*/
    private void createInstructional() {
        Texture instructionalMode = new Texture(Gdx.files.internal("UIElements/instructional.png"));
        TextureRegion instructionalModeRegion = new TextureRegion(instructionalMode);
        TextureRegionDrawable instructionalModeDrawable = new TextureRegionDrawable(instructionalModeRegion);
        Image instructionalModeImage = new Image(instructionalModeDrawable);
        instructionalModeImage.setPosition(840,585);
        instructionalModeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mode = 0;
            }
        });
        stage.addActor(instructionalModeImage);
    }

    private void instructionalMode(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
    }

    private void createBack() {
        Texture backicon = new Texture(Gdx.files.internal("UIElements/backicon.png"));
        TextureRegion backiconRegion = new TextureRegion(backicon);
        TextureRegionDrawable backiconDrawable = new TextureRegionDrawable(backiconRegion);
        Image backiconImage = new Image(backiconDrawable);
        backiconImage.setPosition(0,650);
        backiconImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToSplash(game);
            }
        });
        stage.addActor(backiconImage);
    }

    private void backToSplash(DungeonCoder g) {
        g.setScreen(new SplashScreen(g));
    }

    private void createMainStory() {
        Texture mainStoryMode = new Texture(Gdx.files.internal("UIElements/mainStory.png"));
        TextureRegion mainStoryModeRegion = new TextureRegion(mainStoryMode);
        TextureRegionDrawable mainStoryModeDrawable = new TextureRegionDrawable(mainStoryModeRegion);
        Image mainStoryModeImage = new Image(mainStoryModeDrawable);
        mainStoryModeImage.setPosition(960,585);
        mainStoryModeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mode = 1;
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
        freeBattleModeImage.setPosition(1080,585);
        freeBattleModeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mode = 2;
            }
        });
        stage.addActor(freeBattleModeImage);
    }

    private void freeBattleMode(DungeonCoder g) {
        g.setScreen(new FreeBattleMode(g));
    }

    private void createMenu1(){
        Texture main1 = new Texture(Gdx.files.internal("UIElements/Main1.png"));
        TextureRegion main1Region = new TextureRegion(main1);
        TextureRegionDrawable main1Drawable = new TextureRegionDrawable(main1Region);
        Image main1Image = new Image(main1Drawable);
        main1Image.setPosition(840,453);
        main1Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mode == 0){
                    instructionalMode(game);
                } else if (mode == 1){
                    mainStoryMode(game);
                } else if (mode == 2){
                    freeBattleMode(game);
                }
            }
        });
        stage.addActor(main1Image);
    }
    private void createMenu2(){
        Texture main2 = new Texture(Gdx.files.internal("UIElements/Main2.png"));
        TextureRegion main2Region = new TextureRegion(main2);
        TextureRegionDrawable main2Drawable = new TextureRegionDrawable(main2Region);
        Image main2Image = new Image(main2Drawable);
        main2Image.setPosition(840,325);
        main2Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CostumeScreen(game);
            }
        });
        stage.addActor(main2Image);
    }
    private void CostumeScreen(DungeonCoder g) {
        g.setScreen(new CostumeScreen(g));
    }

    private void createMenu3(){
        Texture main3 = new Texture(Gdx.files.internal("UIElements/Main3.png"));
        TextureRegion main3Region = new TextureRegion(main3);
        TextureRegionDrawable main3Drawable = new TextureRegionDrawable(main3Region);
        Image main3Image = new Image(main3Drawable);
        main3Image.setPosition(840,197);
        main3Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DesignScreen(game);
            }
        });
        stage.addActor(main3Image);
    }
    private void DesignScreen(DungeonCoder g) {
        g.setScreen(new DesignScreen(g));
    }

    private void createMenu4(){
        Texture main4 = new Texture(Gdx.files.internal("UIElements/Main4.png"));
        TextureRegion main4Region = new TextureRegion(main4);
        TextureRegionDrawable main4Drawable = new TextureRegionDrawable(main4Region);
        Image main4Image = new Image(main4Drawable);
        main4Image.setPosition(840,69);
        main4Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                OptionScreen(game);
            }
        });
        stage.addActor(main4Image);
    }
    private void OptionScreen(DungeonCoder g) {
        g.setScreen(new OptionScreen(g));
    }

    private void createCharacter(){
        Texture chara = new Texture(Gdx.files.internal("UIElements/chara.png"));
        TextureRegion charaRegion = new TextureRegion(chara);
        TextureRegionDrawable charaDrawable = new TextureRegionDrawable(charaRegion);
        Image charaImage = new Image(charaDrawable);
        charaImage.setPosition(200,200);
        stage.addActor(charaImage);
    }

}
