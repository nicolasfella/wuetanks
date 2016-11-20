package de.uniwuerzburg.battletanks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.uniwuerzburg.battletanks.BattleTanks;
import de.uniwuerzburg.battletanks.entity.Entity;
import de.uniwuerzburg.battletanks.entity.Obstacle;
import de.uniwuerzburg.battletanks.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

	public static GameScreen instance;

	private int width;

	private int height;
	private final BattleTanks game;

	private SpriteBatch batch;
	private BitmapFont font;

	private OrthographicCamera camera;
	private Viewport viewPort;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private FileHandle tiledMapFileHandle;

	private GlyphLayout layout;

	private List<Entity> entities;

	private List<Player> players;

	public GameScreen(final BattleTanks game, int time) {
		instance = this;
		this.game = game;
	}

	/**
	 * @param game
	 * @param time
	 * @param tiledMapFileHandle
	 * @param players
	 */
	public GameScreen(final BattleTanks game, int time, FileHandle tiledMapFileHandle, List<Player> players) {
		instance = this;
		this.game = game;
		this.players = new ArrayList<>(players);
		this.tiledMapFileHandle = tiledMapFileHandle;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.ORANGE);

		layout = new GlyphLayout();
		layout.setText(font, "");

		if (tiledMapFileHandle != null) {
			tiledMap = new TmxMapLoader(new AbsoluteFileHandleResolver()).load(tiledMapFileHandle.path());
		} else {
			tiledMap = new TmxMapLoader().load("maps/TestMap.tmx");
		}
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		MapProperties tiledMapProps = tiledMap.getProperties();

		int mapWidth = tiledMapProps.get("width", Integer.class);
		int mapHeight = tiledMapProps.get("height", Integer.class);
		int tilePixelWidth = tiledMapProps.get("tilewidth", Integer.class);
		int tilePixelHeight = tiledMapProps.get("tileheight", Integer.class);

		width = mapWidth * tilePixelWidth;
		height = mapHeight * tilePixelHeight;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		viewPort = new FitViewport(width, height, camera);

		entities = new ArrayList<Entity>();

		for (Player p : players) {
			entities.add(p);

			float x = 0;
			float y = 0;
			switch (p.getNumber()) {
			case 1:
				x = 0;
				y = height;
				break;
			case 2:
				x = width;
				y = height;
				break;
			case 3:
				x = 0;
				y = 0;
				break;
			case 4:
				x = width;
				y = 0;
			}
			p.setPosition(x, y);
		}

		// einlesen der objects aus dem objects layer der tilemap und erstellung
		// der obstacles

		MapLayer objectsLayer = tiledMap.getLayers().get("objects");

		if (objectsLayer != null) {
			for (MapObject object : objectsLayer.getObjects()) {

				if (object instanceof RectangleMapObject) {
					RectangleMapObject rectObject = (RectangleMapObject) object;
					Rectangle rect = rectObject.getRectangle();
					Obstacle obst = new Obstacle();
					obst.setPosition((int) rect.getX(), (int) rect.getY());
					obst.setWidth((int) rect.getWidth());
					obst.setHeight((int) rect.getHeight());
					entities.add(obst);
				}
			}
		}

	}

	@Override
	public void render(float delta) {

		Gdx.graphics.setTitle("Battletanks " + Gdx.graphics.getFramesPerSecond() + " fps");

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		for (Entity entity : entities) {
			entity.update();
		}

		// auflösung der kollisionen zwischen playern und obstacles
		for (Entity e1 : entities) {
			if (e1 instanceof Player) {
				for (Entity e2 : entities) {
					if (e2 instanceof Obstacle && e1 != e2) {
						checkCollisionPlayerObstacle((Player) e1, e2);
					}
				}
			}
		}

		// auflösung der kollisionen zwischen playern und playern
		for (Entity e1 : entities) {
			if (e1 instanceof Player) {
				for (Entity e2 : entities) {
					if (e2 instanceof Player && e1 != e2) {
						checkCollisionPlayerPlayer((Player) e1, (Player) e2);
					}
				}
			}
		}

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

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

	private void checkCollisionPlayerPlayer(Player p, Player o) {

		// variablen für 1. player

		float pRX = p.getX() + p.getWidth();
		float pLX = p.getX();
		float pVX = p.getSpeed().x;

		float pRY = p.getY() + p.getHeight();
		float pLY = p.getY();
		float pVY = p.getSpeed().y;

		// variablen für 2. player

		float oRX = o.getX() + o.getWidth();
		float oLX = o.getX();
		float oVX = o.getSpeed().x;

		float oRY = o.getY() + o.getHeight();
		float oLY = o.getY();
		float oVY = o.getSpeed().y;

		// kollision nur bei überschneidung in vertikaler sowie horizontaler
		// richtung
		if ((pRY >= oLY && pLY < oRY) && (pRX >= oLX && pLX < oRX)) {

			// berechnung wie stark sich die player in x und y richtung
			// überlappen
			float overlapX;
			float overlapY;

			if (pLX < oLX) {
				overlapX = pRX - oLX;
			} else {
				overlapX = oRX - pLX;
			}

			if (pLY < oLY) {
				overlapY = pRY - oLY;
			} else {
				overlapY = oRY - pLY;
			}

			// momentan noch mit abfrage ob die überlappung in x oder y richtung
			// aufgelöst werden soll
			// TODO: code kürzen und eventuell anderes verfahren mit
			// richtungsvektoren

			if (overlapY > overlapX) {

				// falls beide player aufeinander zufahren muss der overlap auf
				// beide aufgeteilt werden
				if (pVX > 0 && oVX < 0) {
					p.setX(pLX - overlapX / 2.f);
					p.getSpeed().x = 0;

					o.setX(oLX + overlapX / 2.f);
					o.getSpeed().x = 0;
				} else if (pVX < 0 && oVX > 0) {
					p.setX(pLX + overlapX / 2.f);
					p.getSpeed().x = 0;

					o.setX(oLX - overlapX / 2.f);
					o.getSpeed().x = 0;
				} else {

					// andernfalls muss herausgefunden werden wer den overlap
					// verursacht hat und von welcher richtung
					if (pVX > 0) {
						p.setX(pLX - overlapX);
						p.getSpeed().x = 0;
					}
					if (pVX < 0) {
						p.setX(pLX + overlapX);
						p.getSpeed().x = 0;
					}

					if (oVX > 0) {
						o.setX(oLX - overlapX);
						o.getSpeed().x = 0;
					}
					if (oVX < 0) {
						o.setX(oLX + overlapX);
						o.getSpeed().x = 0;
					}
				}
			} else {

				// falls beide player aufeinander zufahren muss der overlap auf
				// beide aufgeteilt werden
				if (pVY > 0 && oVY < 0) {
					p.setY(pLY - overlapY / 2.f);
					p.getSpeed().y = 0;

					o.setY(oLY + overlapY / 2.f);
					o.getSpeed().y = 0;
				} else if (pVY < 0 && oVY > 0) {
					p.setY(pLY + overlapY / 2.f);
					p.getSpeed().y = 0;

					o.setY(oLY - overlapY / 2.f);
					o.getSpeed().y = 0;
				} else {

					// andernfalls muss herausgefunden werden wer den overlap
					// verursacht hat und von welcher richtung
					if (pVY > 0) {
						p.setY(pLY - overlapY);
						p.getSpeed().y = 0;
					}
					if (pVY < 0) {
						p.setY(pLY + overlapY);
						p.getSpeed().y = 0;
					}

					if (oVY > 0) {
						o.setY(oLY - overlapY);
						o.getSpeed().y = 0;
					}
					if (oVY < 0) {
						o.setY(oLY + overlapY);
						o.getSpeed().y = 0;
					}
				}

			}

		}

	}

	private void checkCollisionPlayerObstacle(Player p, Entity o) {

		Rectangle r1 = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
		Rectangle r2 = new Rectangle(o.getX(), o.getY(), o.getWidth(), o.getHeight());

		if (r1.overlaps(r2)) {
			// System.out.println("Intersection");
		}

		// p1 is left to p2
		if (p.getX() + p.getWidth() >= o.getX()) {
			if (p.getOldPosition().x + p.getWidth() <= o.getX()) {
				if (p.getY() + p.getHeight() > o.getY() && p.getY() < o.getY() + o.getHeight()) {
					if (p.getX() > p.getOldPosition().x) {
						p.setX(o.getX() - p.getWidth());
						p.getSpeed().x = 0;
						// System.out.println("Kollision von links");
					}
				}
			}
		}

		// p1 is below p2
		if (p.getY() + p.getHeight() >= o.getY()) {
			if (p.getOldPosition().y + p.getHeight() <= o.getY()) {
				if (p.getX() + p.getWidth() > o.getX() && p.getX() < o.getX() + o.getWidth()) {
					if (p.getY() > p.getOldPosition().y) {
						p.setY(o.getY() - p.getHeight());
						p.getSpeed().y = 0;
						// System.out.println("Kollision von unten");
					}
				}
			}
		}

		// p1 is above p2
		if (p.getY() < o.getY() + o.getHeight()) {
			if (p.getOldPosition().y >= o.getY() + o.getHeight()) {
				if (p.getX() + p.getWidth() > o.getX() && p.getX() < o.getX() + o.getWidth()) {
					if (p.getY() < p.getOldPosition().y) {
						p.setY(o.getY() + o.getHeight());
						p.getSpeed().y = 0;
						// System.out.println("Kollision von oben");
					}
				}
			}
		}

		// p is right to o
		if (p.getX() <= o.getX() + o.getWidth()) {
			if (p.getOldPosition().x >= o.getX() + o.getWidth()) {
				if (p.getY() + p.getHeight() > o.getY() && p.getY() < o.getY() + o.getHeight()) {
					if (p.getX() < p.getOldPosition().x) {
						p.setX(o.getX() + o.getWidth());
						p.getSpeed().x = 0;
						// System.out.println("Kollision von rechts");
					}
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
		tiledMap.dispose();
		tiledMapFileHandle.delete();

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
