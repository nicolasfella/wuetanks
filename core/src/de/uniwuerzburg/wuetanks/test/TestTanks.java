package de.uniwuerzburg.wuetanks.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uniwuerzburg.wuetanks.entity.Tanks;

public class TestTanks {

	@Test
	public void test() {
		Tanks t = Tanks.GREEN;

		assertEquals(100, t.getMaxHitpoints(), 0);
		assertEquals(30, t.getDamage(), 0);
		assertEquals(0.66f, t.getArmor(), 0);
		assertEquals(0.5f, t.getReloadTime(), 0);

	}

}
