package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BattleTanks extends Game {

	private static TextureAtlas atlas;
	
	@Override
	public void create () {
		atlas = new TextureAtlas(Gdx.files.internal("textures/textures.atlas"));

		setScreen(new MenuScreen(this));
		//setScreen(new GameScreen(this, 0, null));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		atlas.dispose();
		super.dispose();
	}

	public static TextureAtlas getTextureAtlas(){
		return atlas;
	}
}
