package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.ClientConnection;

import java.io.FileNotFoundException;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class InstructionalMode extends ScreenAdapter implements Screen{

    private DungeonCoder game;
    private Stage stage;
    private Skin skin;
    private Skin testSkin;
    private Window window;
    TextButton yesButton;
    Object gObject;

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

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        testSkin.dispose();
    }

    private void createBack() {
        skin = new Skin (Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", skin);
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

    private void btnBackClicked(DungeonCoder g) {
        g.setScreen(new MainMenuScreen(g));
    }


    private void createStages(){
        Texture stage_One = new Texture(Gdx.files.internal("UIElements/task1.png"));
        TextureRegion stage_OneRegion = new TextureRegion(stage_One);
        TextureRegionDrawable stage_OneDrawable = new TextureRegionDrawable(stage_OneRegion);
        Image stage_OneImage = new Image(stage_OneDrawable);
        stage_OneImage.setPosition(100,400);
        stage_OneImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               // if()
               stage_One(game);

            }
        });

        stage.addActor(stage_OneImage);

        Texture stage_Two = new Texture(Gdx.files.internal("UIElements/task2.png"));
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

        Texture stage_Three = new Texture(Gdx.files.internal("UIElements/task3.png"));
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


    private void stage_One(DungeonCoder g){
        Object[] listEntries = {"Objective",
                "In this challenge, we review some basic concepts that will get you started with this module.",
                "===========================================================================================",
                "Task",
                "To complete this challenge, you must save a line of input from stdin to a variable,",
                "print 'Hello, World.' and finally print the value of your variable on the next line.",
                "===========================================================================================",
                "Sample Input",
                "Welcome to Dungeon Coding!",
                "--------------------------------------------------------------------------------------------------------------------------------------------",
                "Sample Output",
                "Hello World",
                "Welcome to Dungeon Coding!",
                "===========================================================================================",
                "Explanation",
                "On the first line, we print the string literal 'Hello, World.' On the second line, we",
                "print the contents of the variable which, for this sample case, happens to be 'Welcome to",
                "Dungeon Coding!'. If you do not print the variable's contents to stdout, you will not",
                "pass the hidden test case.",
                "===========================================================================================",
                "Difficulty: One Dungeon"};

        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
        testSkin = new Skin(Gdx.files.internal("UIElements/test.json"));

        Texture repeat = new Texture(Gdx.files.internal("UIElements/repeat.png"));
        TextureRegion repeatRegion = new TextureRegion(repeat);

        //create Text using lists and scrollpane
        List list = new List(testSkin);
        list.setItems(listEntries);
        ScrollPane scrollPane = new ScrollPane(list, testSkin);
        scrollPane.setSize(0,0);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true,false);
        Table table = new Table(testSkin);
        table.add().growX().row();
        table.add(scrollPane).grow();

        //Repeat Button
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(testSkin.get(Button.ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(repeatRegion);
        ImageButton repeatButton = new ImageButton(style);
        table.add(repeatButton);
        int i = 88;
        char x = (char)i;
        TextButton closeButton = new TextButton(String.valueOf(x), testSkin);
        TextButton startButton = new TextButton("Start", skin, "default");
        TextButton cancelButton = new TextButton("Cancel", skin, "default");
        window = new Window("Task 1", testSkin);
        window.setDebug(false);
        window.getTitleTable().add(closeButton).height(window.getPadTop());
        window.setPosition(300,80);
        //window.defaults().spaceBottom(10);//not sure what does this do
        window.setSize(700,600);
        window.add(scrollPane).colspan(3).left().expand().fillX();
        window.row();
        window.right().bottom();
        window.add(repeatButton).size(50).left();
        window.add(startButton).right().padLeft(465);
        window.add(cancelButton).right();


        yesButton = new TextButton("Yes", testSkin);

        repeatButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor) {

                new Dialog("Task 1", testSkin,"dialog"){
                    protected void result (Object object){
                        gObject = object;
                        System.out.println("Result: "+ object);
                    }
                }.text("Are you sure you want to repeat the task?").button(yesButton, true).button("No",false).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        //when yes is clicked, it repeats the task
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageOne(game);
            }
        });

        //go to task one screen when start is clicked
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageOne(game);

            }
        });


        //cancel button
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });

        //to close the window
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });
        stage.addActor(window);

    }

    private void stage_Two(DungeonCoder g) {
        Object[] listEntries = {"Objective",
                "Explore the Dungeon World",
                "===========================================================================================",
                "Task",
                "Reach the end of the stage with your code input.",
                "",
                "===========================================================================================",
                "Sample Input",
                "moveRight();",
                "moveLeft();",
                "jump();",
                "--------------------------------------------------------------------------------------------------------------------------------------------",
                "Sample Output",
                "The console will print \"You character moved DIRECTION!\".",
                "===========================================================================================",
                "Explanation",
                "You should use the functions provided to move the character and everytime you run the code",
                "your character will move accordingly.",
                "===========================================================================================",
                "Difficulty: Easy"};

        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
        testSkin = new Skin(Gdx.files.internal("UIElements/test.json"));

        Texture repeat = new Texture(Gdx.files.internal("UIElements/repeat.png"));
        TextureRegion repeatRegion = new TextureRegion(repeat);

        //create Text using lists and scrollpane
        List list = new List(testSkin);
        list.setItems(listEntries);
        ScrollPane scrollPane = new ScrollPane(list, testSkin);
        scrollPane.setSize(0,0);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true,false);
        Table table = new Table(testSkin);
        table.add().growX().row();
        table.add(scrollPane).grow();


        //Repeat Button
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(testSkin.get(Button.ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(repeatRegion);
        ImageButton repeatButton = new ImageButton(style);
        table.add(repeatButton);
        int i = 88;
        char x = (char)i;
        TextButton closeButton = new TextButton(String.valueOf(x), testSkin);
        TextButton startButton = new TextButton("Start", skin, "default");
        TextButton cancelButton = new TextButton("Cancel", skin, "default");
        window = new Window("Task 2", testSkin);
        window.setDebug(false);
        window.getTitleTable().add(closeButton).height(window.getPadTop());
        window.setPosition(300,80);
        //window.defaults().spaceBottom(10);//not sure what does this do
        window.setSize(700,600);
        window.add(scrollPane).colspan(3).left().expand().fillY();
        window.row();
        window.right().bottom();
        window.add(repeatButton).size(50).left();
        window.add(startButton).right().padLeft(465);
        window.add(cancelButton).right();


        yesButton = new TextButton("Yes", testSkin);

        repeatButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor) {

                new Dialog("Task 2", testSkin,"dialog"){
                    protected void result (Object object){
                        gObject = object;
                        System.out.println("Result: "+ object);
                    }
                }.text("Are you sure you want to repeat the task?").button(yesButton, true).button("No",false).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        //when yes is clicked, it repeats the task
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    stageTwo(game);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        //go to task two screen when start is clicked
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    stageTwo(game);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


        //cancel button
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });

        //to close the window
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });
        stage.addActor(window);
    }

    private void stage_Three(DungeonCoder g) {
        Object[] listEntries = {"Objective",
                "TASK 3 OBJECTIVE",
                "===========================================================================================",
                "Task",
                "TASK 3 TASK",
                "",
                "===========================================================================================",
                "Sample Input",
                "TASK 3 SAMPLE INPUT",
                "--------------------------------------------------------------------------------------------------------------------------------------------",
                "Sample Output",
                "TASK 3 SAMPLE OUTPUT",
                "===========================================================================================",
                "Explanation",
                "TASK 3 EXPLANATION",
                "===========================================================================================",
                "Difficulty: TASK 3 DIFFICULTY"};


        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));
        testSkin = new Skin(Gdx.files.internal("UIElements/test.json"));

        Texture repeat = new Texture(Gdx.files.internal("UIElements/repeat.png"));
        TextureRegion repeatRegion = new TextureRegion(repeat);

        //create Text using lists and scrollpane
        List list = new List(testSkin);
        list.setItems(listEntries);
        ScrollPane scrollPane = new ScrollPane(list, testSkin);
        scrollPane.setSize(0,0);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true,false);
        Table table = new Table(testSkin);
        table.add().growX().row();
        table.add(scrollPane).grow();


        //Repeat Button
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(testSkin.get(Button.ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(repeatRegion);
        ImageButton repeatButton = new ImageButton(style);
        table.add(repeatButton);
        int i = 88;
        char x = (char)i;
        TextButton closeButton = new TextButton(String.valueOf(x), testSkin);
        TextButton startButton = new TextButton("Start", skin, "default");
        TextButton cancelButton = new TextButton("Cancel", skin, "default");
        window = new Window("Task 3", testSkin);
        window.setDebug(false);
        window.getTitleTable().add(closeButton).height(window.getPadTop());
        window.setPosition(300,80);
        //window.defaults().spaceBottom(10);//not sure what does this do
        window.setSize(700,600);
        window.add(scrollPane).colspan(3).left().expand().fillY();
        window.row();
        window.right().bottom();
        window.add(repeatButton).size(50).left();
        window.add(startButton).right().padLeft(465);
        window.add(cancelButton).right();


        yesButton = new TextButton("Yes", testSkin);

        repeatButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor) {

                new Dialog("Task 3", testSkin,"dialog"){
                    protected void result (Object object){
                        gObject = object;
                        System.out.println("Result: "+ object);
                    }
                }.text("Are you sure you want to repeat the task?").button(yesButton, true).button("No",false).
                        key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        //when yes is clicked, it repeats the task
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageThree(game);
            }
        });

        //go to task three screen when start is clicked
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stageThree(game);

            }
        });


        //cancel button
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });

        //to close the window
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });
        stage.addActor(window);
    }
    private void returnToInstructionalMode(DungeonCoder g){
        g.setScreen(new InstructionalMode(g));
    }

    private void stageOne(DungeonCoder g) {
        g.setScreen(new TaskOne(g));
    }

    private void stageTwo(DungeonCoder g) throws FileNotFoundException {
        g.setScreen(new TaskTwo(g));
    }

    private void stageThree(DungeonCoder g) {
        g.setScreen(new TaskThree(g));
    }
}
