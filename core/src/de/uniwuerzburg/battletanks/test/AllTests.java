package de.uniwuerzburg.battletanks.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestBullet.class, TestEntity.class, TestObstacle.class, TestPlayer.class })
public class AllTests {

}
