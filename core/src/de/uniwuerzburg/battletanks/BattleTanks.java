package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Game;

public class BattleTanks extends Game {
	
	
	@Override
	public void create () {

		//setScreen(new GameScreen(this, 0, null));
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		
	}
}
