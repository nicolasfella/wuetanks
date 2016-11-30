package de.uniwuerzburg.battletanks;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.uniwuerzburg.battletanks.entity.Player;
import de.uniwuerzburg.battletanks.screens.EndScreen;
import de.uniwuerzburg.battletanks.screens.GameScreen;
import de.uniwuerzburg.battletanks.screens.MenuScreen;

public class BattleTanks extends Game {
	
	private static BattleTanks instance;

	private static TextureAtlas atlas;
	private static Preferences prefs;

	private static MenuScreen menu;
	private static GameScreen game;
	private static EndScreen end;
	
	public BattleTanks() {
		instance = this;
	}
	
	@Override
	public void create() {

		prefs = Gdx.app.getPreferences("BattleTanks_Preferences");

		// setDefaultPrefs();

		Gdx.graphics.setWindowedMode(prefs.getInteger("window_width", 1024), prefs.getInteger("window_height", 768));

		atlas = new TextureAtlas(Gdx.files.internal(prefs.getString("texture_atlas_path", "textures/textures.atlas")));

		BattleTanks.showMenu();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

		if(menu!=null){
			menu.dispose();
		}
		if(game!=null){
			game.dispose();
		}
		if(end!=null){
			end.dispose();
		}
		atlas.dispose();
		super.dispose();

	}

	public static TextureAtlas getTextureAtlas() {
		return atlas;
	}

	public static Preferences getPreferences() {
		return prefs;
	}

	private void setDefaultPrefs() {
		prefs.putInteger("window_width", 1024);
		prefs.putInteger("window_height", 768);
		prefs.putString("texture_atlas_path", "textures/textures.atlas");
		prefs.putInteger("default_time", 60);
		prefs.putString("uiskin", "data/uiskin.json");
		prefs.putString("default_map", "maps/TestMap.tmx");
		prefs.putString("title", "Battletanks");
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

	public static void showMenu(){
		if(menu==null){
			menu = new MenuScreen();
		}
		menu.reset();
		
		instance.setScreen(menu);
		
	}
	
	public static void showGame(float time, FileHandle tiledMapFileHandle, List<Player> players){
		if(game==null){
			game = new GameScreen();
		}
		game.reset(time, tiledMapFileHandle, players);
		instance.setScreen(game);
	}
	
	public static void showEnd(List<Player> players){
		if(end==null){
			end = new EndScreen();
		}
		end.reset(players);
		instance.setScreen(end);
	}

	public static GameScreen getGame(){
		return game;
	}

}
