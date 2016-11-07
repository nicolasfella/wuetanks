package de.uniwuerzburg.battletanks;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

	final BattleTanks game;

	static int width = 800;
	static int height = 600;

	SpriteBatch batch;
	Texture img;
	BitmapFont font;

	OrthographicCamera camera;
	private GlyphLayout layout;

	private List<Entity> entities;

	private List<Player> players;

	public GameScreen(final BattleTanks game) {
		this.game = game;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		img = new Texture("gravel.jpg");

		font = new BitmapFont();
		font.setColor(Color.ORANGE);

		layout = new GlyphLayout();
		layout.setText(font, "");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);

		entities = new ArrayList<>();
		players = new ArrayList<>(4);

		Player player1 = new Player(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E);
		entities.add(player1);
		player1.setPosition(0, 0);
		players.add(player1);

		Player player2 = new Player(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.E);
		entities.add(player2);
		player2.setPosition(150, 500);
		players.add(player2);
		
		/*Player player3 = new Player(Input.Keys.U, Input.Keys.J, Input.Keys.H, Input.Keys.K, Input.Keys.E);
		entities.add(player3);
		player3.setPosition(400, 300);
		players.add(player3);*/

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		for (Entity entity : entities) {
			entity.update();
		}

		// Player to player collision
		// extend to player-obstacle?
		for (int i = 0; i < players.size(); i++) {
			for (int j = 0; j < players.size(); j++) {
				if (i == j)
					continue;
				Player p1 = players.get(i);
				Player p2 = players.get(j);

				// p1 is left to p2
				if (p1.getX() + p1.getWidth() >= p2.getX() && p1.getX() + p1.getWidth() < p2.getX() + p2.getWidth()) {
					if (p1.getY() + p1.getHeight() > p2.getY() && p1.getY() < p2.getY() + p2.getHeight()) {
						if (p1.getSpeed().x > 0) {
							p1.setX(p2.getX() - p1.getWidth());
							p1.getSpeed().x = 0;
						}
					}
				}

				// p1 is right to p2
				if (p1.getX() >= p2.getX() && p1.getX() <= p2.getX() + p2.getWidth()) {
					if (p1.getY() + p1.getHeight() > p2.getY() && p1.getY() < p2.getY() + p2.getHeight()) {
						if (p1.getSpeed().x < 0) {
							p1.setX(p2.getX() + p1.getWidth());
							p1.getSpeed().x = 0;
						}
					}
				}

				// p1 is below p2
				if (p1.getY() + p1.getHeight() >= p2.getY()
						&& p1.getY() + p1.getHeight() < p2.getY() + p2.getHeight()) {
					if (p1.getX() + p1.getWidth() > p2.getX() && p1.getX() < p2.getX() + p2.getWidth()) {
						if (p1.getSpeed().y > 0) {
							p1.setY(p2.getY() - p1.getHeight());
							p1.getSpeed().y = 0;
						}
					}
				}

				// p1 is above p2
				if (p1.getY() > p2.getY() && p1.getY() <= p2.getY() + p2.getHeight()) {
					if (p1.getX() + p1.getWidth() > p2.getX() && p1.getX() < p2.getX() + p2.getWidth()) {
						if (p1.getSpeed().y < 0) {
							p1.setY(p2.getY() + p1.getHeight());
							p1.getSpeed().y = 0;
						}
					}
				}

			}
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(img, 0, 0, 400, 400);
		batch.draw(img, 0, 400, 400, 400);
		batch.draw(img, 400, 0, 400, 400);
		batch.draw(img, 400, 400, 400, 400);

		for (

		Entity entity : entities) {
			entity.render(batch);
		}

		// font.draw(batch, "Player 1: 7 hits 3 kills", 10, 20);
		/*
		 * font.draw(batch, "Player 3: 3 hits 3 kills", 10, 25);
		 * font.draw(batch, "Player 1: 5 hits 8 kills", 10, height - 10);
		 * layout.setText(font, "Player 2: 5 hits 8 kills"); font.draw(batch,
		 * "Player 2: 5 hits 8 kills", width - layout.width - 10, height - 10);
		 * layout.setText(font, "Player 4: 10 hits 6 kills"); font.draw(batch,
		 * "Player 4: 10 hits 6 kills", width - layout.width - 10, 25);
		 */

		batch.end();

	}

	@Override
	public void resize(int width, int height) {

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
		font.dispose();
		batch.dispose();
		img.dispose();

	}

}
