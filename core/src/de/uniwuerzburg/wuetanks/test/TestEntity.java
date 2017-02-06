package de.uniwuerzburg.wuetanks.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

import de.uniwuerzburg.wuetanks.Wuetanks;
import de.uniwuerzburg.wuetanks.entity.Entity;
import de.uniwuerzburg.wuetanks.test.mock.MockWuetanks;

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

		float[] xvals = { 0, 1, 20, -300 };
		float[] yvals = { 40, 0, 1, -20090 };

		for (int i = 0; i < xvals.length; i++) {
			e.setX(xvals[i]);
			assertEquals(xvals[i], e.getX(), 0);

			e.setY(yvals[i]);
			assertEquals(yvals[i], e.getY(), 0);

			e.setSpeed(new Vector2(xvals[i], yvals[i]));
			assertEquals(xvals[i], e.getSpeed().x, 0);
			assertEquals(yvals[i], e.getSpeed().y, 0);

			e.setPosition(xvals[i] - 5, yvals[i] - 10);
			assertEquals(xvals[i] - 5, e.getX(), 0);
			assertEquals(yvals[i] - 10, e.getY(), 0);

		}

		int[] wvals = { 0, 1, 30, 140 };
		int[] hvals = { 40, 0, 1, 50 };

		for (int i = 0; i < hvals.length; i++) {
			e.setHeight(hvals[i]);
			assertEquals(hvals[i], e.getHeight());
			e.setWidth(wvals[i]);
			assertEquals(wvals[i], e.getWidth());
		}

		boolean nullExceptionThrown = false;

		try {
			Entity e2 = new Entity("");
		} catch (NullPointerException ex) {
			nullExceptionThrown = true;
		}

		assertTrue(nullExceptionThrown);

	}

}
