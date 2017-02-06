package de.uniwuerzburg.wuetanks.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uniwuerzburg.wuetanks.test.mock.MockWuetanks;

@RunWith(Suite.class)
@SuiteClasses({ TestBullet.class, TestEntity.class, TestObstacle.class, TestPlayer.class, TestTanks.class })
public class AllTests {

	@BeforeClass
	public static void init() {
		new MockWuetanks();
	}

}
