package de.uniwuerzburg.wuetanks.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.wuetanks.entity.Bullet;
import de.uniwuerzburg.wuetanks.entity.Player;
import de.uniwuerzburg.wuetanks.entity.Tanks;

public class TestBullet {

	@Test
	public void test() {
		Bullet b = new Bullet(new Player(Tanks.GREEN, 0, 0, 0, 0, 0), new Vector2());

		assertEquals(Tanks.GREEN.getDamage(), b.getDamage(), 0);
		b.setMovingSpeed(20);
		assertEquals(20, b.getMovingSpeed());

	}

}
