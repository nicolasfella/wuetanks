package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.BattleTanks;

public class Bullet extends Entity {

	private int movingSpeed;
	private float dmg;
	private Player player;

	public Bullet(Player player, Vector2 position, Direction direction) {
		super("bullet" + player.getTank().getName());
		
		this.player = player;
		dmg = player.getTank().getDamage();

		width = BattleTanks.getPreferences().getInteger("bullet_width", 12);
		height = BattleTanks.getPreferences().getInteger("bullet_height", 26);
		movingSpeed = BattleTanks.getPreferences().getInteger("bullet_speed", 450);

		this.position = position.cpy();
		this.position.x -= width / 2.f;
		// this.position.y -= height/2.f;

		speed.x = (float) -Math.sin(Math.toRadians(direction.getRotation()));
		speed.y = (float) Math.cos(Math.toRadians(direction.getRotation()));
		

		sprite.setOrigin(sprite.getWidth() / 2.f, 0.f);
		sprite.setRotation(direction.getRotation());

	}

	public void update() {
		
		speed.nor();
		speed.scl(movingSpeed);
		
		super.update();
	}

	public float getDamage() {
		return dmg;
	}
	
	public Player getPlayer(){
		return player;
	}

}
