package de.uniwuerzburg.wuetanks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import de.uniwuerzburg.wuetanks.Wuetanks;

public class ErrorScreen implements Screen {

	private Stage stage;
	private Stage bgStage;
	private Skin skin;
	private Table mainTable;
	private Preferences prefs;
	private Image background;
	private String message;

	public ErrorScreen(String message) {
		prefs = Wuetanks.getInstance().getPreferences();

		this.skin = new Skin(Gdx.files.internal(prefs.getString("uiskin", "data/uiskin.json")));
		this.stage = new Stage(
				new FitViewport(prefs.getInteger("window_width", 1024), prefs.getInteger("window_height", 768)));
		this.bgStage = new Stage(new StretchViewport(1920, 1080));

		this.message = message;

		Gdx.input.setInputProcessor(stage);

		reset(message);
	}

	public void reset(String message) {
		stage.clear();
		bgStage.clear();
		Gdx.input.setInputProcessor(stage);

		this.message = message;
		create();
	}

	private void create() {
		background = new Image(new Texture(Gdx.files.internal(prefs.getString("background", "background.png"))));
		Color c = background.getColor();
		background.setColor(c.r, c.g, c.b, 0.6f);
		bgStage.addActor(background);

		Label error = new Label("Error:", skin);
		Label errorMsg = new Label(message, skin);

		this.mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.add(error).padBottom(20);
		mainTable.row();
		mainTable.add(errorMsg).padBottom(20);

		mainTable.row();
		mainTable.add(newGameButton()).padTop(20);

		stage.addActor(mainTable);
	}

	private Button newGameButton() {
		Button start = new TextButton("NEW GAME!", skin);
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Wuetanks.getInstance().showMenu();
			}
		});
		return start;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// Gdx.gl.glClearColor(0, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		bgStage.act(Gdx.graphics.getDeltaTime());
		stage.act(Gdx.graphics.getDeltaTime());

		bgStage.getViewport().apply();
		bgStage.draw();
		stage.getViewport().apply();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		bgStage.getViewport().update(width, height, true);
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
