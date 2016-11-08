package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {
	final BattleTanks game;

	private Skin skin;
	private Stage stage;
	private Table mainTable;

	public MenuScreen(final BattleTanks game) {
		this.game = game;

		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		mainTable = new Table();
		mainTable.setFillParent(true);

		create();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	public void create() {
		final Button start = new TextButton("START!", skin, "toggle");
		TextField test = new TextField(null, skin);
		start.align(Align.bottom);
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen(game));
				dispose();
			}
		});

		final Button next = new TextButton("NEXT TANK", skin);
		Image one = new Image(new Texture(Gdx.files.internal("player.png")));
		Image two = new Image(new Texture(Gdx.files.internal("player2.png")));
		Image zero = new Image();

		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (mainTable.getChildren().contains(zero, false)) {
					mainTable.removeActor(zero);
					mainTable.addActorAt(3, one);

				} else if (mainTable.getChildren().contains(one, false)) {
					mainTable.removeActor(one);
					mainTable.addActorAt(3, two);

					mainTable.addActorAt(3, two);
				} else if (mainTable.getChildren().contains(two, false)) {
					mainTable.removeActor(two);
					mainTable.addActorAt(3, one);
				}

			}
		});
		
		
		
		test.setMaxLength(3);
		test.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					try {
						int time = Integer.parseInt(test.getText());
						System.out.println(time);

					} catch (NumberFormatException e) {
						System.out.println("Keine gültige Zahl eingegeben!");
					}
				}
				return true;
			}
		});

		mainTable.add(start, test, next, zero);
		stage.addActor(mainTable);
	}
}