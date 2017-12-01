package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.values.DefaultValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class FreeBattleMode extends ApplicationAdapter implements Screen {

    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private Skin skin;
    private TextButton uploadButton;
    private   Object[] listEntries = {};
    private boolean open = false;
    private int result;

    public FreeBattleMode (DungeonCoder g) {
        game = g;

        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);

        createBack();
        createUploadButton();
        createTable();
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
        stage.dispose();
    }
    private void createTable(){
        if(open){
            listEntries[0] = DefaultValues.username;
            listEntries[1] = DefaultValues.username;
            listEntries[2] = DefaultValues.username;
            listEntries[3] = DefaultValues.username;
            System.out.println("OPEN!");
        }

        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        Table table = new Table(skin);
        com.badlogic.gdx.scenes.scene2d.ui.List list = new List(skin);
        list.setItems(listEntries);
        ScrollPane scrollPane = new ScrollPane(list, skin);
        scrollPane.setSize(0,0);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true,false);
        table.add().growX().row();
        table.add(scrollPane).grow();
        table.setSize(700,600);
        table.setPosition(130,50);
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
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(btnBack);
    }

    public void createFileChooser () throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame window = new JFrame("Upload TMX file");
        JPanel topPanel = new JPanel();
        final JButton openFileChooser = new JButton("Find your file");
        //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JFileChooser fc = new JFileChooser();
        openFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new java.io.File("user.home"));
                fc.setDialogTitle("Choose your TMX file");
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (fc.showOpenDialog(openFileChooser) == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, fc.getSelectedFile().getAbsolutePath());
                    result = JFileChooser.APPROVE_OPTION;
                }
            }
        });
        window.add(BorderLayout.NORTH, topPanel);
        topPanel.add(openFileChooser);
        window.setSize(400, 100);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }

    private void createUploadButton() {
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        uploadButton = new TextButton("UPLOAD", skin);
        uploadButton.setPosition(1000, 500);
        uploadButton.setSize(130, 50);
        uploadButton.addListener(new ClickListener() {
           @Override
            public void clicked(InputEvent e, float x, float y) {
               try {
                    createFileChooser();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
            }
        });
        stage.addActor(uploadButton);

    }
}
