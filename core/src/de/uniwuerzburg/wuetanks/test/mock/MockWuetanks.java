package de.uniwuerzburg.wuetanks.test.mock;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.uniwuerzburg.wuetanks.Wuetanks;

public class MockWuetanks extends Wuetanks {

	private TextureAtlas mockAtlas;
	private MockAudio mockAudio;
	private MockFiles mockFiles;
	private MockPreferences mockPreferences;

	public MockWuetanks() {
		super();
		mockAtlas = new MockAtlas();
		mockAudio = new MockAudio();
		mockFiles = new MockFiles();
		mockPreferences = new MockPreferences();
	}

	@Override
	public TextureAtlas getTextureAtlas() {
		return mockAtlas;
	}

	@Override
	public Audio getAudio() {
		return mockAudio;
	}

	@Override
	public Files getFiles() {
		return mockFiles;
	}

	@Override
	public Preferences getPreferences() {
		return mockPreferences;
	}

}
