package de.uniwuerzburg.battletanks;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.uniwuerzburg.battletanks.FileChooser.ResultListener;

public class MenuScreen implements Screen {
	final BattleTanks game;
	private Skin skin;
	private Stage stage;
	private Table mainTable;

	private int time;
	
	// falls keine tilemap geladen wird, wird die TestMap an GameScreen übergeben
	private FileHandle tiledMapFileHandle = Gdx.files.internal("TestMap.tmx");

	public MenuScreen(final BattleTanks game) {
		this.game = game;

		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.stage = new Stage(new ScreenViewport());
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
		// game.setScreen(new GameScreen(game));
		if (time != 0) {
			game.setScreen(new GameScreen(game, time, tiledMapFileHandle));
			dispose();

		} else {
			final Button close = new TextButton("close", skin);
			Dialog error = new Dialog("Error", skin);
			error.text("No time entered!");
			error.button(close);
			error.show(stage);
		}
	}

	/**
	 * Erstellt das gesamte Menü
	 */
	public void create() {
		Button start = createStartButton();
		HorizontalGroup mapLoader = createMapLoader();
		HorizontalGroup hgroup = createTimeTextField();

		mainTable.add(mapLoader, hgroup);
		mainTable.row();
		for (int i = 0; i < 2; i++) {
			mainTable.add(createTankChooser()).expand();
			mainTable.add(createTankChooser()).expand();
			mainTable.row();
		}
		mainTable.add(start).colspan(2).padBottom(20);

		stage.addActor(mainTable);
	}

	/**
	 * Erstellt den Startbutton, der dafür sorgt, dass zum GameScreen gewechselt
	 * wird
	 * 
	 * @return Button
	 */
	private Button createStartButton() {
		final Button start = new TextButton("START!", skin);// , "toggle");
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
					// System.out.println(time);
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
	 * durch die Panzer durchzuwechseln um einen auszuwählen.
	 * 
	 * @return Table
	 */
	private HorizontalGroup createTankChooser() {
		HorizontalGroup temp = new HorizontalGroup();
		Button next = new TextButton(" >> ", skin);
		Button previous = new TextButton(" << ", skin);

		Image one = new Image(new Texture(Gdx.files.internal("player.png")));
		Image two = new Image(new Texture(Gdx.files.internal("player2.png")));
		Image three = new Image(new Texture(Gdx.files.internal("player3.png")));
		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (temp.getChildren().contains(one, false)) {
					temp.removeActor(one);
					temp.addActorAt(1, two);
				} else if (temp.getChildren().contains(two, false)) {
					temp.removeActor(two);
					temp.addActorAt(1, three);
				} else if (temp.getChildren().contains(three, false)) {
					temp.removeActor(three);
					temp.addActorAt(1, one);
				}
			}
		});

		previous.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (temp.getChildren().contains(one, false)) {
					temp.removeActor(one);
					temp.addActorAt(1, three);
				} else if (temp.getChildren().contains(two, false)) {
					temp.removeActor(two);
					temp.addActorAt(1, one);
				} else if (temp.getChildren().contains(three, false)) {
					temp.removeActor(three);
					temp.addActorAt(1, two);
				}
			}
		});

		temp.addActorAt(0, previous);
		temp.addActorAt(1, one);
		temp.addActorAt(2, next);

		return temp;

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
	}

}