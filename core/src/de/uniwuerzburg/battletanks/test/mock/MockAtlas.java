package de.uniwuerzburg.battletanks.test.mock;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MockAtlas extends TextureAtlas {
	
	public MockAtlas() {
	
	}
	
	@Override
	public Sprite createSprite(String name) {
		return new MockSprite();
	}

}
