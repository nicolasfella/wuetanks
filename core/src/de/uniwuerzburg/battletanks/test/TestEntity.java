package de.uniwuerzburg.battletanks.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uniwuerzburg.battletanks.entity.Entity;

public class TestEntity {

	@Test
	public void test() {
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
