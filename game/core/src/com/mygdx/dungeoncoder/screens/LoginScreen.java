package com.mygdx.dungeoncoder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.utils.ClientConnection;
import com.sun.deploy.util.SessionState;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class LoginScreen implements Screen {

    private DungeonCoder game;
    private Stage stage;
    private TextField txfUsername;
    private final TextField txfPassword;
    private Skin textSkin;
    private Pinger ping;
    public LoginScreen(DungeonCoder g) {
        game = g;
        stage = new Stage(new ScalingViewport(Scaling.fit, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT)));
        ping = new Pinger();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("comic-ui.json"));

        // creating buttons
        TextButton btnLogin = new TextButton("Login", skin);
        btnLogin.setPosition(750,200);
        btnLogin.setSize(150,60);

        TextButton btnCreateAcc = new TextButton("Create Account", skin);
        btnCreateAcc.setPosition(350,200);
        btnCreateAcc.setSize(300,60);

        btnCreateAcc.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://18.221.243.28/CreateAccount.html");
            }
        });
        textSkin = new Skin(Gdx.files.internal("UIElements/test.json"));

        // creating text fields
        txfUsername = new TextField("TestUser", textSkin);
        txfUsername.setPosition(400, 360);
        txfUsername.setSize(450,40);

        txfPassword = new TextField("NewPass", textSkin);
        txfPassword.setPasswordCharacter('*');
        txfPassword.setPasswordMode(true);
        txfPassword.setPosition(400, 280);
        txfPassword.setSize(450,40);

        // creating labels
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("comic-sans.fnt"));
        labelStyle.font = myFont;

        Label lblGameLogo = new Label("DUNGEON CODER", labelStyle);
        lblGameLogo.setPosition(360, 500);
        lblGameLogo.setFontScale(2);
        /*lblGameLogo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/ryanteo96/dungeon-coder");
            }
        });*/

        Label lblUsername = new Label("Username", labelStyle);
        lblUsername.setPosition(400, 400);

        Label lblPassword = new Label("Password", labelStyle);
        lblPassword.setPosition(400, 320);


        // button listeners.
        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnLoginClicked(game);
            }
        });


        // adding stage actors.
        stage.addActor(txfUsername);
        stage.addActor(txfPassword);
        stage.addActor(btnLogin);
        stage.addActor(btnCreateAcc);
        stage.addActor(lblGameLogo);
        stage.addActor(lblUsername);
        stage.addActor(lblPassword);


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

    @Override
    public void show() {

    }

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

    public void btnLoginClicked(DungeonCoder g) {
        shareVariable.connect = new ClientConnection();
        if (shareVariable.connect.requestLogin(txfUsername.getText(), txfPassword.getText())) {
            shareVariable.connected = true;
            ping.start();
            System.out.println(ping);
            g.setScreen(new MainMenuScreen(g));
            //System.out.println(shareVariable.connect);
        } else {
            System.out.println("Login Failed");
            shareVariable.connected = false;
        }

    }

    public void btnBackClicked(DungeonCoder g){
         g.setScreen(new SplashScreen(g));
    }

}
