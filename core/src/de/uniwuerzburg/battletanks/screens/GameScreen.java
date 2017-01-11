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

	private int width;
	private int height;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewPort;
	private ShapeRenderer shapeRenderer;

	private BitmapFont font;
	private int fontSize = BattleTanks.getInstance().getPreferences().getInteger("game_font_size", 14);
	private GlyphLayout layout;
	private FreeTypeFontGenerator generator;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private FileHandle tiledMapFileHandle;

	private List<Entity> entities;
	private List<Player> players;

	private int spawnOffset = BattleTanks.getInstance().getPreferences().getInteger("spawn_offset", 20);

	private Music music;

	private float time;
	private float startTime;

	/**
	 * Creates a new GameScreen instance
	 * 
	 * @param time
	 *            The games length in seconds
	 * @param tiledMapFileHandle
	 *            Filehandle of the map to be loaded
	 * @param players
	 *            The games players
	 */
	public GameScreen(float time, FileHandle tiledMapFileHandle, List<Player> players) {
		reset(time, tiledMapFileHandle, players);
	}

	/**
	 * Resets the game
	 * 
	 * @param time
	 *            The games length in seconds
	 * @param tiledMapFileHandle
	 *            Filehandle of the map to be loaded
	 * @param players
	 *            The games players
	 */
	public void reset(float time, FileHandle tiledMapFileHandle, List<Player> players) {

		if (time <= 0) {
			throw new IllegalArgumentException("No valid time entered");
		}

		if (players == null) {
			throw new NullPointerException("No List of Players given");
		}

		this.startTime = time;
		this.time = time;
		this.players = new ArrayList<>(players);
		this.tiledMapFileHandle = tiledMapFileHandle;
		entities = new ArrayList<Entity>();
	}

	/**
	 * Called when Screen is shown. For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
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
		generator = new FreeTypeFontGenerator(Gdx.files
				.internal(BattleTanks.getInstance().getPreferences().getString("game_font", "fonts/Vera.ttf")));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = fontSize;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
		layout = new GlyphLayout();
		layout.setText(font, "");
	}

	private void loadMusic() {
		music = Gdx.audio.newMusic(Gdx.files
				.internal(BattleTanks.getInstance().getPreferences().getString("background_music", "music.mp3")));
		// music.play();
		// music.setLooping(true);
	}

	/**
	 * Called each frame. For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
	@Override
	public void render(float delta) {
		Gdx.graphics.setTitle(BattleTanks.getInstance().getPreferences().getString("title", "Battletanks"));
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		time -= delta;
		if (time <= 0) {
			BattleTanks.getInstance().showEnd(players);
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

	// updates all entities in the game and solves collisions between them

	private void updateEntities() {
		// updates all current entities
		int n = entities.size();
		for (int i = 0; i < n; i++) {
			Entity entity = entities.get(i);
			entity.update();
		}

		// updates new entities which were inserted by updating (eg: bullets)
		for (int i = n; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.update();
		}

		// solve collisions between players and obstacles
		for (Player p : players) {
			for (Entity e : entities) {
				if (e instanceof Obstacle) {
					detectCollisionAndResponse(p, e);
				}
			}
		}

		// solve collisions between players and players
		for (Player p : players) {
			for (Player e : players) {
				detectCollisionAndResponse(p, e);
			}
		}

		// find bullets which can be deleted
		List<Entity> bulletsToDelete = new LinkedList<Entity>();

		for (Entity b : entities) {
			if (b instanceof Bullet) {
				// bullets out of the game will be deleted
				if (isOutOfGame(b)) {
					bulletsToDelete.add(b);
				} else {
					// bullets hitting an entity will be deleted
					for (Entity e : entities) {
						if (detectBulletHitAndDamage((Bullet) b, e)) {
							bulletsToDelete.add(b);
						}
					}
				}
			}
		}

		// delete marked bullets
		for (Entity b : bulletsToDelete) {
			entities.remove(b);
		}

	}

	// loads the given map

	private void loadMap() {

		// try to load the map. if it fails, load the default map
		try {
			tiledMap = new TmxMapLoader(new AbsoluteFileHandleResolver()).load(tiledMapFileHandle.path());
		} catch (Exception e) {
			tiledMap = new TmxMapLoader()
					.load(BattleTanks.getInstance().getPreferences().getString("default_map", "maps/default.tmx"));
		}

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		MapProperties tiledMapProps = tiledMap.getProperties();

		// calculates the width and height of the map in pixels
		int mapWidth = tiledMapProps.get("width", Integer.class);
		int mapHeight = tiledMapProps.get("height", Integer.class);
		int tilePixelWidth = tiledMapProps.get("tilewidth", Integer.class);
		int tilePixelHeight = tiledMapProps.get("tileheight", Integer.class);

		width = mapWidth * tilePixelWidth;
		height = mapHeight * tilePixelHeight;

		// reads the objects from the objects layer of the tilemap and creates
		// obstacles
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

	// detects collision between bullet and entity if entity is a player,
	// perform damage on him
	// @param b bullet
	// @param p entity
	// @return boolean whether there was a collision

	private boolean detectBulletHitAndDamage(Bullet b, Entity p) {

		if (b == p || b.getPlayer() == p) {
			return false;
		}

		// collision rectangles
		Rectangle bRect = b.getCollisionRectangle();
		Rectangle pRect = p.getCollisionRectangle();

		if (bRect.overlaps(pRect)) {
			if (p instanceof Player) {
				((Player) p).hitPlayer(b);
			}
			return true;
		}

		return false;
	}

	// @param e entity
	// @return boolean whether entity is out of game

	private boolean isOutOfGame(Entity e) {

		Rectangle playField = new Rectangle(0, 0, width, height);
		Rectangle eRect = e.getCollisionRectangle();

		if (eRect.overlaps(playField)) {
			return false;
		}

		return true;
	}

	// Detects whether there is a collision between the entities. If yes, then
	// move the entities to solve the collision.
	// @param p entity
	// @param o entity

	private void detectCollisionAndResponse(Entity p, Entity o) {

		if (p == o) {
			return;
		}

		// variables for the first entity

		Rectangle pRect = p.getCollisionRectangle();

		float pRX = pRect.getX() + pRect.getWidth();
		float pLX = pRect.getX();
		float pVX = p.getSpeed().x;

		float pRY = pRect.getY() + pRect.getHeight();
		float pLY = pRect.getY();
		float pVY = p.getSpeed().y;

		// variables for the second entity

		Rectangle oRect = o.getCollisionRectangle();

		float oRX = oRect.getX() + oRect.getWidth();
		float oLX = oRect.getX();
		float oVX = o.getSpeed().x;

		float oRY = oRect.getY() + oRect.getHeight();
		float oLY = oRect.getY();
		float oVY = o.getSpeed().y;

		// if the collision rectangles overlap then there is a collision
		if (pRect.overlaps(oRect)) {

			if ((startTime - Gdx.graphics.getDeltaTime()) <= time) {
				BattleTanks.getInstance()
						.showError("There are obstacles at the players' spawn points!\nTry loading another map!");
			}

			// calculates the horizontal and vertical overlap
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

			// to solve the collision, move the entities in the direction of the
			// smaller overlap
			// --> moving with minimal distance to solve the collision

			if (overlapY > overlapX) {

				translations = solveOverlap(pVX, oVX, overlapX);

				if (translations[0] != 0) {
					p.setX(p.getX() + translations[0]);
					p.getSpeed().x = 0;
				}

				if (translations[1] != 0) {
					o.setX(o.getX() + translations[1]);
					o.getSpeed().x = 0;
				}

			} else {

				translations = solveOverlap(pVY, oVY, overlapY);

				if (translations[0] != 0) {
					p.setY(p.getY() + translations[0]);
					p.getSpeed().y = 0;
				}

				if (translations[1] != 0) {
					o.setY(o.getY() + translations[1]);
					o.getSpeed().y = 0;
				}

			}

		}

	}

	// calculates the translations for the entities based on the type of
	// collision
	// @param pSpeed speed of the first entity
	// @param oSpeed speed of the second entity
	// @param overlap of the entities
	// @return float[0] is the translation for the first entity, float[1] is the
	// translation for the second entity

	private float[] solveOverlap(float pSpeed, float oSpeed, float overlap) {
		float[] translations = new float[2];

		// if an entity isn't moving, then the other entity will be moved back
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

		// otherwise there are only the following possibilities
		else {

			// if both entities are driving towards each other, then both
			// entities will be moved back
			if (pSpeed > 0 && oSpeed < 0) {

				translations[0] = -overlap / 2.f;
				translations[1] = overlap / 2.f;

			} else if (pSpeed < 0 && oSpeed > 0) {

				translations[0] = overlap / 2.f;
				translations[1] = -overlap / 2.f;

			}

			// if an entity is faster then the other one and hits the other one
			// in the back, the faster one will be moved back
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

	/**
	 * Called when window is resized. For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
	}

	/**
	 * For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
	@Override
	public void pause() {

	}

	/**
	 * For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
	@Override
	public void resume() {

	}

	/**
	 * For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
	@Override
	public void hide() {

	}

	/**
	 * Called when the window is destroyed. For further information see
	 * <a href="https://github.com/libgdx/libgdx/wiki/The-life-cycle">libGDX
	 * Wiki</a>
	 */
	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
		tiledMap.dispose();
		shapeRenderer.dispose();
		generator.dispose();
		music.dispose();

		for (Entity e : entities) {
			e.dispose();
		}
	}

	/**
	 * @return Returns the width of the game world in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return Returns the height of the game world in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return Returns the list of entities currently active in the game logic
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * 
	 * @return Return the list of players currently active in the game logic
	 */
	public List<Player> getPlayers() {
		return players;
	}

}
