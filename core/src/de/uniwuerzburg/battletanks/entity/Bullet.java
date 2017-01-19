package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.BattleTanks;

public class Bullet extends Entity {

	private int movingSpeed;
	private float dmg;
	private Player player;
	private Direction direction;

	/**
	 * Creates a bullet flying in the direction of the player.
	 * 
	 * @param player
	 *            who shot the bullet
	 * @param position
	 *            of the bottom center of the bullet
	 */

	public Bullet(Player player, Vector2 position) {
		super("bullet" + player.getTank().getName());

		this.player = player;
		direction = player.getDirection();
		dmg = player.getTank().getDamage();

		width = BattleTanks.getInstance().getPreferences().getInteger("bullet_width", 12);
		height = BattleTanks.getInstance().getPreferences().getInteger("bullet_height", 26);
		movingSpeed = BattleTanks.getInstance().getPreferences().getInteger("bullet_speed", 450);

		this.position = position.cpy();
		this.position.x -= width / 2.f;

		// calculates the speed from the direction
		speed.x = (float) -Math.sin(Math.toRadians(direction.getRotation()));
		speed.y = (float) Math.cos(Math.toRadians(direction.getRotation()));

		// sets the rotation center to the bottom center of the bullet and
		// rotates it
		sprite.setOrigin(width / 2.f, 0);
		sprite.setRotation(direction.getRotation());

		calculateCollisionRectangleSize();
		calculateCollisionRectangleVector();

	}

	/**
	 * Called each frame, moves the bullet.
	 */

	public void update() {
		speed.nor();
		speed.scl(movingSpeed);

		super.update();
	}

	// Calculates the width and height of the collision rectangle. If the bullet
	// is rotated diagonal, then a horizontal collision rectangle is used which
	// diagonal is as long as the height of the bullet.

	private void calculateCollisionRectangleSize() {

		switch (direction) {

		case LEFT:
		case RIGHT:
			collisionRectangleWidth = height;
			collisionRectangleHeight = width;
			break;

		case UPLEFT:
		case UPRIGHT:
		case DOWNLEFT:
		case DOWNRIGHT:
			collisionRectangleWidth = (float) (height / Math.sqrt(2));
			collisionRectangleHeight = collisionRectangleWidth;
			break;

		default:
			collisionRectangleWidth = width;
			collisionRectangleHeight = height;
			break;
		}

	}

	// Calculates the vector from the current position to the bottom left corner
	// of the collision rectangle.

	private void calculateCollisionRectangleVector() {
		switch (direction) {
		case RIGHT:
			toCollisionRectangleVector.x = width / 2.f;
			toCollisionRectangleVector.y = -width / 2.f;
			break;

		case DOWN:
			toCollisionRectangleVector.y = -height;
			break;

		case LEFT:
			toCollisionRectangleVector.x = -height + width / 2f;
			toCollisionRectangleVector.y = -width / 2f;
			break;

		case UPRIGHT:
			toCollisionRectangleVector.x = width / 2f;
			break;

		case DOWNRIGHT:
			toCollisionRectangleVector.x = width / 2f;
			toCollisionRectangleVector.y = -collisionRectangleHeight;
			break;

		case DOWNLEFT:
			toCollisionRectangleVector.x = width / 2f - collisionRectangleWidth;
			toCollisionRectangleVector.y = -collisionRectangleHeight;
			break;

		case UPLEFT:
			toCollisionRectangleVector.x = width / 2f - collisionRectangleWidth;
			break;

		case UP:
			break;
		}
	}

	/**
	 * 
	 * @return damage of the bullet
	 */

	public float getDamage() {
		return dmg;
	}

	/**
	 * 
	 * @return player who created the bullet
	 */

	public Player getPlayer() {
		return player;
	}

	public void setMovingSpeed(int speed) {
		this.movingSpeed = speed;
	}

	public int getMovingSpeed() {
		return movingSpeed;
	}

}