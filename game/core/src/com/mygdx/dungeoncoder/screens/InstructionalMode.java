package com.mygdx.dungeoncoder.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
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
        //Dialog dialog = new Dialog("Stage 1",skin);
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);
        // Gdx.input.setInputProcessor(stage = new Stage());
        skin = new Skin(Gdx.files.internal("dialogSkins/plain-james-ui.json"));

        Dialog dialog = new Dialog("Stage 1",skin);
        dialog.getBackground().setMinWidth(300);//size of the dialog
        dialog.getBackground().setMinHeight(500);
        dialog.button("Start");
        dialog.button("Cancel");
        dialog.text("Objective\n" +
                "In this challenge, we review some basic concepts that will get you started with this module.\n\n" +
                "Task\n" +
                "To complete this challenge, you must save a line of input from stdin to a variable, print Hello, World.\n on a single line," +
                "and finally print the value of your variable on the next line.\n\n" +
                "Sample Input\n" +
                "Welcome to Dungeon Coding!\n\n" +
                "Sample Output\n" +
                "Hello, World.\n" +
                "Welcome to Dungeon Coding!\n\n" +
                "Explanation\n" +
                "On the first line, we print the string literal Hello, World.. On the second line, we print \nthe contents of the  variable which, for this sample case, " +
                "happens to be Welcome to Dungeon Coding!. \nIf you do not print the variable's contents to stdout, you will not pass the hidden test case.\n\n" +
                "Difficulty: One Dungeon");
        dialog.show(stage);

        /*new Dialog("Stage 1", skin) {

            {
                text("Objective\n" +
                        "In this challenge, we review some basic concepts that will get you started with this module.\n\n" +
                        "Task\n" +
                        "To complete this challenge, you must save a line of input from stdin to a variable, print Hello, World.\n on a single line," +
                        "and finally print the value of your variable on the next line.\n\n" +
                        "Sample Input\n" +
                        "Welcome to Dungeon Coding!\n\n" +
                        "Sample Output\n" +
                        "Hello, World.\n" +
                        "Welcome to Dungeon Coding!\n\n" +
                        "Explanation\n" +
                        "On the first line, we print the string literal Hello, World.. On the second line, we print \nthe contents of the  variable which, for this sample case, " +
                        "happens to be Welcome to Dungeon Coding!. \nIf you do not print the variable's contents to stdout, you will not pass the hidden test case.\n\n" +
                        "Difficulty: One Dungeon");

                TextButton btnStart = new TextButton("Start",skin);
                btnStart.setPosition(100,200);
                btnStart.setSize(100,200);

                stage.addActor(btnStart);

                button("Cancel", "glad you stay");
            }

            @Override
            protected void result(final Object object) {
                new Dialog("", skin) {

                    {
                        text(object.toString());
                        button("OK");
                    }

                }.show(stage);
            }

        }.show(stage);
*/

    }
}
