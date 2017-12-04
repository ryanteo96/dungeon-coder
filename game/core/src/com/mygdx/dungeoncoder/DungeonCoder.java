package com.mygdx.dungeoncoder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.dungeoncoder.screens.LoginScreen;
import com.mygdx.dungeoncoder.screens.MainMenuScreen;
import com.mygdx.dungeoncoder.screens.SplashScreen;
import com.mygdx.dungeoncoder.screens.TaskOne;

public class DungeonCoder extends Game {
	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 211;

	/* WARNING Using AssetManager in a static way can cause issues, especially on Android.
    Instead you may want to pass around Assetmanager to those the classes that need it.*/
	public static AssetManager manager;

	@Override
	public void create() {
    	batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Mario/music/mario_music.ogg",Music.class);
		manager.load("Mario/sounds/coin.wav",Sound.class);
		manager.load("Mario/sounds/bump.wav",Sound.class);
		manager.load("Mario/sounds/breakblock.wav",Sound.class);
		manager.load("Mario/sounds/powerup_spawn.wav",Sound.class);
		manager.load("Mario/sounds/powerup.wav", Sound.class);
		manager.load("Mario/sounds/powerdown.wav", Sound.class);
		manager.load("Mario/sounds/stomp.wav", Sound.class);
		manager.load("Mario/sounds/mariodie.wav", Sound.class);
		manager.load("UIElements/Animation/pain.mp3",Sound.class);
		manager.load("UIElements/Animation/footstep.wav",Music.class);
		manager.load("UIElements/Animation/backgroundmusic.mp3",Music.class);
		manager.load("UIElements/Animation/stagecomplete.mp3",Sound.class);
		manager.load("UIElements/Animation/robottalking.wav",Music.class);
		manager.load("UIElements/Animation/jump.wav",Sound.class);
		manager.load("UIElements/Animation/achievement.mp3",Sound.class);
		manager.load("UIElements/Animation/npc2.wav",Music.class);
		manager.load("UIElements/Animation/questcompleted.wav",Music.class);
		manager.load("UIElements/Animation/fail.mp3",Music.class);
		manager.load("UIElements/Animation/gamevictory.wav",Music.class);
		manager.finishLoading(); //Asynchronous loading, blocks everything and ask every assets to load first
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
		batch.dispose();
		manager.dispose();
	}

// testing testing

}
