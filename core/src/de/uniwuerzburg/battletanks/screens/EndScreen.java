package de.uniwuerzburg.battletanks.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.uniwuerzburg.battletanks.entity.Player;

public class EndScreen implements Screen {

	List<Player> players;

	private Stage stage;
	private Skin skin;
	private Table mainTable;

	public EndScreen() {
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.stage = new Stage(new FitViewport(1024, 768));
		Gdx.input.setInputProcessor(stage);

		Label test = new Label("ENDE DES SPIELS!", skin);
		this.mainTable = new Table();
		mainTable.setFillParent(true);

		mainTable.add(test);
		stage.addActor(mainTable);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
