package de.uniwuerzburg.battletanks.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.battletanks.entity.Bullet;
import de.uniwuerzburg.battletanks.entity.Direction;
import de.uniwuerzburg.battletanks.entity.Player;
import de.uniwuerzburg.battletanks.entity.Tanks;
import de.uniwuerzburg.battletanks.test.mock.MockBattleTanks;

public class TestPlayer {

	@Test
	public void test() {

		Player p = new Player(Tanks.GREEN, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT);

		assertEquals(Keys.UP, p.getKey_up());
		assertEquals(Keys.DOWN, p.getKey_down());
		assertEquals(Keys.LEFT, p.getKey_left());
		assertEquals(Keys.RIGHT, p.getKey_right());
		assertEquals(Keys.CONTROL_RIGHT, p.getKey_shoot());

		p.setKey_down(Keys.S);
		p.setKey_left(Keys.A);
		p.setKey_right(Keys.D);
		p.setKey_up(Keys.W);
		p.setKey_shoot(Keys.SPACE);

		assertEquals(Keys.W, p.getKey_up());
		assertEquals(Keys.S, p.getKey_down());
		assertEquals(Keys.A, p.getKey_left());
		assertEquals(Keys.D, p.getKey_right());
		assertEquals(Keys.SPACE, p.getKey_shoot());

		p.setMovingSpeed(40);
		assertEquals(40, p.getMovingSpeed());

		p.setNumber(3);
		assertEquals(3, p.getNumber());

		p.setDirection(Direction.UPLEFT);
		assertEquals(Direction.UPLEFT, p.getDirection());

		Bullet b = new Bullet(p, new Vector2());
		float before = p.getCurrentHitpoints();
		p.hitPlayer(b);
		float after = p.getCurrentHitpoints();
		float expected = before - (1 - p.getTank().getArmor()) * p.getTank().getDamage();
		assertEquals(expected, after, 0);

	}

}
