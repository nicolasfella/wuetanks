package de.uniwuerzburg.battletanks.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.uniwuerzburg.battletanks.BattleTanks;
import de.uniwuerzburg.battletanks.entity.Player;

public class EndScreen implements Screen {

	List<Player> players;

	private Stage stage;
	private Skin skin;
	private Table mainTable;
	private BattleTanks game;

	private Preferences prefs;

	private Texture background;

	public EndScreen(final BattleTanks game, List<Player> players) {
		this.players = new ArrayList<>(players);
		this.game = game;
		prefs = BattleTanks.getPreferences();

		this.skin = new Skin(Gdx.files.internal(prefs.getString("uiskin", "data/uiskin.json")));
		this.stage = new Stage(
				new FitViewport(prefs.getInteger("window_width", 1024), prefs.getInteger("window_height", 768)));
		background = new Texture(Gdx.files.internal("background.png"));
		Gdx.input.setInputProcessor(stage);

		create();

	}

	private void create() {
		Label test = new Label("Scoreboard:", skin);
		this.mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.add(test).padBottom(20);

		Comparator<Player> comp = new playerComparator();
		players.sort(comp);

		for (Player p : players) {
			Label playerLabel = new Label(
					"Player " + p.getNumber() + " Kills: " + p.getKills() + " Deaths: " + p.getDeathCount(), skin);
			mainTable.row();
			mainTable.add(playerLabel).pad(5);
		}

		mainTable.row();
		mainTable.add(newGameButton()).padTop(20);

		stage.addActor(mainTable);
	}

	private Button newGameButton() {
		Button start = new TextButton("NEW GAME!", skin);
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MenuScreen m = new MenuScreen(game);
				BattleTanks.addScreen(m);
				game.setScreen(m);
			}
		});
		return start;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		// Gdx.gl.glClearColor(0, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());

		stage.getBatch().begin();
		Color c = stage.getBatch().getColor();
		stage.getBatch().setColor(c.r, c.g, c.b, 0.6f);
		stage.getBatch().draw(background, 0, 0, stage.getWidth(), stage.getHeight());
		stage.getBatch().end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	private class playerComparator implements Comparator<Player> {
		@Override
		public int compare(Player o1, Player o2) {
			if (o1.getKills() != o2.getKills()) {
				return o2.getKills() - o1.getKills();
			} else if (o1.getDeathCount() != o2.getDeathCount()) {
				return o1.getDeathCount() - o2.getDeathCount();
			} else if (o1.getNumber() != o2.getNumber()) {
				return o1.getNumber() - o2.getNumber();
			}

			return 0;
		}
	}
}
