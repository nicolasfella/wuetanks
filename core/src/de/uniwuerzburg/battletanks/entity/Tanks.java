package de.uniwuerzburg.battletanks.entity;

public enum Tanks {
	/** low hp/armor, high damage */
	BEIGE(100f, 50f, 0.25f),

	/** medium hp/armor/damage */
	BLACK(150f, 40f, 0.25f),

	/** medium hp/armor/damage */
	BLUE(200f, 30f, 0.5f),

	/** medium hp/armor/damage */
	GREEN(250f, 20f, 0.75f),

	/** high hp/armor, low damage */
	RED(300f, 10f, 0.75f);

	
	
	private float health;
	private float damage;
	
	/**Reducing incoming dmg before hp subtraction*/
	private float armor;

	private Tanks(float health, float damage, float armor) {
		this.health = health;
		this.damage = damage;
		this.armor = armor;
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

	public float getHealth() {
		return health;
	}

	public float getDamage() {
		return damage;
	}

	public float getArmor() {
		return armor;
	}

}
