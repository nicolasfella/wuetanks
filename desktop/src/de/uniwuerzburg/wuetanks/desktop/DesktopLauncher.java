package de.uniwuerzburg.wuetanks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.uniwuerzburg.wuetanks.Wuetanks;

public class DesktopLauncher {
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1024;
		config.height = 768;

		new LwjglApplication(new Wuetanks(), config);
	}
}
