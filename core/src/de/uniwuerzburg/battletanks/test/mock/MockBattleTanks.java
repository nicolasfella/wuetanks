package de.uniwuerzburg.battletanks.test.mock;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.uniwuerzburg.battletanks.BattleTanks;

public class MockBattleTanks extends BattleTanks{

	private static TextureAtlas mockAtlas;
	
	public MockBattleTanks() {
		super();
		mockAtlas = new MockAtlas();
	}
	
	@Override
	public TextureAtlas getTextureAtlas() {
		return mockAtlas;
	}
	
}
