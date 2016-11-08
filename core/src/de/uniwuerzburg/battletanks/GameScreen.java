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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

	static int width = 800;
	static int height = 600;
	final BattleTanks game;
	SpriteBatch batch;
	Texture img;
	BitmapFont font;

	OrthographicCamera camera;
	Viewport viewPort;

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
		viewPort = new FitViewport(width,height,camera);

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

		/*
		 * Player player3 = new Player(Input.Keys.U, Input.Keys.J, Input.Keys.H,
		 * Input.Keys.K, Input.Keys.E); entities.add(player3);
		 * player3.setPosition(400, 300); players.add(player3);
		 */

		Obstacle test = new Obstacle();
		test.setPosition(300, 200);
		test.setWidth(100);
		test.setHeight(160);
		entities.add(test);
		
		Obstacle test2 = new Obstacle();
		test2.setPosition(500, 70);
		test2.setWidth(100);
		test2.setHeight(80);
		entities.add(test2);
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
		for (int i = 0; i < entities.size(); i++) {
			for (int j = 0; j < entities.size(); j++) {
				if (i == j)
					continue;

				Entity e1 = entities.get(i);
				Entity e2 = entities.get(j);

				if (e1 instanceof Player) {

					if (e2 instanceof Player || e2 instanceof Obstacle) {
						checkCollisionPlayerObstacle((Player) e1, e2);
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

		for (Entity entity : entities) {
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

	private void checkCollisionPlayerObstacle(Player p, Entity o) {
		// p1 is left to p2
		if (p.getX() + p.getWidth() >= o.getX() && p.getX() + p.getWidth() < o.getX() + o.getWidth()) {
			if (p.getY() + p.getHeight() > o.getY() && p.getY() < o.getY() + o.getHeight()) {
				if (p.getSpeed().x > 0) {
					p.setX(o.getX() - p.getWidth());
					p.getSpeed().x = 0;
				}
			}
		}

		// p1 is right to p2
		if (p.getX() >= o.getX() && p.getX() <= o.getX() + o.getWidth()) {
			if (p.getY() + p.getHeight() > o.getY() && p.getY() < o.getY() + o.getHeight()) {
				if (p.getSpeed().x < 0) {
					p.setX(o.getX() + o.getWidth());
					p.getSpeed().x = 0;
				}
			}
		}

		// p1 is below p2
		if (p.getY() + p.getHeight() >= o.getY() && p.getY() + p.getHeight() < o.getY() + o.getHeight()) {
			if (p.getX() + p.getWidth() > o.getX() && p.getX() < o.getX() + o.getWidth()) {
				if (p.getSpeed().y > 0) {
					p.setY(o.getY() - p.getHeight());
					p.getSpeed().y = 0;
				}
			}
		}

		// p1 is above p2
		if (p.getY() > o.getY() && p.getY() <= o.getY() + o.getHeight()) {
			if (p.getX() + p.getWidth() > o.getX() && p.getX() < o.getX() + o.getWidth()) {
				if (p.getSpeed().y < 0) {
					p.setY(o.getY() + o.getHeight());
					p.getSpeed().y = 0;
				}
			}
		}

	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
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
