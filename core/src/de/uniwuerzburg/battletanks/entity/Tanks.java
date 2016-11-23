package de.uniwuerzburg.battletanks.entity;

public enum Tanks {
	/** low hp/armor, high damage */
	BEIGE("Beige", 100f, 50f, 0.25f, 5.f),

	/** medium hp/armor/damage */
	BLACK("Black", 150f, 40f, 0.25f, 4.f),

	/** medium hp/armor/damage */
	BLUE("Blue", 200f, 30f, 0.5f, 3.f),

	/** medium hp/armor/damage */
	GREEN("Green", 250f, 20f, 0.75f, 2.f),

	/** high hp/armor, low damage */
	RED("Red", 300f, 10f, 0.75f, 1.f);

	private float health;
	private float damage;
	private String name;
	
	/** reloadTime is in seconds */
	private float reloadTime;
	
	/** Reducing incoming dmg before hp subtraction */
	private float armor;

	private Tanks(String name, float health, float damage, float armor, float reloadTime) {
		this.name = name;
		this.health = health;
		this.damage = damage;
		this.armor = armor;
		this.reloadTime = reloadTime;
	}

	public void calculateDamage() {

	}

	public void setHealth(float health) {
		this.health = health;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setArmor(float armor) {
		this.armor = armor;
	}

	public String getName() {
		return name;
	}

	public float getHealth() {
		return health;
	}

	public float getDamage() {
		return damage;
	}

	public float getArmor() {
		return armor;
	}
	
	public float getReloadTime(){
		return reloadTime;
	}

}
