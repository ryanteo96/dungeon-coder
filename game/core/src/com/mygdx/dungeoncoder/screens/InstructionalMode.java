package com.mygdx.dungeoncoder.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.plaf.basic.BasicOptionPaneUI;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class InstructionalMode extends ScreenAdapter implements Screen{

    private DungeonCoder game;
    private Stage stage;
    private Skin skin;

    public InstructionalMode (DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);
        createStages();
        createBack();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(172/255f,115/255f,57/255f,1);
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
//        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
     //   skin.dispose();
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

        Texture stage_Two = new Texture(Gdx.files.internal("UIElements/stage2.png"));
        TextureRegion stage_TwoRegion = new TextureRegion(stage_Two);
        TextureRegionDrawable stage_TwoDrawable = new TextureRegionDrawable(stage_TwoRegion);
        Image stage_TwoImage = new Image(stage_TwoDrawable);
        stage_TwoImage.setPosition(500,400);
        stage_TwoImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage_Two(game);
            }
        });

        stage.addActor(stage_TwoImage);

        Texture stage_Three = new Texture(Gdx.files.internal("UIElements/stage3.png"));
        TextureRegion stage_ThreeRegion = new TextureRegion(stage_Three);
        TextureRegionDrawable stage_ThreeDrawable = new TextureRegionDrawable(stage_ThreeRegion);
        Image stage_ThreeImage = new Image(stage_ThreeDrawable);
        stage_ThreeImage.setPosition(900,400);
        stage_ThreeImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage_Three(game);
            }
        });

        stage.addActor(stage_ThreeImage);
    }

    private void stage_One(DungeonCoder g) {
        /*stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(stage = new Stage());*/
        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
        TextButton startButton = new TextButton("Start", skin, "default");
        TextButton cancelButton = new TextButton("Cancel", skin, "default");

        Dialog dialog = new Dialog("Stage 1",skin);
        dialog.getBackground().setMinWidth(250);//size of the dialog
        dialog.getBackground().setMinHeight(600);
        startButton.setSize(80,30);
        startButton.setPosition(970,20);

        cancelButton.setSize(100,30);
        cancelButton.setPosition(1060,20);

        dialog.addActor(startButton);
        dialog.addActor(cancelButton);


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    stageOne(game);

            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToInstructionalMode(game);
            }
        });


        String str = "Objective\nIn this challenge, we review some basic concepts that will get you started with this module.\n\n" +
                "Task\n" +
                "To complete this challenge, you must save a line of input from stdin to a variable, print Hello, World.\n" +
                "and finally print the value of your variable on the next line.\n\n" +
                "Sample Input\n" +
                "Welcome to Dungeon Coding!\n\n" +
                "Sample Output\n" +
                "Hello, World.\n" +
                "Welcome to Dungeon Coding!\n\n" +
                "Explanation\n" +
                "On the first line, we print the string literal Hello, World.. On the second line, we print the contents of the \nvariable which, for this sample case," +
                "happens to be Welcome to Dungeon Coding!. If you do not print the \nvariable's contents to stdout, you will not pass the hidden test case.\n\n" +
                "Difficulty: One Dungeon";


        dialog.text(str);

        //stage.addActor(dialog.button(startButton));
        dialog.show(stage);
    }

    private void stage_Two(DungeonCoder g) {
        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
        TextButton startButton = new TextButton("Start", skin, "default");
        TextButton cancelButton = new TextButton("Cancel", skin, "default");
        Dialog dialog = new Dialog("Stage 2",skin);
        dialog.getBackground().setMinWidth(250);//size of the dialog
        dialog.getBackground().setMinHeight(600);



        startButton.setSize(80,30);
        startButton.setPosition(800,20);

        cancelButton.setSize(100,30);
        cancelButton.setPosition(900,20);

        dialog.addActor(startButton);
        dialog.addActor(cancelButton);


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageTwo(game);

            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToInstructionalMode(game);
            }
        });

        String str2 = ("Objective \n" +
                "Today, we're discussing data types.\n\n" +
                "Task\n" +
                "Complete the code in the editor.\n" +
                "1. Declare 3 variables: one of type int, one of type double, and one of type String.\n" +
                "2. Read 3 lines of input from stdin (according to the sequence given in the Input \n" +
                "Format section below) and initialize your  variables.\n" +
                "3. Use the  operator to perform the following operations: \n" +
                "   Print the sum of  plus your int variable on a new line.\n" +
                "   Print the sum of  plus your double variable to a scale of one decimal place on a new line.\n" +
                "   Concatenate  with the string you read as input and print the result on a new line.\n\n" +
                "Input Format\n" +
                "The first line contains an integer that you must sum with i\n" +
                "The second line contains a double that you must sum with d\n" +
                "The third line contains a string that you must concatenate with s\n\n" +
                "Output Format\n" +
                "Print the sum of both integers on the fist line, the sum of both doubles (scaled to 1 decimal\n" +
                "place) on the second line, and then the two concatenated strings on the third line)\n\n" +
                "Difficulty: One Dungeon");

        dialog.text(str2);
        dialog.show(stage);
    }

    private void stage_Three(DungeonCoder g) {
        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
        TextButton startButton = new TextButton("Start", skin, "default");
        TextButton cancelButton = new TextButton("Cancel", skin, "default");
        Dialog dialog = new Dialog("Stage 3",skin);
        dialog.getBackground().setMinWidth(250);//size of the dialog
        dialog.getBackground().setMinHeight(600);

        startButton.setSize(80,30);
        startButton.setPosition(970,20);

        cancelButton.setSize(100,30);
        cancelButton.setPosition(1060,20);

        dialog.addActor(startButton);
        dialog.addActor(cancelButton);


        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageThree(game);

            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToInstructionalMode(game);
            }
        });


        String str3 = "Objective\nIn this challenge, we review some basic concepts that will get you started with this module.\n\n" +
                "Task\n" +
                "To complete this challenge, you must save a line of input from stdin to a variable, print Hello, World.\n" +
                "and finally print the value of your variable on the next line.\n\n" +
                "Sample Input\n" +
                "Welcome to Dungeon Coding!\n\n" +
                "Sample Output\n" +
                "Hello, World.\n" +
                "Welcome to Dungeon Coding!\n\n" +
                "Explanation\n" +
                "On the first line, we print the string literal Hello, World.. On the second line, we print the contents of the \nvariable which, for this sample case," +
                "happens to be Welcome to Dungeon Coding!. If you do not print the \nvariable's contents to stdout, you will not pass the hidden test case.\n\n" +
                "Difficulty: One Dungeon";
        dialog.text(str3);
        dialog.show(stage);
    }
    private void returnToInstructionalMode(DungeonCoder g){
        g.setScreen(new InstructionalMode(g));
    }

    private void stageOne(DungeonCoder g) {
        g.setScreen(new StageOne(g));
    }

    private void stageTwo(DungeonCoder g) {
        g.setScreen(new StageTwo(g));
    }

    private void stageThree(DungeonCoder g) {
        g.setScreen(new StageThree(g));
    }
}
