package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import de.uniwuerzburg.battletanks.BattleTanks;

/**
 * Represents an object in the game world.
 */
public class Entity implements Disposable {

	/**
	 * The position of the entity
	 */
	protected Vector2 position;

	/**
	 * The vector to the position of the rectangle which defines the collision
	 * object
	 */
	protected Vector2 toCollisionRectangleVector;

	/** The width of the collision rectangle */
	protected float collisionRectangleWidth;

	/** The height of the collision rectangle */
	protected float collisionRectangleHeight;

	/**
	 * The position of the entity in the previous frame
	 */
	protected Vector2 oldPosition;

	/**
	 * The width of the entity
	 */
	protected int width;

	/**
	 * The height of the entity
	 */
	protected int height;

	/**
	 * The speed of the entity, in units per second
	 */
	protected Vector2 speed;

	/**
	 * The texture of the entity, loaded from the TextureAtlas
	 */
	protected Sprite sprite;

	/**
	 * Creates an entity at (0,0) with an empty texture
	 */
	public Entity() {
		this("empty");
	}

	/**
	 * Creates an entity at (0,0) with given texture
	 * 
	 * @param spriteName
	 *            the name of the texture in the TextureAtlas
	 */
	public Entity(String spriteName) {

		if (spriteName == null) {
			throw new NullPointerException("No Spritename given");
		}

		position = new Vector2();
		oldPosition = new Vector2();
		toCollisionRectangleVector = new Vector2();
		speed = new Vector2();

		width = 0;
		height = 0;

		collisionRectangleWidth = 0;
		collisionRectangleHeight = 0;

		sprite = BattleTanks.getInstance().getTextureAtlas().createSprite(spriteName);

		if (sprite == null) {
			throw new IllegalArgumentException(spriteName + " was not found in TextureAtlas");
		}

		sprite.setPosition(position.x, position.y);
	}

	/**
	 * Used for game logic, called each frame
	 */
	public void update() {
		oldPosition = position.cpy();
		position.add(speed.scl(Gdx.graphics.getDeltaTime()));
	}

	/**
	 * Draws the entity to the given SpriteBatch
	 * 
	 * @param batch
	 *            The SpriteBatch the entity is drawn to
	 */
	public void render(SpriteBatch batch) {
		sprite.setPosition(position.x, position.y);
		sprite.setSize(width, height);
		sprite.draw(batch);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {

		if (height < 0) {
			throw new IllegalArgumentException("Height cannot be less than zero");
		}

		this.height = height;
		if (collisionRectangleHeight == 0) {
			collisionRectangleHeight = height;
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {

		if (width < 0) {
			throw new IllegalArgumentException("Width cannot be less than zero");
		}

		this.width = width;
		if (collisionRectangleWidth == 0) {
			collisionRectangleWidth = width;
		}
	}

	public Vector2 getSpeed() {
		return speed;
	}

	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}

	public float getX() {
		return position.x;
	}

	public void setX(float x) {
		position.x = x;
	}

	public float getY() {
		return position.y;
	}

	public void setY(float y) {
		position.y = y;
	}

	/**
	 * @return The Rectangle, which should be used for collision detection
	 */
	public Rectangle getCollisionRectangle() {
		Vector2 rectPos = position.cpy().add(toCollisionRectangleVector);
		return new Rectangle(rectPos.x, rectPos.y, collisionRectangleWidth, collisionRectangleHeight);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public Vector2 getOldPosition() {
		return oldPosition;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setSprite(String spriteName) {
		sprite = BattleTanks.getInstance().getTextureAtlas().createSprite(spriteName);
	}

	@Override
	public void dispose() {
	}
}
