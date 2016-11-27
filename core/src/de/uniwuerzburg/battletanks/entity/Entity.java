package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

		position = new Vector2();
		oldPosition = new Vector2();
		speed = new Vector2();

		sprite = BattleTanks.getTextureAtlas().createSprite(spriteName);
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
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
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
		sprite = BattleTanks.getTextureAtlas().createSprite(spriteName);
	}

	@Override
	public void dispose() {
	}
}
