package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.uniwuerzburg.battletanks.BattleTanks;
import de.uniwuerzburg.battletanks.screens.GameScreen;

public class Player extends Entity {

	private static final int WIDTH = 40, HEIGHT = 40;

	private int key_up, key_down, key_left, key_right, key_shoot;

	private int movingSpeed = 150;

	private int number;

	private Direction direction;

	private Sprite gunSprite;

	private TextureAtlas atlas;

	public Player(String sprite, int key_up, int key_down, int key_left, int key_right, int key_shoot) {
		// super("player");
		super(sprite);

		this.key_up = key_up;
		this.key_down = key_down;
		this.key_right = key_right;
		this.key_left = key_left;
		this.key_shoot = key_shoot;

		width = WIDTH;
		height = HEIGHT;

		direction = Direction.UP;

		gunSprite = BattleTanks.getTextureAtlas().createSprite("barrelBlue");
		gunSprite.setSize(10, 40);
		gunSprite.setOrigin(gunSprite.getWidth()/2, 0);
	}

	@Override
	public void update() {

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
		super.update();
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		gunSprite.setPosition(getX() + 0.5f*(getWidth() - gunSprite.getWidth()), getY() + getHeight()/2);
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

}
