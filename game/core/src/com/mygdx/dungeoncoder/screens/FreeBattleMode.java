package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import com.mygdx.dungeoncoder.DungeonCoder;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import static com.mygdx.dungeoncoder.values.DefaultValues.GROUND_BIT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class FreeBattleMode extends ApplicationAdapter implements Screen {

    private ScrollPane scrollPane;
    private List<String> list;
    private SpriteBatch batcher;
    private TextureAtlas atlas;

    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    private Skin skin;
    private TextButton uploadButton;
    private TextButton downloadButton;
    private TextButton sortButton;
    private Object[] listEntries = {};
    private int result;
    private int sort = 0;

    private TextField textArea;


    public FreeBattleMode (DungeonCoder g) {
        game = g;

        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));

        Gdx.input.setInputProcessor(stage);
        System.out.println("It has gone in free battle mode");
        createTable(sort);
        createUploadButton();
        createDownloadButton();
        createSortButton();
        createBack();
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
        skin.dispose();
    }


    private void createTable(int order) {
        atlas = new TextureAtlas(Gdx.files.internal("dialogSkins/plain-james-ui.atlas"));
        skin = new Skin(Gdx.files.internal(("dialogSkins/plain-james-ui.json")));
        batcher = new SpriteBatch();
        list = new List<String>(skin);

        // requesting level list.

        ArrayList<String> levels = shareVariable.connect.requestLevelList();

        if (order == 0) {
            sort = 0;
            Collections.sort(levels,new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
        } else {
            sort = 1;
            Collections.sort(levels, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            Collections.sort(levels, Collections.<String>reverseOrder());
        }


        String[] strings = new String[levels.size()];
        for (int i = 0; i < levels.size(); i++) {
            System.out.println(levels.get(i));
        }

        for (int i = 0, k = 0; i < levels.size(); i++) {
            strings[k++] = "Level: " + levels.get(i);
        }

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("comic-sans.fnt"));
        labelStyle.font = myFont;

        Label lblLevelList = new Label("LEVELS", labelStyle);
        lblLevelList.setPosition(120, 600);
        lblLevelList.setFontScale(1);
        stage.addActor(lblLevelList);

        /*String[] strings = new String[30];
        for (int i = 0, k = 0; i < 30; i++) {
            strings[k++] = "TestFile" + i;
        }

        if (order == 0) {
            sort = 0;
            for (int i = 0, k = 0; i < 30; i++) {
                strings[k++] = "TestFile" + i;
            }
        } else {
            sort = 1;
            for (int i = 29, k = 0; i >= 0; i--) {
                strings[k++] = "TestFile" + i;
            }
        }*/

        list.setItems(strings);
        Texture background = new Texture(Gdx.files.internal("UIElements/greybackground.png"));
        TextureRegion backgroundRegion = new TextureRegion(background);
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(backgroundRegion);
        Image backgroundImage = new Image(backgroundDrawable);

        //backgroundImage.setSize(500,500);
        backgroundImage.setBounds(0,0,710,530);
        backgroundImage.setPosition(107,50);
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        scrollPane = new ScrollPane(list);
        scrollPane.setBounds(0, 0, 470, 350);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition(110, 50);
        scrollPane.setTransform(true);
        scrollPane.setScale(1.5f);
        scrollPane.setColor(Color.WHITE);
        //scrollPane.setColor(145/255f,145/255f,145/255f,1);
        stage.addActor(backgroundImage);
        stage.addActor(scrollPane);
        Gdx.input.setInputProcessor(stage);
    }

    private void createBack() {
        backButtonSkin = new Skin(Gdx.files.internal("comic-ui.json"));
        TextButton btnBack = new TextButton("Back", backButtonSkin);
        btnBack.setPosition(0, 600);
        btnBack.setSize(100, 100);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                try {
                    dispose();
                    game.setScreen(new MainMenuScreen(game));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
        stage.addActor(btnBack);
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
                    System.out.println("The path: " + fc.getSelectedFile().getAbsoluteFile().getAbsolutePath());
                    String filePath = fc.getSelectedFile().getAbsoluteFile().getAbsolutePath();
                    int indexOfDot = filePath.lastIndexOf('.');
                    String fileName = "";
                    int filenameStart = 0;
                    for(int i = indexOfDot - 1; i >= 0; i--){
                        if(!Character.isLetterOrDigit(filePath.charAt(i))){
                            filenameStart = i;
                            break;
                        }
                    }
                    fileName = filePath.substring(filenameStart + 1);
                    System.out.println("fileName is: "+ fileName);
                    File f = new File(fc.getSelectedFile().getAbsolutePath());
                    shareVariable.connect.uploadLevel(fileName, f);
                    result = JFileChooser.APPROVE_OPTION;

                    //System.out.println(result);
                    //System.out.println(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        window.add(BorderLayout.NORTH, topPanel);
        topPanel.add(openFileChooser);
        window.setSize(400, 100);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }

    private void createDownloadButton() {
        textArea = new TextField("", skin);
        textArea.setPosition(830,400);
        textArea.setSize(150,50);
        textArea.setAlignment(Align.center);
        textArea.setMessageText("Type in here!");
        stage.addActor(textArea);
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        downloadButton = new TextButton("DOWNLOAD", skin);
        downloadButton.setPosition(1000, 400);
        downloadButton.setSize(130, 50);
        downloadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                shareVariable.connect.requestCustomLevel(textArea.getText());
            }
        });
        stage.addActor(downloadButton);
    }

    private void createSortButton() {
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        sortButton = new TextButton("SORT", skin);
        sortButton.setPosition(1000, 300);
        sortButton.setSize(130, 50);
        sortButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
               scrollPane.remove();
                if (sort == 0) {
                    createTable(1);
                } else {
                    createTable(0);
                }

            }
        });
        stage.addActor(sortButton);

    }

    /*
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



*/
}
