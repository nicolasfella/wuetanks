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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import de.uniwuerzburg.battletanks.entity.Bullet;
import de.uniwuerzburg.battletanks.entity.Direction;
import de.uniwuerzburg.battletanks.entity.Entity;
import de.uniwuerzburg.battletanks.entity.Obstacle;
import de.uniwuerzburg.battletanks.entity.Player;
import de.uniwuerzburg.battletanks.entity.Tanks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
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

	private float time;

	private int startOffset = 20;

	private ShapeRenderer shapeRenderer;

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
	public GameScreen(final BattleTanks game, float time, FileHandle tiledMapFileHandle, List<Player> players) {
		instance = this;
		this.game = game;
		this.time = time;
		this.players = new ArrayList<>(players);
		this.tiledMapFileHandle = tiledMapFileHandle;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.WHITE);

		layout = new GlyphLayout();
		layout.setText(font, "");

		if (tiledMapFileHandle != null) {
			tiledMap = new TmxMapLoader(new AbsoluteFileHandleResolver()).load(tiledMapFileHandle.path());
		} else {
			tiledMap = new TmxMapLoader()
					.load(BattleTanks.getPreferences().getString("default_map", "maps/TestMap.tmx"));
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
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		entities = new ArrayList<Entity>();

		for (Player p : players) {
			entities.add(p);
			p.setEntities(entities);

			float x = 0;
			float y = 0;
			switch (p.getNumber()) {
			case 1:
				x = startOffset;
				y = height - startOffset - p.getHeight();
				p.setDirection(Direction.DOWNRIGHT);
				break;
			case 2:
				x = width - p.getWidth() - startOffset;
				y = height - p.getHeight() - startOffset;
				p.setDirection(Direction.DOWNLEFT);
				break;
			case 3:
				x = startOffset;
				y = startOffset;
				p.setDirection(Direction.UPRIGHT);
				break;
			case 4:
				x = width - p.getWidth() - startOffset;
				y = startOffset;
				p.setDirection(Direction.UPLEFT);
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
		Gdx.graphics.setTitle(BattleTanks.getPreferences().getString("title", "Battletanks"));
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// float deltaTime = Gdx.graphics.getDeltaTime();
		// time -= deltaTime;
		time -= delta;
		if (time <= 0) {
			game.setScreen(new EndScreen());
			// this.dispose();
		}

		camera.update();

		// updated zuerst die ursprünglichen entities
		int n = entities.size();
		for (int i = 0; i < n; i++) {
			Entity entity = entities.get(i);
			entity.update();
		}

		// updated neu hinzugekommene entities
		for (int i = n; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.update();
		}

		for (Entity p : entities) {

			// auflösung der kollisionen zwischen playern und obstacles
			if (p instanceof Player) {
				// ((Player) p).setCurrentHitpoints(((Player)
				// p).getTank().getMaxHitpoints()*(float)Math.abs(Math.sin(time)));
				for (Entity e : entities) {
					if (e instanceof Obstacle && p != e) {
						detectCollisionAndResponse(p, e);
					}
				}
			}
		}

		List<Entity> bulletsToDelete = new LinkedList<Entity>();

		for (Entity b : entities) {
			if (b instanceof Bullet) {

				// bullets die das spielfeld verlassen werden gelöscht
				if (isOutOfGame(b)) {
					bulletsToDelete.add(b);
				} else {
					// bullets die auf eine entity treffen werden gelöscht
					for (Entity e : entities) {
						if (detectBulletHitAndDamage((Bullet) b, e)) {
							bulletsToDelete.add(b);
						}
					}
				}
			}
		}

		// lösche bullets
		for (Entity b : bulletsToDelete) {
			entities.remove(b);
		}

		// auflösung der kollisionen zwischen playern und playern
		for (Entity p : entities) {
			if (p instanceof Player) {
				for (Entity e : entities) {
					if (e instanceof Player && p != e) {
						detectCollisionAndResponse(p, e);
					}
				}
			}
		}

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (Player p : players) {
			p.renderLifeBar(shapeRenderer);
		}
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for (Entity entity : entities) {
			entity.render(batch);
		}

		for (Player p : players) {
			p.renderGun(batch);
		}

		String timeLeft = "Time left: " + formatTime(time);

		layout.setText(font, timeLeft);
		font.draw(batch, timeLeft, width / 2 - layout.width / 2, height - 10);

		batch.end();

	}

	private boolean detectBulletHitAndDamage(Bullet b, Entity p) {
		// variablen für bullet

		float bRX = b.getX() + b.getWidth();
		float bLX = b.getX();

		float bRY = b.getY() + b.getHeight();
		float bLY = b.getY();

		// variablen für player

		float pRX = p.getX() + p.getWidth();
		float pLX = p.getX();

		float pRY = p.getY() + p.getHeight();
		float pLY = p.getY();

		// kollision nur bei überschneidung in vertikaler sowie horizontaler
		// richtung
		if ((bRY >= pLY && bLY < pRY) && (bRX >= pLX && bLX < pRX) && b.getPlayer() != p) {
			if (p instanceof Player) {
				((Player) p).hitPlayer(b.getDamage());
			}

			return true;
		}

		return false;
	}

	private boolean isOutOfGame(Entity e) {
		float max = Math.max(e.getHeight(), e.getWidth());

		if (e.getX() < 0 - max || e.getX() > width + max || e.getY() < 0 - max || e.getY() > height + max) {
			return true;
		}

		return false;
	}

	private void detectCollisionAndResponse(Entity p, Entity o) {

		// variablen für 1. entity

		float pRX = p.getX() + p.getWidth();
		float pLX = p.getX();
		float pVX = p.getSpeed().x;

		float pRY = p.getY() + p.getHeight();
		float pLY = p.getY();
		float pVY = p.getSpeed().y;

		// variablen für 2. entity

		float oRX = o.getX() + o.getWidth();
		float oLX = o.getX();
		float oVX = o.getSpeed().x;

		float oRY = o.getY() + o.getHeight();
		float oLY = o.getY();
		float oVY = o.getSpeed().y;

		// kollision nur bei überschneidung in vertikaler sowie horizontaler
		// richtung
		if ((pRY >= oLY && pLY < oRY) && (pRX >= oLX && pLX < oRX)) {

			// berechnung wie stark sich die entities in x und y richtung
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

			float[] translations;

			// es wird in richtung der kleineren überlappung aufgelöst ->
			// kleinste verschiebung

			if (overlapY > overlapX) {

				translations = solveOverlap(pVX, oVX, overlapX);

				if (translations[0] != 0) {
					p.setX(pLX + translations[0]);
					p.getSpeed().x = 0;
				}

				if (translations[1] != 0) {
					o.setX(oLX + translations[1]);
					o.getSpeed().x = 0;
				}

			} else {

				translations = solveOverlap(pVY, oVY, overlapY);

				if (translations[0] != 0) {
					p.setY(pLY + translations[0]);
					p.getSpeed().y = 0;
				}

				if (translations[1] != 0) {
					o.setY(oLY + translations[1]);
					o.getSpeed().y = 0;
				}

			}

		}

	}

	private float[] solveOverlap(float pSpeed, float oSpeed, float overlap) {
		float[] translations = new float[2];

		// falls eine der beiden entities steht, wird der verursacher der
		// kollision zurückgesetzt
		if (pSpeed == 0 || oSpeed == 0) {
			if (pSpeed > 0) {
				translations[0] = -overlap;
			}
			if (pSpeed < 0) {
				translations[0] = overlap;
			}

			if (oSpeed > 0) {
				translations[1] = -overlap;
			}
			if (oSpeed < 0) {
				translations[1] = overlap;
			}
		}

		// andernfalls gibt es nur noch folgende kollisionsursachen
		else {

			// falls beide entities aufeinander zufahren muss der overlap auf
			// beide aufgeteilt werden
			if (pSpeed > 0 && oSpeed < 0) {

				translations[0] = -overlap / 2.f;
				translations[1] = overlap / 2.f;

			} else if (pSpeed < 0 && oSpeed > 0) {

				translations[0] = overlap / 2.f;
				translations[1] = -overlap / 2.f;

			}

			// falls eine entity schneller als die andere ist und ihr hinten
			// auffährt, wird die schnellere ausgebremst
			else if (pSpeed > 0 && oSpeed > 0) {

				if (pSpeed > oSpeed) {
					translations[0] = -overlap;
				} else {
					translations[1] = -overlap;
				}

			} else if (pSpeed < 0 && oSpeed < 0) {

				if (pSpeed < oSpeed) {
					translations[0] = overlap;
				} else {
					translations[1] = overlap;
				}

			}

		}

		return translations;
	}

	public String formatTime(float time) {

		int minutes = (int) time / 60;
		int seconds = (int) time % 60;

		if (seconds < 10) {
			return minutes + ":0" + seconds;
		}

		return minutes + ":" + seconds;
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
		if (tiledMapFileHandle != null) {
			tiledMapFileHandle.delete();
		}
		shapeRenderer.dispose();

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
