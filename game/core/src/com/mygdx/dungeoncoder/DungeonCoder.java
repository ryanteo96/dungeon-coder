package com.mygdx.dungeoncoder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.dungeoncoder.screens.LoginScreen;
import com.mygdx.dungeoncoder.screens.MainMenuScreen;
import com.mygdx.dungeoncoder.screens.SplashScreen;

public class DungeonCoder extends Game {



    @Override
	public void create() {
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}


// testing testing

}
