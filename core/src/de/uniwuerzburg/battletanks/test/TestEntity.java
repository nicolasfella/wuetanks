package de.uniwuerzburg.battletanks.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uniwuerzburg.battletanks.BattleTanks;
import de.uniwuerzburg.battletanks.entity.Entity;
import de.uniwuerzburg.battletanks.test.mock.MockBattleTanks;

public class TestEntity {

	@Test
	public void test() {
		
		MockBattleTanks mb = new MockBattleTanks();
		
		Entity e = new Entity();

		boolean widthExceptionThrown = false;

		try {
			e.setWidth(-60);
		} catch (IllegalArgumentException ex) {
			widthExceptionThrown = true;
		}

		assertTrue(widthExceptionThrown);

		boolean heightExceptionThrown = false;

		try {
			e.setHeight(-60);
		} catch (IllegalArgumentException ex) {
			heightExceptionThrown = true;
		}

		assertTrue(widthExceptionThrown);
		assertTrue(heightExceptionThrown);
	}

}
