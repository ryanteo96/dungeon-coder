package com.mygdx.dungeoncoder.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.dungeoncoder.DungeonCoder;
import com.mygdx.dungeoncoder.GameInputProcessor;

import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_HEIGHT;
import static com.mygdx.dungeoncoder.values.DefaultValues.VIRTUAL_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Dungeon Coder";
		config.width = VIRTUAL_WIDTH;
		config.height = VIRTUAL_HEIGHT;
		new LwjglApplication(new DungeonCoder(), config);

		GameInputProcessor inputProcessor = new GameInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);

	}
}
