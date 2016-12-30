package de.uniwuerzburg.battletanks.test.mock;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class MockAudio implements Audio {

	public MockAudio() {

	}

	@Override
	public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
		return null;
	}

	@Override
	public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
		return null;
	}

	@Override
	public Sound newSound(FileHandle fileHandle) {
		return null;
	}

	@Override
	public Music newMusic(FileHandle file) {
		return null;
	}

}
