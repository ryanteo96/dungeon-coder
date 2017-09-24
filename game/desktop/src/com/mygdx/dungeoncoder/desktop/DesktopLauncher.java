package com.mygdx.dungeoncoder.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.GameInputProcessor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Dungeon Coder";
		//config.setFromDisplayMode(displayMode);
		new LwjglApplication(new DungeonCoder(), config);

		GameInputProcessor inputProcessor = new GameInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);

	}
}
