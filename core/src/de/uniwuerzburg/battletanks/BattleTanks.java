package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

import de.uniwuerzburg.battletanks.screens.MenuScreen;

public class BattleTanks extends Game {

    private static TextureAtlas atlas;
    private static Preferences prefs;

    private static List<Screen> screens;

    @Override
    public void create() {

        screens = new ArrayList<>();

        prefs = Gdx.app.getPreferences("BattleTanks_Preferences");

       // setDefaultPrefs();

        Gdx.graphics.setWindowedMode(prefs.getInteger("window_width", 1024), prefs.getInteger("window_height", 768));

        atlas = new TextureAtlas(Gdx.files.internal(prefs.getString("texture_atlas_path", "textures/textures.atlas")));

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

        for(Screen s: screens){
            s.dispose();
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

    private void setDefaultPrefs(){
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
        prefs.flush();
    }

    public static void addScreen(Screen s){
        screens.add(s);
    }

}
