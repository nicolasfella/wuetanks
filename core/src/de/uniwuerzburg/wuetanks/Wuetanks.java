package de.uniwuerzburg.wuetanks;

import java.util.List;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.uniwuerzburg.wuetanks.entity.Player;
import de.uniwuerzburg.wuetanks.screens.EndScreen;
import de.uniwuerzburg.wuetanks.screens.ErrorScreen;
import de.uniwuerzburg.wuetanks.screens.GameScreen;
import de.uniwuerzburg.wuetanks.screens.MenuScreen;

/**
 * Central class. Only one instance at a time
 * 
 */
public class Wuetanks extends Game {

	private static Wuetanks instance;
	private TextureAtlas atlas;
	private Preferences prefs;
	private MenuScreen menu;
	private GameScreen game;
	private EndScreen end;
	private ErrorScreen error;

	/**
	 * Creates a new instance and makes it the current instance. Only one
	 * instance at a time is possible
	 */
	public Wuetanks() {
		Wuetanks.instance = this;
	}

	/**
	 * Called when the application is launched
	 */
	@Override
	public void create() {

		prefs = Gdx.app.getPreferences("Wuetanks_Preferences");

		// setDefaultPrefs();

		Gdx.graphics.setWindowedMode(prefs.getInteger("window_width", 1024), prefs.getInteger("window_height", 768));

		atlas = new TextureAtlas(Gdx.files.internal(prefs.getString("texture_atlas_path", "textures/textures.atlas")));

		showMenu();
	}

	/**
	 * Called each frame
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Called when the application is closed
	 */
	@Override
	public void dispose() {

		if (menu != null) {
			menu.dispose();
		}
		if (game != null) {
			game.dispose();
		}
		if (end != null) {
			end.dispose();
		}
		if (error != null) {
			error.dispose();
		}
		atlas.dispose();
		super.dispose();

	}

	/**
	 * 
	 * @return The currently loaded TextureAtlas
	 */
	public TextureAtlas getTextureAtlas() {
		return atlas;
	}

	/**
	 * 
	 * @return The games Preferences
	 */
	public Preferences getPreferences() {
		return prefs;
	}

	// Writes default values to the Preferences file. Should only be called on
	// user request
	private void setDefaultPrefs() {
		prefs.putInteger("window_width", 1024);
		prefs.putInteger("window_height", 768);
		prefs.putString("texture_atlas_path", "textures/textures.atlas");
		prefs.putInteger("default_time", 60);
		prefs.putString("uiskin", "data/uiskin.json");
		prefs.putString("default_map", "maps/TestMap.tmx");
		prefs.putString("title", "Wuetanks");
		prefs.putInteger("player_width", 40);
		prefs.putInteger("player_height", 40);
		prefs.putInteger("gun_width", 10);
		prefs.putInteger("gun_height", 40);
		prefs.putInteger("bullet_width", 12);
		prefs.putInteger("bullet_height", 26);
		prefs.putInteger("bullet_speed", 450);
		prefs.putInteger("player_speed", 150);
		prefs.putString("shot_sound", "shot.wav");
		prefs.putInteger("game_font_size", 14);
		prefs.getInteger("spawn_offset", 20);
		prefs.putString("game_font", "fonts/Vera.ttf");
		prefs.putString("background", "background.png");
		prefs.putString("background_music", "music.mp3");
		prefs.flush();
	}

	/**
	 * Launches the menu screen
	 */
	public void showMenu() {
		if (menu == null) {
			menu = new MenuScreen();
		} else {
			menu.reset();
		}
		instance.setScreen(menu);
	}

	/**
	 * Launches the game
	 * 
	 * @param time
	 *            The games length in seconds
	 * @param tiledMapFileHandle
	 *            Filehandle of the map to be loaded
	 * @param players
	 *            The games players
	 */
	public void showGame(float time, FileHandle tiledMapFileHandle, List<Player> players) {

		if (game == null) {
			game = new GameScreen(time, tiledMapFileHandle, players);
		} else {
			game.reset(time, tiledMapFileHandle, players);
		}
		instance.setScreen(game);
	}

	/**
	 * Launches the endscreen
	 * 
	 * @param players
	 *            List of players for ranking
	 */
	public void showEnd(List<Player> players) {
		if (end == null) {
			end = new EndScreen(players);
		} else {
			end.reset(players);
		}
		instance.setScreen(end);
	}

	public void showError(String message) {
		if (error == null) {
			error = new ErrorScreen(message);
		} else {
			error.reset(message);
		}
		instance.setScreen(error);
	}

	/**
	 * Returns the current GameScreen instance
	 */
	public GameScreen getGame() {
		return game;
	}

	/**
	 * Returns the current MenuScreen instance
	 */
	public MenuScreen getMenu() {
		return menu;
	}

	/**
	 * Returns the current EndScreen instance
	 */
	public EndScreen getEndScreen() {
		return end;
	}

	public ErrorScreen getErrorScreen() {
		return error;
	}

	public static Wuetanks getInstance() {
		return instance;
	}

	public Audio getAudio() {
		return Gdx.audio;
	}

	public Files getFiles() {
		return Gdx.files;
	}

}
