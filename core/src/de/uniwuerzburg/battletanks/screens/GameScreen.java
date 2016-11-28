package de.uniwuerzburg.battletanks.screens;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

public class GameScreen implements Screen {

	public static GameScreen instance;

	private int width;
	private int height;
	private final BattleTanks game;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewPort;
	private ShapeRenderer shapeRenderer;

	private BitmapFont font;
	private int fontSize = BattleTanks.getPreferences().getInteger("game_font_size", 14);
	private GlyphLayout layout;
	private FreeTypeFontGenerator generator;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private FileHandle tiledMapFileHandle;

	private List<Entity> entities;
	private List<Player> players;

	private int spawnOffset = BattleTanks.getPreferences().getInteger("spawn_offset", 20);

	private Music music;

	private float time;

	public GameScreen(final BattleTanks game, float time, FileHandle tiledMapFileHandle, List<Player> players) {
		instance = this;
		this.game = game;
		this.time = time;
		this.players = new ArrayList<>(players);
		this.tiledMapFileHandle = tiledMapFileHandle;
		entities = new ArrayList<Entity>();
	}

	@Override
	public void show() {
		loadMap();
		setUpGraphics();
		spawnPlayers();
		loadMusic();
	}

	private void spawnPlayers() {
		for (Player p : players) {
			entities.add(p);

			float x = 0;
			float y = 0;
			switch (p.getNumber()) {
			case 1:
				x = spawnOffset;
				y = height - spawnOffset - p.getHeight();
				p.setDirection(Direction.DOWNRIGHT);
				break;
			case 2:
				x = width - p.getWidth() - spawnOffset;
				y = height - p.getHeight() - spawnOffset;
				p.setDirection(Direction.DOWNLEFT);
				break;
			case 3:
				x = spawnOffset;
				y = spawnOffset;
				p.setDirection(Direction.UPRIGHT);
				break;
			case 4:
				x = width - p.getWidth() - spawnOffset;
				y = spawnOffset;
				p.setDirection(Direction.UPLEFT);
			}
			p.setPosition(x, y);
		}
	}

	private void setUpGraphics() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		viewPort = new FitViewport(width, height, camera);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		// Loading font
		generator = new FreeTypeFontGenerator(
				Gdx.files.internal(BattleTanks.getPreferences().getString("game_font", "fonts/Vera.ttf")));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = fontSize;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
		layout = new GlyphLayout();
		layout.setText(font, "");
	}

	private void loadMusic() {
		music = Gdx.audio
				.newMusic(Gdx.files.internal(BattleTanks.getPreferences().getString("background_music", "music.mp3")));
		// music.play();
		// music.setLooping(true);
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.setTitle(BattleTanks.getPreferences().getString("title", "Battletanks"));
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		time -= delta;
		if (time <= 0) {
			EndScreen e = new EndScreen(game, players);
			BattleTanks.addScreen(e);
			game.setScreen(e);
		}

		camera.update();
		updateEntities();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		renderEntities();

		renderText();

		batch.end();

	}

	private void renderText() {
		String timeLeft = "Time left: " + formatTime(time);

		layout.setText(font, timeLeft);
		font.draw(batch, timeLeft, width / 2 - layout.width / 2, height - 10);

		String text = "";
		int offset = 10;

		for (Player p : players) {
			text = "Player " + p.getNumber() + " : " + p.getKills() + " kills" + "\n" + p.getDeathCount() + " deaths";

			switch (p.getNumber()) {
			case 1:
				layout.setText(font, text);
				font.draw(batch, text, offset, height - offset);
				break;
			case 2:
				layout.setText(font, text);
				font.draw(batch, text, width - layout.width - offset, height - offset);
				break;
			case 3:
				layout.setText(font, text);
				font.draw(batch, text, offset, layout.height + offset);
				break;
			case 4:
				layout.setText(font, text);
				font.draw(batch, text, width - layout.width - offset, layout.height + offset);
				break;

			}

		}
	}

	private void renderEntities() {
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
	}

	private void updateEntities() {
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

		// auflösung der kollisionen zwischen playern und obstacles
		for (Player p : players) {
			for (Entity e : entities) {
				if (e instanceof Obstacle && p != e) {
					detectCollisionAndResponse(p, e);
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
		for (Player p : players) {
			for (Player e : players) {
				if (p != e) {
					detectCollisionAndResponse(p, e);
				}
			}
		}
	}

	private void loadMap() {
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

	private boolean detectBulletHitAndDamage(Bullet b, Entity p) {

		// variablen für bullet

		float bRX = b.getBottomLeftCornerX() + b.getRenderWidth();
		float bLX = b.getBottomLeftCornerX();

		float bRY = b.getBottomLeftCornerY() + b.getRenderHeight();
		float bLY = b.getBottomLeftCornerY();

		// variablen für player

		float pRX = p.getBottomLeftCornerX() + p.getRenderWidth();
		float pLX = p.getBottomLeftCornerX();

		float pRY = p.getBottomLeftCornerY() + p.getRenderHeight();
		float pLY = p.getBottomLeftCornerY();

		// kollision nur bei überschneidung in vertikaler sowie horizontaler
		// richtung
		if ((bRY >= pLY && bLY < pRY) && (bRX >= pLX && bLX < pRX) && b.getPlayer() != p && b != p) {

			if (p instanceof Player) {
				((Player) p).hitPlayer(b);
			}

			return true;
		}

		return false;
	}

	private boolean isOutOfGame(Entity e) {
		
		if (e.getBottomLeftCornerX() < 0 - e.getRenderWidth() || e.getBottomLeftCornerX() > width + e.getRenderWidth() || e.getBottomLeftCornerY() < 0 - e.getRenderHeight() || e.getBottomLeftCornerY() > height + e.getRenderHeight()) {
			return true;
		}

		return false;
	}

	private void detectCollisionAndResponse(Entity p, Entity o) {

		// variablen für 1. entity

		float pRX = p.getBottomLeftCornerX() + p.getRenderWidth();
		float pLX = p.getBottomLeftCornerX();
		float pVX = p.getSpeed().x;

		float pRY = p.getBottomLeftCornerY() + p.getRenderHeight();
		float pLY = p.getBottomLeftCornerY();
		float pVY = p.getSpeed().y;

		// variablen für 2. entity

		float oRX = o.getBottomLeftCornerX() + o.getRenderWidth();
		float oLX = o.getBottomLeftCornerX();
		float oVX = o.getSpeed().x;

		float oRY = o.getBottomLeftCornerY() + o.getRenderHeight();
		float oLY = o.getBottomLeftCornerY();
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

	private String formatTime(float time) {

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
		generator.dispose();
		music.dispose();

		for (Entity e : entities) {
			e.dispose();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Entity> getEntities() {
		return entities;
	}
}
