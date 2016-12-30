package de.uniwuerzburg.battletanks.entity;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.BattleTanks;

/**
 * Represents a Player/Tank in the game logic
 */
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

	/**
	 * Creates a new Player based on a given Tank model
	 * 
	 * @param tank
	 *            Tank model the Player is based on
	 * @param key_up
	 *            The players UP key
	 * @param key_down
	 *            The players DOWN key
	 * @param key_left
	 *            The players LEFT key
	 * @param key_right
	 *            The players RIGHT key
	 * @param key_shoot
	 *            The players SHOOT key
	 */
	public Player(Tanks tank, int key_up, int key_down, int key_left, int key_right, int key_shoot) {
		super("tank" + tank.getName());
		
		this.tank = tank;

		this.key_up = key_up;
		this.key_down = key_down;
		this.key_right = key_right;
		this.key_left = key_left;
		this.key_shoot = key_shoot;

		initForSpawn();

		width = BattleTanks.getInstance().getPreferences().getInteger("player_width", 40);
		height = BattleTanks.getInstance().getPreferences().getInteger("player_height", 40);

		collisionRectangleWidth = width;
		collisionRectangleHeight = height;

		movingSpeed = BattleTanks.getInstance().getPreferences().getInteger("player_speed", 150);

		gunSprite = BattleTanks.getInstance().getTextureAtlas().createSprite("barrel" + tank.getName());
		gunSprite.setSize(BattleTanks.getInstance().getPreferences().getInteger("gun_width", 10),
				BattleTanks.getInstance().getPreferences().getInteger("gun_height", 40));
		gunSprite.setOrigin(gunSprite.getWidth() / 2, 0);
		
		direction = Direction.UP;
	}

	/**
	 * Called each frame. Processes the user input, moves the Player
	 */
	@Override
	public void update() {

		// player is dead and respawns
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

			// shootTimer will be increased by the time since the last frame
			shootTimer += Gdx.graphics.getDeltaTime();

			// if the reloadTime is over and key_shoot is pressed, then create
			// bullet and reset shootTimer
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

			// if the player would leave the playing field, then the speed is
			// set to 0 and player's position is set to the border of the
			// playing field

			if (position.x + speed.x * Gdx.graphics.getDeltaTime() < 0) {
				position.x = 0;
				speed.x = 0;
			}

			if (position.x + speed.x * Gdx.graphics.getDeltaTime() + width > BattleTanks.getInstance().getGame().getWidth()) {
				position.x = BattleTanks.getInstance().getGame().getWidth() - width;
				speed.x = 0;
			}

			if (position.y + speed.y * Gdx.graphics.getDeltaTime() < 0) {
				position.y = 0;
				speed.y = 0;
			}

			if (position.y + speed.y * Gdx.graphics.getDeltaTime() + height > BattleTanks.getInstance().getGame().getHeight()) {
				position.y = BattleTanks.getInstance().getGame().getHeight() - height;
				speed.y = 0;
			}

		}
		super.update();
	}

	/**
	 * Resets the Players HP, Speed and Shottimer to default
	 */
	public void initForSpawn() {
		currentHitpoints = tank.getMaxHitpoints();
		speed.set(0, 0);
		shootTimer = tank.getReloadTime();
	}

	/**
	 * Calculates the respawn Point with the greatest Distance to the other
	 * players
	 * 
	 * @return The respawn position
	 */
	public Vector2 getSpawnPoint() {
		float gameWidth = BattleTanks.getInstance().getGame().getWidth();
		float gameHeight = BattleTanks.getInstance().getGame().getHeight();

		int spawnOffset = BattleTanks.getInstance().getPreferences().getInteger("spawn_offset", 20);

		// possible spawn points are the corners of the game
		Vector2[] spawnPoints = new Vector2[4];
		spawnPoints[0] = new Vector2(spawnOffset, spawnOffset);
		spawnPoints[1] = new Vector2(spawnOffset, gameHeight - height - spawnOffset);
		spawnPoints[2] = new Vector2(gameWidth - width - spawnOffset, spawnOffset);
		spawnPoints[3] = new Vector2(gameWidth - width - spawnOffset, gameHeight - height - spawnOffset);

		// creates list of all positions of the other players
		List<Vector2> playerPositions = new LinkedList<Vector2>();
		for (Player p : BattleTanks.getInstance().getGame().getPlayers()) {
			if (p != this) {
				playerPositions.add(p.getPosition().cpy());
			}
		}

		// calculates the minimal distance to a player for each spawn point
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

		// calculates the index of the farthest spawn point
		int max = 0;
		for (int i = 1; i < minDistances.length; i++) {
			if (minDistances[i] > minDistances[max]) {
				max = i;
			}
		}

		return spawnPoints[max];
	}

	/**
	 * Creates a new Bullet at the tip of the gun of the tank
	 */
	public void createBullet() {
		Vector2 pos = position.cpy();

		// pos will be the middle of the player
		pos.x += width / 2.f;
		pos.y += height / 2.f;

		// pos will be the top of the gun
		pos.x -= Math.sin(Math.toRadians(direction.getRotation())) * gunSprite.getHeight();
		pos.y += Math.cos(Math.toRadians(direction.getRotation())) * gunSprite.getHeight();

		Bullet bullet = new Bullet(this, pos);
		BattleTanks.getInstance().getGame().getEntities().add(bullet);

		tank.getShotSound().play();
	}

	/**
	 * Draws the tank's gun
	 * 
	 * @param batch
	 *            The SpriteBatch to be drawn to
	 */
	public void renderGun(SpriteBatch batch) {
		gunSprite.setPosition(getX() + 0.5f * (getWidth() - gunSprite.getWidth()), getY() + getHeight() / 2);
		gunSprite.setRotation(direction.getRotation());
		gunSprite.draw(batch);
	}

	/**
	 * @return The UP key
	 */
	public int getKey_up() {
		return key_up;
	}

	/**
	 * Sets the UP key
	 */
	public void setKey_up(int key_up) {
		this.key_up = key_up;
	}

	/**
	 * @return The DOWN key
	 */
	public int getKey_down() {
		return key_down;
	}

	/**
	 * Sets the DOWN key
	 */
	public void setKey_down(int key_down) {
		this.key_down = key_down;
	}

	/**
	 * @return The LEFT key
	 */
	public int getKey_left() {
		return key_left;
	}

	/**
	 * Sets the LEFT key
	 */
	public void setKey_left(int key_left) {
		this.key_left = key_left;
	}

	/**
	 * @return The RIGHT key
	 */
	public int getKey_right() {
		return key_right;
	}

	/**
	 * Sets the RIGHT key
	 */
	public void setKey_right(int key_right) {
		this.key_right = key_right;
	}

	/**
	 * @return The SHOOT key
	 */
	public int getKey_shoot() {
		return key_shoot;
	}

	/**
	 * Sets the SHOOT key
	 */
	public void setKey_shoot(int key_shoot) {
		this.key_shoot = key_shoot;
	}

	/**
	 * @return The length of the speed Vector
	 */
	public int getMovingSpeed() {
		return movingSpeed;
	}

	/**
	 * Sets the moving speed
	 * 
	 * @param movingSpeed
	 *            The lenght of the speed vector
	 */
	public void setMovingSpeed(int movingSpeed) {
		this.movingSpeed = movingSpeed;
	}

	/**
	 * Sets the Player's number. 0 < Number <= 4
	 * 
	 * @param number
	 */
	public void setNumber(int number) {
		if (number >= 1 && number <= 4) {
			this.number = number;
		}
	}

	/**
	 * 
	 * @return The Player's number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * 
	 * @return The Tank the Player is based on
	 */
	public Tanks getTank() {
		return tank;
	}

	/**
	 * @return The Direction the Player is pointing
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the Direction the Player is pointing
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Draws the lifebar
	 * 
	 * @param r
	 *            The ShapeRenderer used to draw the lifebar
	 */
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

	/**
	 * 
	 * @return The player's current hitpoints
	 */
	public float getCurrentHitpoints() {
		return currentHitpoints;
	}

	/**
	 * Processes the hit of a Bullet
	 * 
	 * @param bullet
	 *            The Bullet the player was hit by
	 */
	public void hitPlayer(Bullet bullet) {
		float realDamage = tank.calculateDamage(bullet.getDamage());

		currentHitpoints -= realDamage;
		if (currentHitpoints <= 0) {
			currentHitpoints = 0.f;
			bullet.getPlayer().setKills(bullet.getPlayer().getKills() + 1);
		}

	}

	/**
	 * 
	 * @return the number of kills the Player made in the current round
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Sets the amount of kills the player made in the current round
	 * 
	 * @param kills
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}

	/**
	 * 
	 * @return The number of times the Player died in this round
	 */
	public int getDeathCount() {
		return deathCount;
	}

	/**
	 * Called when Battletanks is closed
	 */
	@Override
	public void dispose() {
		tank.getShotSound().dispose();
	}
}
