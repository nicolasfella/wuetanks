package de.uniwuerzburg.battletanks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.uniwuerzburg.battletanks.BattleTanks;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		System.out.println("Hallo von Nico");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		
		//TODO decide on with and height, fullscreen?
		config.width = 800;
		config.height = 600;
		
		
		new LwjglApplication(new BattleTanks(), config);
	}
}
