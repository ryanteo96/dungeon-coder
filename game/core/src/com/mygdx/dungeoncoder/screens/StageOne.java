package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.dungeoncoder.DungeonCoder;

import static com.badlogic.gdx.utils.Scaling.fit;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;


public class StageOne implements Screen {
    private DungeonCoder game;
    private Stage stage;
    private Skin backButtonSkin;
    Object[] listEntries = {"This is a list entry1", "And another one1", "The meaning of life1", "Is hard to come by1",
            "This is a list entry2", "And another one2", "The meaning of life2", "Is hard to come by2", "This is a list entry3",
            "And another one3", "The meaning of life3", "Is hard to come by3", "This is a list entry4", "And another one4",
            "The meaning of life4", "Is hard to come by4", "This is a list entry5", "And another one5", "The meaning of life5",
            "Is hard to come by5"};
    Skin skin;
    Texture texture1;
    Texture texture2;
    Label fpsLabel;


    public StageOne (DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        Gdx.input.setInputProcessor(stage);
        create();
        createBack();
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

    public void create(){
        skin = new Skin(Gdx.files.internal("UIElements/test.json"));
        texture1 = new Texture(Gdx.files.internal("UIElements/badlogicsmall.jpg"));
        texture2 = new Texture(Gdx.files.internal("UIElements/badlogic.jpg"));
        TextureRegion image = new TextureRegion(texture1);
        TextureRegion imageFlipped = new TextureRegion(image);
        imageFlipped.flip(true, true);
        TextureRegion image2 = new TextureRegion(texture2);
        //stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, new PolygonSpriteBatch());
        //stage = new Stage(new ScreenViewport());
        //Gdx.input.setInputProcessor(stage);

        // Group.debug = true;

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.get(Button.ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(image);
        style.imageDown = new TextureRegionDrawable(imageFlipped);
        ImageButton iconButton = new ImageButton(style);

        Button buttonMulti = new TextButton("Multi\nLine\nToggle", skin, "toggle");
        Button imgButton = new Button(new Image(image), skin);
        Button imgToggleButton = new Button(new Image(image), skin, "toggle");

        Label myLabel = new Label("this is some text.", skin);
        myLabel.setWrap(true);

        Table t = new Table();
        t.row();
        t.add(myLabel);

        t.layout();

        final CheckBox checkBox = new CheckBox(" Continuous rendering", skin);
        checkBox.setChecked(true);
        final Slider slider = new Slider(0, 10, 1, false, skin);
        slider.setAnimateDuration(0.3f);
        TextField textfield = new TextField("", skin);
        textfield.setMessageText("Click here!");
        textfield.setAlignment(Align.center);
        final SelectBox selectBox = new SelectBox(skin);
        //selectBox.setAlignment(Align.right);
        //selectBox.getList().setAlignment(Align.right);
        selectBox.getStyle().listStyle.selection.setRightWidth(10);
        selectBox.getStyle().listStyle.selection.setLeftWidth(20);
        selectBox.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println(selectBox.getSelected());
            }
        });
        selectBox.setItems("Android1", "Windows1 long text in item", "Linux1", "OSX1", "Android2", "Windows2", "Linux2", "OSX2",
                "Android3", "Windows3", "Linux3", "OSX3", "Android4", "Windows4", "Linux4", "OSX4", "Android5", "Windows5", "Linux5",
                "OSX5", "Android6", "Windows6", "Linux6", "OSX6", "Android7", "Windows7", "Linux7", "OSX7");
        selectBox.setSelected("Linux6");
        Image imageActor = new Image(image2);
        ScrollPane scrollPane = new ScrollPane(imageActor);

        List list = new List(skin);
        list.setItems(listEntries);
        list.getSelection().setMultiple(true);
        list.getSelection().setRequired(false);


        // list.getSelection().setToggle(true);
        ScrollPane scrollPane2 = new ScrollPane(list, skin);
        scrollPane2.setFlickScroll(false);
        Label minSizeLabel = new Label("minWidth cell", skin); // demos SplitPane respecting widget's minWidth
        Table rightSideTable = new Table(skin);
        rightSideTable.add(minSizeLabel).growX().row();
        rightSideTable.add(scrollPane2).grow();
        SplitPane splitPane = new SplitPane(scrollPane, rightSideTable, false, skin, "default-horizontal");
        fpsLabel = new Label("fps:", skin);

        // configures an example of a TextField in password mode.
        final Label passwordLabel = new Label("Textfield in password mode: ", skin);
        final TextField passwordTextField = new TextField("", skin);
        passwordTextField.setMessageText("password");
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);

        buttonMulti.addListener(new TextTooltip(
                "This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip!",
                skin));
        Table tooltipTable = new Table(skin);
        tooltipTable.pad(10).background("default-round");
        tooltipTable.add(new TextButton("Fancy tooltip!", skin));
        imgButton.addListener(new Tooltip(tooltipTable));

        // window.debug();
        Window window = new Window("Dialog", skin);
        window.getTitleTable().add(new TextButton("X", skin)).height(window.getPadTop());
        window.setPosition(450, 150);
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(iconButton);
        window.add(buttonMulti);
        window.add(imgButton);
        window.add(imgToggleButton);
        window.row();
        window.add(checkBox);
        window.add(slider).minWidth(100).fillX().colspan(2);
        window.row();
        window.add(selectBox).maxWidth(100);
        window.add(textfield).minWidth(100).expandX().fillX().colspan(5);
        window.row();
        window.add(splitPane).fill().expand().colspan(4).maxHeight(200);
        window.row();
        window.add(passwordLabel).colspan(2);
        window.add(passwordTextField).minWidth(100).expandX().fillX().colspan(2);
        window.row();
        window.add(fpsLabel).colspan(4);
        window.pack();
        window.setDebug(false);
        // stage.addActor(new Button("Behind Window", skin));
        stage.addActor(window);

        textfield.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });

        slider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("UITest", "slider: " + slider.getValue());
            }
        });

        iconButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                new Dialog("Some Dialog", skin, "dialog") {
                    protected void result (Object object) {
                        System.out.println("Chosen: " + object);
                    }
                }.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Input.Keys.ENTER, true)
                        .key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        checkBox.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(checkBox.isChecked());
            }
        });
    }

    private void btnBackClicked(DungeonCoder g) {
        g.setScreen(new InstructionalMode(g));
    }
    @Override
    public void show() {
        System.out.println("yes you are in stage one");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(172/255f, 115/255f, 57/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

        fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        texture1.dispose();
        texture2.dispose();
    }

}
