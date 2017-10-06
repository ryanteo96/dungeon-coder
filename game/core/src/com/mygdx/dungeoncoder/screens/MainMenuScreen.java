package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

import javax.swing.event.ChangeEvent;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class MainMenuScreen implements Screen {

    private DungeonCoder game;
    private Stage stage;
    private int mode = 0;
    private Skin backButtonSkin;
    private Skin dialogSkin;
    private boolean finishedAssignment = true;
    TextButton oKButton;
    TextButton cannotContinue;
    TextButton continueButton;

    Texture instructionalMode_Text = new Texture(Gdx.files.internal("UIElements/instructionalmode.png"));
    TextureRegion instructionalModeRegion_Text = new TextureRegion(instructionalMode_Text);
    TextureRegionDrawable instructionalModeDrawable_Text = new TextureRegionDrawable(instructionalModeRegion_Text);
    Image instructionalModeImage_Text = new Image(instructionalModeDrawable_Text);

    Texture mainStoryMode_Text = new Texture(Gdx.files.internal("UIElements/mainstorymode.png"));
    TextureRegion mainStoryModeRegion_Text = new TextureRegion(mainStoryMode_Text);
    TextureRegionDrawable mainStoryModeDrawable_Text = new TextureRegionDrawable(mainStoryModeRegion_Text);
    Image mainStoryModeImage_Text = new Image(mainStoryModeDrawable_Text);

    Texture freeBattleMode_Text = new Texture(Gdx.files.internal("UIElements/freebattlemode.png"));
    TextureRegion freeBattleModeRegion_Text = new TextureRegion(freeBattleMode_Text);
    TextureRegionDrawable freeBattleModeDrawable_Text = new TextureRegionDrawable(freeBattleModeRegion_Text);
    Image freeBattleModeImage_Text = new Image(freeBattleModeDrawable_Text);


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
    public void btnBackClicked(DungeonCoder g) {
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
        backButtonSkin.dispose();
        stage.dispose();
    }

    private void instructionalMode(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
    }

    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                btnBackClicked(game);

            }
        });
        stage.addActor(btnBack);
    }

    private void createInstructional() {
        Texture instructionalMode = new Texture(Gdx.files.internal("UIElements/instructional.png"));
        TextureRegion instructionalModeRegion = new TextureRegion(instructionalMode);
        TextureRegionDrawable instructionalModeDrawable = new TextureRegionDrawable(instructionalModeRegion);
        Image instructionalModeImage = new Image(instructionalModeDrawable);
        instructionalModeImage.setPosition(840,585);
        instructionalModeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mode = 0;
                instructionalModeImage_Text.setPosition(200, 500);
                System.out.println("You are now in instructional mode");
                stage.addActor(instructionalModeImage_Text);
                instructionalModeImage_Text.setVisible(true);
                mainStoryModeImage_Text.setVisible(false);
                freeBattleModeImage_Text.setVisible(false);
            }
        });

        stage.addActor(instructionalModeImage);
    }


    private void createMainStory() {
        dialogSkin = new Skin(Gdx.files.internal("UIElements/test.json"));
        Texture mainStoryMode = new Texture(Gdx.files.internal("UIElements/mainStory.png"));
        TextureRegion mainStoryModeRegion = new TextureRegion(mainStoryMode);
        TextureRegionDrawable mainStoryModeDrawable = new TextureRegionDrawable(mainStoryModeRegion);
        Image mainStoryModeImage = new Image(mainStoryModeDrawable);
        mainStoryModeImage.setPosition(960,585);
        mainStoryModeImage.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mode = 1;
                mainStoryModeImage_Text.setPosition(200, 500);
                System.out.println("You are now in main story mode");
                stage.addActor(mainStoryModeImage_Text);
                instructionalModeImage_Text.setVisible(false);
                mainStoryModeImage_Text.setVisible(true);
                freeBattleModeImage_Text.setVisible(false);
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
                freeBattleModeImage_Text.setPosition(200, 500);
                System.out.println("You are now in free battle mode");
                stage.addActor(freeBattleModeImage_Text);
                freeBattleModeImage_Text.setVisible(true);
                instructionalModeImage_Text.setVisible(false);
                mainStoryModeImage_Text.setVisible(false);
            }
        });
        stage.addActor(freeBattleModeImage);
    }

    private void freeBattleMode(DungeonCoder g) {
        g.setScreen(new FreeBattleMode(g));
    }

    private void createMenu1(){
        dialogSkin = new Skin(Gdx.files.internal("UIElements/test.json"));
        Texture main1 = new Texture(Gdx.files.internal("UIElements/Main1.png"));
        TextureRegion main1Region = new TextureRegion(main1);
        TextureRegionDrawable main1Drawable = new TextureRegionDrawable(main1Region);
        Image main1Image = new Image(main1Drawable);
        main1Image.setPosition(840,453);
        cannotContinue = new TextButton("OK" ,dialogSkin);
        continueButton = new TextButton("Continue" ,dialogSkin);
        main1Image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mode == 0){
                    instructionalMode(game);
                } else if (mode == 1){
                    if(finishedAssignment){
                        new Dialog("Access Granted.", dialogSkin,"dialog"){
                            protected void result (Object object){
                                System.out.println("Result: "+ object);
                                System.out.println("CLICKED");
                            }
                        }.text("    You have finished your assignment.    ").button(continueButton, true).button("Cancel",false).
                                key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);

                    }else{
                        new Dialog("Access Denied!", dialogSkin,"dialog"){
                            protected void result (Object object){
                                System.out.println("Result: "+ object);
                                System.out.println("CLICKED");
                            }
                        }.text("     You have not finished your assignment!     ").button(cannotContinue, false).button("Cancel",false).
                                key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
                    }

                    //continue to go to main story mode if assignment is finished.
                    continueButton.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            mainStoryMode(game);
                        }
                    });

                    /*//stay at main menu if assignment is not finished
                    cannotContinue.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            game.setScreen(new MainMenuScreen(game));
                        }
                    });*/

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
