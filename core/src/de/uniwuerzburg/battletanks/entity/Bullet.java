package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.BattleTanks;

public class Bullet extends Entity {

	private int movingSpeed;
	private float dmg;
	private Player player;

	private Direction direction;

	public Bullet(Player player, Vector2 position) {
		super("bullet" + player.getTank().getName());

		this.player = player;
		direction = player.getDirection();
		dmg = player.getTank().getDamage();

		width = BattleTanks.getPreferences().getInteger("bullet_width", 12);
		height = BattleTanks.getPreferences().getInteger("bullet_height", 26);
		movingSpeed = BattleTanks.getPreferences().getInteger("bullet_speed", 450);

		this.position = position.cpy();
		this.position.x -= width / 2.f;

		speed.x = (float) -Math.sin(Math.toRadians(direction.getRotation()));
		speed.y = (float) Math.cos(Math.toRadians(direction.getRotation()));

		sprite.setOrigin(width / 2.f, 0);
		sprite.setRotation(direction.getRotation());

		calculateCollisionRectangleSize();
		calculateCollisionRectangleVector();

	}

	public void update() {
		speed.nor();
		speed.scl(movingSpeed);

		super.update();
	}

	/** berechnet die breite und höhe des kollisionsrechtecks */
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

	/**
	 * berechnet einen vektor der ausgehend von der aktuellen position zur
	 * unteren linken ecke des kollisions-rechtsecks führt
	 */
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

	public float getDamage() {
		return dmg;
	}

	public Player getPlayer() {
		return player;
	}

}