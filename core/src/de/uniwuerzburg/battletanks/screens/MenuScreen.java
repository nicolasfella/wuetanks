package de.uniwuerzburg.battletanks.screens;

import java.util.*;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.uniwuerzburg.battletanks.BattleTanks;
import de.uniwuerzburg.battletanks.entity.Player;
import de.uniwuerzburg.battletanks.entity.Tanks;
import de.uniwuerzburg.battletanks.utility.FileChooser;
import de.uniwuerzburg.battletanks.utility.FileChooser.ResultListener;

/**
 * The main menu of the Game; For choosing tanks, a map and the match duration
 */
public class MenuScreen implements Screen {
	final BattleTanks game;

	/** The skin.json for Buttons, Fonts etc */
	private Skin skin;

	/** The standard TextureAtlas as used in this project */
	private TextureAtlas atlas;

	/** The main Stage; Provides environment for the main Table */
	private Stage stage;

	/** The mainTable; Contains every UI element */
	private Table mainTable;

	/** The TextField for match duration input */
	private TextField timeInput;

	/** List of players for GameScreen */
	private List<Player> players;

	/** time in seconds for GameScreen */
	private float time;

	private FileHandle tiledMapFileHandle;

	private Preferences prefs;

	/**
	 * New MenuScreen for a BattleTanks game; sets atlas, skin, stage, mainTable
	 * and creates the UI
	 * 
	 * @param game
	 */
	public MenuScreen(final BattleTanks game) {
		this.game = game;
		prefs = BattleTanks.getPreferences();
		tiledMapFileHandle = null;
		this.atlas = BattleTanks.getTextureAtlas();

		this.skin = new Skin(Gdx.files.internal(prefs.getString("uiskin", "data/uiskin.json")));
		this.stage = new Stage(new FitViewport(prefs.getInteger("window_width", 1024), prefs.getInteger("window_height", 768)));
		Gdx.input.setInputProcessor(stage);

		mainTable = new Table();
		mainTable.setFillParent(true);

		// stage.setDebugAll(true);
		create();
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

	/**
	 * Checks if a time and at least one tank are chosen, then starts the Game
	 * by creating and setting a new GameScreen;
	 */
	private void startGame() {
		if (time != 0 && !players.isEmpty()) {
			game.setScreen(new GameScreen(game, time, tiledMapFileHandle, players));
			this.dispose();
		} else {
			final Button close = new TextButton("close", skin);
			Dialog error = new Dialog("Error", skin);

			if (time == 0) {
				error.text("No valid time entered!");
			} else {
				error.text("No tank chosen!");
			}
			error.button(close);
			error.show(stage);
		}
	}

	/** Creates the entire Menu UI */
	public void create() {
		players = new ArrayList<>(4);

		Button start = createStartButton();
		HorizontalGroup mapLoader = createMapLoader();
		HorizontalGroup timeTextField = createTimeTextField();

		mainTable.add(mapLoader, timeTextField);
		mainTable.row().padTop(10);
		mainTable.add(createTankChooser(1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.E)).expand();
		mainTable.add(createTankChooser(2, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT)).expand();
		mainTable.row();
		mainTable.add(createTankChooser(3, Keys.T, Keys.G, Keys.F, Keys.H, Keys.Z)).expand();
		mainTable.add(createTankChooser(4, Keys.I, Keys.K, Keys.J, Keys.L, Keys.O)).expand();
		mainTable.row();
		mainTable.add(start).colspan(2).padBottom(20).width(100);

		stage.addActor(mainTable);
	}

	/**
	 * Creates the StartButton that calls startGame()
	 * 
	 * @return Button
	 */
	private Button createStartButton() {
		Button start = new TextButton("START!", skin);
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					time = Float.parseFloat(timeInput.getText());
				} catch (NumberFormatException e) {
					time = 0;
				}
				startGame();
			}
		});
		return start;
	}

	/**
	 * Creates the UI Element for the input of a match duration
	 * 
	 * @return HorizontalGroup
	 */
	private HorizontalGroup createTimeTextField() {
		HorizontalGroup hgroup = new HorizontalGroup();

		Label enterTime = new Label("Enter time (sec): ", skin);
		timeInput = new TextField(null, skin);
		timeInput.setMaxLength(3);
		timeInput.setText(""+prefs.getInteger("default_time", 50));

		hgroup.addActor(enterTime);
		hgroup.addActor(timeInput);
		return hgroup;

	}

	/**
	 * Creates the UI Element for loading a Map; Uses the custom FileChooser UI
	 * 
	 * @return HorizontalGroup
	 */
	private HorizontalGroup createMapLoader() {
		HorizontalGroup mapLoader = new HorizontalGroup();
		Button loadButton = new TextButton("Load Map", skin);
		Label loadedMap = new Label("", skin);

		loadButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				FileChooser dialog = FileChooser.createDialog("Load TileMap ...", skin,
						Gdx.files.absolute(Gdx.files.getExternalStoragePath()));
				dialog.setResultListener(new ResultListener() {
					@Override
					public boolean result(boolean success, FileHandle result) {
						if (success) {
							loadedMap.setText(result.name());
							tiledMapFileHandle = result;
						}
						return true;
					}
				});
				dialog.show(stage);
			}
		});

		mapLoader.addActor(loadButton);
		mapLoader.addActor(loadedMap);

		return mapLoader;
	}

	/**
	 * Creates the UI Element for choosing a Tank by cycling through the
	 * available Tanks; Once a tank is locked, a new player is created and added
	 * to the List players; Element shows key assignments
	 * 
	 * @param playerNumber
	 * @param key_up
	 * @param key_down
	 * @param key_left
	 * @param key_right
	 * @param key_shoot
	 * @return Table
	 */
	private Table createTankChooser(int playerNumber, int key_up, int key_down, int key_left, int key_right,
			int key_shoot) {
		Table tankChooser = new Table();

		HorizontalGroup tankCycle = new HorizontalGroup();
		Button next = new TextButton(" >> ", skin);
		Button previous = new TextButton(" << ", skin);

		Tanks[] tanks = Tanks.values();
		List<Tanks> tankList = new ArrayList<Tanks>(Arrays.asList(tanks));
		Image[] images = new Image[5];

		for (int i = 0; i < images.length; i++) {
			images[i] = new Image(atlas.createSprite("tank" + tankList.get(i).getName()));
			images[i].setName(tankList.get(i).getName());
		}

		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (int i = 0; i < images.length; i++) {
					if (tankCycle.getChildren().contains(images[i], false)) {
						tankCycle.removeActor(images[i]);
						int mod = Math.floorMod((i + 1), images.length);
						tankCycle.addActorAt(1, images[mod]);
						break;
					}
				}
			}
		});

		previous.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (int i = 0; i < images.length; i++) {
					if (tankCycle.getChildren().contains(images[i], false)) {
						tankCycle.removeActor(images[i]);
						int mod = Math.floorMod((i - 1), images.length);
						System.out.println(mod);
						tankCycle.addActorAt(1, images[mod]);
						break;
					}
				}
			}
		});
		tankCycle.addActorAt(0, previous);
		tankCycle.addActorAt(1, images[0]);
		tankCycle.addActorAt(2, next);

		TextButton lockButton = new TextButton("Lock tank choice", skin);
		lockButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String tankName = tankCycle.getChildren().get(1).getName();
				Tanks tank = tankList.stream().filter(t -> t.getName().equals(tankName)).findAny().get();

				Player player = new Player(tank, key_up, key_down, key_left, key_right, key_shoot);
				player.setNumber(playerNumber);
				players.add(player);

				next.setDisabled(true);
				previous.setDisabled(true);
				lockButton.setDisabled(true);
			}
		});

		tankChooser.add(tankCycle);
		tankChooser.row();
		tankChooser.add(lockButton).padTop(5);
		tankChooser.row();
		tankChooser.add(createKeys(playerNumber)).expand();
		return tankChooser;

	}

	/**
	 * Creates the UI Element for displaying the key assignments used in
	 * createTankChooser
	 * 
	 * @param playerNumber
	 * @return Table
	 */
	private Table createKeys(int playerNumber) {
		Table keys = new Table();
		Image move = new Image();
		Image shoot = new Image();
		switch (playerNumber) {
		case 1:
			move = new Image(atlas.createSprite("wasd"));
			shoot = new Image(atlas.createSprite("e"));
			break;
		case 2:
			move = new Image(atlas.createSprite("updownleftright"));
			shoot = new Image(atlas.createSprite("ctrl"));
			break;
		case 3:
			move = new Image(atlas.createSprite("tfgh"));
			shoot = new Image(atlas.createSprite("z"));
			break;
		case 4:
			move = new Image(atlas.createSprite("ijkl"));
			shoot = new Image(atlas.createSprite("o"));
			break;
		}
		Label plus = new Label(" + ", skin);

		keys.add(move, plus, shoot);
		keys.padTop(20);
		keys.padBottom(20);

		return keys;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
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