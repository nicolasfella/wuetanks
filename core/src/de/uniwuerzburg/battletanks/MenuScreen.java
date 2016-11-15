package de.uniwuerzburg.battletanks;

import java.util.*;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

import de.uniwuerzburg.battletanks.FileChooser.ResultListener;

public class MenuScreen implements Screen {
	final BattleTanks game;
	private Skin skin;
	private Stage stage;
	private Table mainTable;

	private TextureAtlas atlas;
	private TextureAtlas keyatlas = new TextureAtlas(Gdx.files.internal("textures/keyatlas.atlas"));
	private List<Player> players;

	private int time;

	// falls keine tilemap geladen wird, wird die TestMap an GameScreen
	// übergeben
	private FileHandle tiledMapFileHandle = Gdx.files.internal("maps/TestMap.tmx");

	public MenuScreen(final BattleTanks game) {
		this.game = game;

		this.atlas = BattleTanks.getTextureAtlas();

		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.stage = new Stage(new FitViewport(1024,768));
		Gdx.input.setInputProcessor(stage);

		mainTable = new Table();
		mainTable.setFillParent(true);

		// stage.setDebugAll(true);
		create();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	/**
	 * Überprüft, ob alle benötigten Parameter (time, map, players) festgelegt
	 * wurden und startet dann das game
	 * 
	 */
	private void startGame() {
		if (time != 0 && !players.isEmpty()) {
			game.setScreen(new GameScreen(game, time, tiledMapFileHandle, players));
			dispose();

		} else {
			final Button close = new TextButton("close", skin);
			Dialog error = new Dialog("Error", skin);

			if (time == 0) {
				error.text("No time entered!");
			} else {
				error.text("No tank chosen!");
			}
			error.button(close);
			error.show(stage);
		}
	}

	/**
	 * Erstellt das gesamte Menü
	 */
	public void create() {
		players = new ArrayList<>(4);

		Button start = createStartButton();
		HorizontalGroup mapLoader = createMapLoader();
		HorizontalGroup hgroup = createTimeTextField();

		mainTable.add(mapLoader, hgroup);
		mainTable.row();
		mainTable.add(createTankChooser(1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.E)).expand();
		mainTable.add(createTankChooser(2, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT)).expand();
		mainTable.row();
		mainTable.add(createTankChooser(3, Keys.T, Keys.G, Keys.F, Keys.H, Keys.Z)).expand();
		mainTable.add(createTankChooser(4, Keys.I, Keys.K, Keys.J, Keys.L, Keys.O)).expand();
		mainTable.row();
		mainTable.add(start).colspan(2).padBottom(20).width(100).height(50);

		stage.addActor(mainTable);
	}

	/**
	 * Erstellt den Startbutton, der dafür sorgt, dass zum GameScreen gewechselt
	 * wird
	 * 
	 * @return Button
	 */
	private Button createStartButton() {
		Button start = new TextButton("START!", skin);// , "toggle");
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startGame();
			}
		});
		return start;
	}

	/**
	 * Erstellt ein TextField mit Enter-Button zur Eingabe einer Spieldauer
	 * 
	 * @return HorizontalGroup
	 */
	private HorizontalGroup createTimeTextField() {
		HorizontalGroup hgroup = new HorizontalGroup();

		Button enter = new TextButton("Enter Time", skin);
		TextField timeInput = new TextField(null, skin);
		timeInput.setMaxLength(3);

		enter.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					time = Integer.parseInt(timeInput.getText());
					timeInput.setDisabled(true);
					enter.setDisabled(true);
				} catch (NumberFormatException e) {
					final Button close = new TextButton("close", skin);
					Dialog error = new Dialog("Error", skin);
					error.text("Not a valid number!");
					error.button(close);
					error.show(stage);
				}
			}
		});

		hgroup.addActor(timeInput);
		hgroup.addActor(enter);
		return hgroup;

	}

	/**
	 * Erstellt eine Möglichkeit, Maps zu laden; Das Label oll dann später
	 * natürlich die jeweils geladene Map anzeigen zur überprüfung für den
	 * benutzer, ob er die richtige Map ausgewählt hat
	 * 
	 * @return HorizontalGroup
	 */
	private HorizontalGroup createMapLoader() {
		HorizontalGroup temp = new HorizontalGroup();
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

		temp.addActor(loadButton);
		temp.addActor(loadedMap);

		return temp;
	}

	/**
	 * Erstellt eine Table mit Darstellung des Panzers und einer Möglichkeit,
	 * durch die Panzer durchzuwechseln um einen auszuwählen. Ein TankChooser
	 * wird mit einer Tastenbelegung initialisiert Erstellt gleichzeitig die
	 * Player, die dann an den GameScreen übergeben werden
	 * 
	 * @return Table
	 */
	private Table createTankChooser(int number, int key_up, int key_down, int key_left, int key_right, int key_shoot) {
		Table tankChooser = new Table();

		HorizontalGroup temp = new HorizontalGroup();
		Button next = new TextButton(" >> ", skin);
		Button previous = new TextButton(" << ", skin);

		Image[] images = new Image[3];
		Image one = new Image(atlas.createSprite("player"));
		one.setName("player");
		images[0] = one;
		Image two = new Image(atlas.createSprite("player2"));
		two.setName("player2");
		images[1] = two;
		Image three = new Image(atlas.createSprite("player3"));
		three.setName("player3");
		images[2] = three;

		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (int i = 0; i < images.length; i++) {
					if (temp.getChildren().contains(images[i], false)) {
						temp.removeActor(images[i]);
						int mod = Math.floorMod((i + 1), images.length);
						temp.addActorAt(1, images[mod]);
						break;
					}
				}
			}
		});

		previous.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (int i = 0; i < images.length; i++) {
					if (temp.getChildren().contains(images[i], false)) {
						temp.removeActor(images[i]);
						int mod = Math.floorMod((i - 1), images.length);
						System.out.println(mod);
						temp.addActorAt(1, images[mod]);
						break;
					}
				}
			}
		});
		temp.addActorAt(0, previous);
		temp.addActorAt(1, one);
		temp.addActorAt(2, next);

		TextButton lockButton = new TextButton("Lock tank choice", skin);
		lockButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String sprite = temp.getChildren().get(1).getName();
				Player player = new Player(sprite, key_up, key_down, key_left, key_right, key_shoot);
				player.setNumber(number);
				players.add(player);

				next.setDisabled(true);
				previous.setDisabled(true);
				lockButton.setChecked(true);
			}
		});

		tankChooser.add(temp);
		tankChooser.row();
		tankChooser.add(lockButton);
		tankChooser.row();
		tankChooser.add(createKeys(number)).expand();
		return tankChooser;

	}

	private Table createKeys(int number) {
		Table keys = new Table();
		Image move = new Image();
		Image shoot = new Image();
		switch (number) {
		case 1:
			move = new Image(keyatlas.createSprite("wasd"));
			shoot = new Image(keyatlas.createSprite("e"));
			break;
		case 2:
			move = new Image(keyatlas.createSprite("updownleftright"));
			shoot = new Image(keyatlas.createSprite("ctrl"));
			break;
		case 3:
			move = new Image(keyatlas.createSprite("tfgh"));
			shoot = new Image(keyatlas.createSprite("z"));
			break;
		case 4:
			move = new Image(keyatlas.createSprite("ijkl"));
			shoot = new Image(keyatlas.createSprite("o"));
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
		keyatlas.dispose();
	}

}