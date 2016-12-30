package de.uniwuerzburg.battletanks.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uniwuerzburg.battletanks.test.mock.MockBattleTanks;

@RunWith(Suite.class)
@SuiteClasses({ TestBullet.class, TestEntity.class, TestObstacle.class, TestPlayer.class, TestTanks.class })
public class AllTests {

	@BeforeClass
	public static void init() {
		new MockBattleTanks();
	}

}
