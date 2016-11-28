package de.uniwuerzburg.battletanks.entity;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.BattleTanks;
import de.uniwuerzburg.battletanks.screens.GameScreen;

public class Player extends Entity {

	private int key_up, key_down, key_left, key_right, key_shoot;

	private int movingSpeed;

	private int number;

	private float shootTimer;

	private Direction direction;

	private Sprite gunSprite;

	private Tanks tank;

	private float currentHitpoints;

	private int kills;

	private int deathCount;

	public Player(Tanks tank, int key_up, int key_down, int key_left, int key_right, int key_shoot) {
		super("tank" + tank.getName());

		this.tank = tank;

		this.key_up = key_up;
		this.key_down = key_down;
		this.key_right = key_right;
		this.key_left = key_left;
		this.key_shoot = key_shoot;

		initForSpawn();

		width = BattleTanks.getPreferences().getInteger("player_width", 40);
		height = BattleTanks.getPreferences().getInteger("player_height", 40);
		
		collisionRectangleWidth = width;
		collisionRectangleHeight = height;

		movingSpeed = BattleTanks.getPreferences().getInteger("player_speed", 150);

		gunSprite = BattleTanks.getTextureAtlas().createSprite("barrel" + tank.getName());
		gunSprite.setSize(BattleTanks.getPreferences().getInteger("gun_width", 10),
				BattleTanks.getPreferences().getInteger("gun_height", 40));
		gunSprite.setOrigin(gunSprite.getWidth() / 2, 0);
	}

