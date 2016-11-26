package de.uniwuerzburg.battletanks.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public enum Tanks {
	
	BEIGE("Beige", 100f, 72f, 0.25f, 0.85f),

	
	BLACK("Black", 155f, 20f, 0.2f, 0.38f),

	
	BLUE("Blue", 179f, 20f, 0.4f, 0.5f),

	
	GREEN("Green", 100f, 30f, 0.66f, 0.5f),

	
	RED("Red", 150f, 10f, 0.66f, 0.25f);

	private float maxHitpoints;
	private float damage;
	private String name;

	/** reloadTime is in seconds */
	private float reloadTime;

	/** Reducing incoming dmg before hp subtraction */
	private float armor;

	private Sound shotSound;

	private Tanks(String name, float maxHitpoints, float damage, float armor, float reloadTime) {
		this.name = name;
		this.maxHitpoints = maxHitpoints;
		this.damage = damage;
		this.armor = armor;
		this.reloadTime = reloadTime;
		shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
	}

	/** funktion zur berechnung der stärke eines tanks */
	public float getStrength() {

		// eventuell kann man eine höhere nachladezeit noch als weniger stark
		// einstufen, da eine höhere präzision benötigt wird

		// schaden pro sekunde
		float dps = getDPS();

		// mehr armor wirkt sich genauso aus wie eine prozentuale erhöhung der
		// hp
		float life = getLife();

		// das verhältnis zwischen schaden und leben sollte indirekt
		// proportional sein
		// -> mehr leben gleicht weniger schaden aus

		return dps * life;

	}
	
	public float getDPS(){
		return damage/reloadTime;
	}
	
	public float getLife(){
		return maxHitpoints * (1f + armor);
	}
	
	

	public float calculateDamage(float dmg) {
		return (1.f - armor) * dmg;
	}

	public void setMaxHitpoints(float maxHitpoints) {
		this.maxHitpoints = maxHitpoints;
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

	public float getMaxHitpoints() {
		return maxHitpoints;
	}

	public float getDamage() {
		return damage;
	}

	public float getArmor() {
		return armor;
	}

	public float getReloadTime() {
		return reloadTime;
	}

	public Sound getShotSound() {
		return shotSound;
	}

}
