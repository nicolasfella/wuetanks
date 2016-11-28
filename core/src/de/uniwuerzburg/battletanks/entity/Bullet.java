package de.uniwuerzburg.battletanks.entity;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.BattleTanks;

public class Bullet extends Entity {

	private int movingSpeed;
	private float dmg;
	private Player player;

	private Vector2 toLeftBottomCorner;
	private float renderWidth;
	private float renderHeight;

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

		calculateRealSize();
		calculateVectorToLeftBottomCorner();

	}

	public void update() {
		speed.nor();
		speed.scl(movingSpeed);

		super.update();
	}

	/**
	 * vertauscht width und height bei links und rechtsdrehung oder berechnet die
	 * breite und höhe eines quadrates, das den großteil der kugel abdeckt
	 */
	private void calculateRealSize() {

		switch (direction) {

		case LEFT:
		case RIGHT:
			renderWidth = height;
			renderHeight = width;
			break;

		case UPLEFT:
		case UPRIGHT:
		case DOWNLEFT:
		case DOWNRIGHT:
			renderWidth = (float) (height / Math.sqrt(2));
			renderHeight = renderWidth;
			break;

		default:
			renderWidth = width;
			renderHeight = height;
			break;
		}

	}

	/**
	 * berechnet einen vektor der ausgehend von der aktuellen position zur
	 * unteren linken ecke der kugel bzw. dem quadrat, das die kugel
	 * abdeckt, verläuft
	 */
	private void calculateVectorToLeftBottomCorner() {
		toLeftBottomCorner = new Vector2();

		switch (direction) {
		case RIGHT:
			toLeftBottomCorner.x = width / 2.f;
			toLeftBottomCorner.y = -width / 2.f;
			break;

		case DOWN:
			toLeftBottomCorner.y = -height;
			break;

		case LEFT:
			toLeftBottomCorner.x = -height + width / 2f;
			toLeftBottomCorner.y = -width / 2f;
			break;

		case UPRIGHT:
			toLeftBottomCorner.x = width / 2f;
			break;

		case DOWNRIGHT:
			toLeftBottomCorner.x = width / 2f;
			toLeftBottomCorner.y = -renderHeight;
			break;

		case DOWNLEFT:
			toLeftBottomCorner.x = width / 2f - renderWidth;
			toLeftBottomCorner.y = -renderHeight;
			break;

		case UPLEFT:
			toLeftBottomCorner.x = width / 2f - renderWidth;
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

	public Vector2 getBottomLeftCorner() {
		return position.cpy().add(toLeftBottomCorner);
	}

	public float getBottomLeftCornerX() {
		return position.x + toLeftBottomCorner.x;
	}

	public float getBottomLeftCornerY() {
		return position.y + toLeftBottomCorner.y;
	}

	public float getRenderWidth() {

		return renderWidth;
	}

	public float getRenderHeight() {

		return renderHeight;
	}

}