	@Override
	public void update() {

		if (currentHitpoints == 0.f) {
			deathCount++;
			initForSpawn();
			position = getSpawnPoint().cpy();
			oldPosition = position.cpy();

		} else {

			if (speed.x < 0 && speed.y > 0) {
				direction = Direction.UPLEFT;
			} else if (speed.x > 0 && speed.y > 0) {
				direction = Direction.UPRIGHT;
			} else if (speed.x < 0 && speed.y < 0) {
				direction = Direction.DOWNLEFT;
			} else if (speed.x > 0 && speed.y < 0) {
				direction = Direction.DOWNRIGHT;
			} else if (speed.y > 0 && speed.x == 0) {
				direction = Direction.UP;
			} else if (speed.x < 0 && speed.y == 0) {
				direction = Direction.LEFT;
			} else if (speed.y < 0 && speed.x == 0) {
				direction = Direction.DOWN;
			} else if (speed.x > 0 && speed.y == 0) {
				direction = Direction.RIGHT;
			}

			shootTimer += Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(key_shoot) && shootTimer >= tank.getReloadTime()) {
				createBullet();
				shootTimer = 0.f;
			}

			speed.set(0, 0);

			if (Gdx.input.isKeyPressed(key_up)) {
				speed.y = 1;
			}

			if (Gdx.input.isKeyPressed(key_down)) {
				speed.y = -1;
			}

			if (Gdx.input.isKeyPressed(key_left)) {
				speed.x = -1;
			}
			if (Gdx.input.isKeyPressed(key_right)) {
				speed.x = 1;
			}

			if (Gdx.input.isKeyPressed(key_up) && Gdx.input.isKeyPressed(key_down)) {
				speed.y = 0;
			}

			if (Gdx.input.isKeyPressed(key_left) && Gdx.input.isKeyPressed(key_right)) {
				speed.x = 0;
			}

			speed.nor();
			speed.scl(movingSpeed);

			if (position.x + speed.x * Gdx.graphics.getDeltaTime() < 0) {
				position.x = 0;
				speed.x = 0;
			}

			if (position.x + speed.x * Gdx.graphics.getDeltaTime() + width > GameScreen.instance.getWidth()) {
				position.x = GameScreen.instance.getWidth() - width;
				speed.x = 0;
			}

			if (position.y + speed.y * Gdx.graphics.getDeltaTime() < 0) {
				position.y = 0;
				speed.y = 0;
			}

			if (position.y + speed.y * Gdx.graphics.getDeltaTime() + height > GameScreen.instance.getHeight()) {
				position.y = GameScreen.instance.getHeight() - height;
				speed.y = 0;
			}

		}
		super.update();
	}

	public void initForSpawn() {
		currentHitpoints = tank.getMaxHitpoints();
		speed.set(0, 0);
		shootTimer = tank.getReloadTime();
	}

	public Vector2 getSpawnPoint() {
		float gameWidth = GameScreen.instance.getWidth();
		float gameHeight = GameScreen.instance.getHeight();

		int spawnOffset = BattleTanks.getPreferences().getInteger("spawn_offset", 20);

		Vector2[] spawnPoints = new Vector2[4];
		spawnPoints[0] = new Vector2(spawnOffset, spawnOffset);
		spawnPoints[1] = new Vector2(spawnOffset, gameHeight - height - spawnOffset);
		spawnPoints[2] = new Vector2(gameWidth - width - spawnOffset, spawnOffset);
		spawnPoints[3] = new Vector2(gameWidth - width - spawnOffset, gameHeight - height - spawnOffset);

		List<Vector2> playerPositions = new LinkedList<Vector2>();
		for (Entity e : GameScreen.instance.getEntities()) {
			if (e instanceof Player && e != this) {
				playerPositions.add(e.getPosition().cpy());
			}
		}

		float[] minDistances = new float[4];

		for (int i = 0; i < spawnPoints.length; i++) {
			minDistances[i] = Float.MAX_VALUE;
			for (Vector2 p : playerPositions) {
				float dist = spawnPoints[i].dst2(p);
				if (minDistances[i] > dist) {
					minDistances[i] = dist;
				}
			}

		}

		int max = 0;
		for (int i = 1; i < minDistances.length; i++) {
			if (minDistances[i] > minDistances[max]) {
				max = i;
			}
		}

		return spawnPoints[max];
	}

	public void createBullet() {
		Vector2 pos = position.cpy();

		// pos wird zu mitte des players
		pos.x += width / 2.f;
		pos.y += height / 2.f;

		// pos wird zu spitze der waffe
		pos.x -= Math.sin(Math.toRadians(direction.getRotation())) * gunSprite.getHeight();
		pos.y += Math.cos(Math.toRadians(direction.getRotation())) * gunSprite.getHeight();

		Bullet bullet = new Bullet(this, pos);
		GameScreen.instance.getEntities().add(bullet);

		tank.getShotSound().play();
	}

	public void renderGun(SpriteBatch batch) {
		gunSprite.setPosition(getX() + 0.5f * (getWidth() - gunSprite.getWidth()), getY() + getHeight() / 2);
		gunSprite.setRotation(direction.getRotation());
		gunSprite.draw(batch);
	}

	public int getKey_up() {
		return key_up;
	}

	public void setKey_up(int key_up) {
		this.key_up = key_up;
	}

	public int getKey_down() {
		return key_down;
	}

	public void setKey_down(int key_down) {
		this.key_down = key_down;
	}

	public int getKey_left() {
		return key_left;
	}

	public void setKey_left(int key_left) {
		this.key_left = key_left;
	}

	public int getKey_right() {
		return key_right;
	}

	public void setKey_right(int key_right) {
		this.key_right = key_right;
	}

	public int getKey_shoot() {
		return key_shoot;
	}

	public void setKey_shoot(int key_shoot) {
		this.key_shoot = key_shoot;
	}

	public int getMovingSpeed() {
		return movingSpeed;
	}

	public void setMovingSpeed(int movingSpeed) {
		this.movingSpeed = movingSpeed;
	}

	public void setNumber(int number) {
		if (number >= 1 && number <= 4) {
			this.number = number;
		}
	}

	public int getNumber() {
		return number;
	}

	public Tanks getTank() {
		return tank;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void renderLifeBar(ShapeRenderer r) {

		float percentage = currentHitpoints / tank.getMaxHitpoints();

		if (getY() > 15) {
			r.setColor(Color.RED);
			r.rect(getX(), getY() - 10, getWidth(), 5);
			r.setColor(Color.GREEN);
			r.rect(getX(), getY() - 10, getWidth() * percentage, 5);
		} else {
			r.setColor(Color.RED);
			r.rect(getX(), getY() + getHeight() + 5, getWidth(), 5);
			r.setColor(Color.GREEN);
			r.rect(getX(), getY() + getHeight() + 5, getWidth() * percentage, 5);
		}
	}

	public float getCurrentHitpoints() {
		return currentHitpoints;
	}

	public void hitPlayer(Bullet bullet) {
		float realDamage = tank.calculateDamage(bullet.getDamage());

		currentHitpoints -= realDamage;
		if (currentHitpoints <= 0) {
			currentHitpoints = 0.f;
			bullet.getPlayer().setKills(bullet.getPlayer().getKills() + 1);
		}

	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeathCount() {
		return deathCount;
	}

	@Override
	public void dispose() {
		tank.getShotSound().dispose();
	}
}
