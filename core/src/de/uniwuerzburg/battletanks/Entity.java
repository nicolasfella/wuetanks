package de.uniwuerzburg.battletanks;

/* TODO
 * Use Texturemap instead of single textures 
 * 
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author nico Represents an object in the game world.
 */
public class Entity {

	protected Vector2 position;
	protected int width, height;

	protected Vector2 speed;

	protected Sprite sprite;

	public Entity(String spritePath) {

		position = new Vector2();
		speed = new Vector2();

		Texture texture = new Texture(Gdx.files.internal(spritePath));
		sprite = new Sprite(texture);
		sprite.setPosition(position.x, position.y);
	}

	public void update() {
		position.add(speed.scl(Gdx.graphics.getDeltaTime()));
	}

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

}
