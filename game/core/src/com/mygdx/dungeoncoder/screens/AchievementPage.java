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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.SaveProcessor;
import sun.applet.Main;

import java.io.*;
import java.util.Scanner;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class AchievementPage implements Screen {
    SaveProcessor s = new SaveProcessor();
    private DungeonCoder game;
    private Stage stage;
    public int stageC;
    public int InsC;
    public int MainC;
    public int FreeC;
    public int autoSave;
    private Skin backButtonSkin;

    public AchievementPage(DungeonCoder g) throws FileNotFoundException {
        InsC = s.getInsCleared();
        MainC = s.getMainCleared();
        FreeC = s.getFreeCleared();
        stageC = InsC + MainC + FreeC;
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        createBack();
        createAC1();
        createAC2();
        createAC3();
        createSave();
        createTest();
        createOn();
        createOff();
        System.out.printf("Overall Stage completed: %d\n", stageC);
        System.out.printf("Instructional Stage completed: %d\n", InsC);
        System.out.printf("Main Story Completed: %d\n", MainC);
        System.out.printf("Free Battle Completed: %d\n", FreeC);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(240 / 255f, 240 / 255f, 240 / 255f, 1);
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

    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                back();

            }
        });
        stage.addActor(btnBack);
    }


    private void back() {
        game.setScreen(new SplashScreen(game));
    }

    private void createAC1() {
        Texture AC1;
        if (stageC >= 5) {
            AC1 = new Texture(Gdx.files.internal("UIElements/AC130.png"));
        } else {
            AC1 = new Texture(Gdx.files.internal("UIElements/AC1.png"));
        }
        TextureRegion AC1Region = new TextureRegion(AC1);
        TextureRegionDrawable AC1Drawable = new TextureRegionDrawable(AC1Region);
        Image AC1Image = new Image(AC1Drawable);
        AC1Image.setPosition(400, 500);
        stage.addActor(AC1Image);
    }

    private void createAC2() {
        Texture AC2;
        AC2 = new Texture(Gdx.files.internal("UIElements/AC2.png"));
        TextureRegion AC2Region = new TextureRegion(AC2);
        TextureRegionDrawable AC2Drawable = new TextureRegionDrawable(AC2Region);
        Image AC2Image = new Image(AC2Drawable);
        AC2Image.setPosition(400, 300);
        stage.addActor(AC2Image);
    }

    private void createAC3() {
        Texture AC3;
        AC3 = new Texture(Gdx.files.internal("UIElements/AC3.png"));
        TextureRegion AC3Region = new TextureRegion(AC3);
        TextureRegionDrawable AC3Drawable = new TextureRegionDrawable(AC3Region);
        Image AC3Image = new Image(AC3Drawable);
        AC3Image.setPosition(400, 100);
        stage.addActor(AC3Image);
    }

    private void createSave() {
        Texture save = new Texture(Gdx.files.internal("UIElements/save.png"));
        TextureRegion saveRegion = new TextureRegion(save);
        TextureRegionDrawable saveDrawable = new TextureRegionDrawable(saveRegion);
        Image main4Image = new Image(saveDrawable);
        main4Image.setPosition(1080, 630);
        main4Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Save(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.addActor(main4Image);
    }

    private void Save(DungeonCoder g) throws IOException {
        s.Save();
    }

    private void createTest() {
        Texture save = new Texture(Gdx.files.internal("UIElements/freeWin.png"));
        TextureRegion saveRegion = new TextureRegion(save);
        TextureRegionDrawable saveDrawable = new TextureRegionDrawable(saveRegion);
        Image main4Image = new Image(saveDrawable);
        main4Image.setPosition(1000, 630);
        main4Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    beat(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.addActor(main4Image);
    }

    private void beat(DungeonCoder g) throws IOException {
        s.insClear();
        if (s.autoSave()) {
            Save(game);
        }
        if (stageC == 5){
            System.out.println("Achievement 1 completed!!!");
        }
    }

    private void createOn() {
        Texture back = new Texture(Gdx.files.internal("UIElements/On.png"));
        TextureRegion backRegion = new TextureRegion(back);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(backRegion);
        Image backImage = new Image(backDrawable);
        backImage.setPosition(1000, 500);
        backImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.setAutoSave(1);
            }
        });
        stage.addActor(backImage);
    }
    private void createOff() {
        Texture back = new Texture(Gdx.files.internal("UIElements/Off.png"));
        TextureRegion backRegion = new TextureRegion(back);
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(backRegion);
        Image backImage = new Image(backDrawable);
        backImage.setPosition(1000, 440);
        backImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.setAutoSave(0);
            }
        });
        stage.addActor(backImage);
    }


}