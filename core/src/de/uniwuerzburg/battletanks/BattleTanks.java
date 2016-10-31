package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Game;

public class BattleTanks extends Game {
	
	
	@Override
	public void create () {
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		
	}
}
