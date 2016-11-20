package de.uniwuerzburg.battletanks.entity;

public enum Direction {

	UP(0), UPRIGHT(315), RIGHT(270), DOWNRIGHT(225), DOWN(180), DOWNLEFT(135), LEFT(90), UPLEFT(45);

	float rotation;

	Direction(float rotation){
		this.rotation = rotation;
	}

	public float getRotation(){
		return rotation;
	}

}
