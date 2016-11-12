package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents an object in the game world.
 */
public class Entity {

    protected Vector2 position;

    protected Vector2 oldPosition;
    protected int width, height;

    protected Vector2 speed;

    protected Sprite sprite;

    public Entity() {
        this("empty");
    }

    public Entity(String spriteName) {

        position = new Vector2();
        oldPosition = new Vector2();
        speed = new Vector2();

        sprite = BattleTanks.getTextureAtlas().createSprite(spriteName);
        sprite.setPosition(position.x, position.y);
    }

    public void update() {
        oldPosition = position.cpy();
        position.add(speed.scl(Gdx.graphics.getDeltaTime()));
    }

    public void render(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);
        sprite.draw(batch);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        position.x = x;
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getOldPosition() {
        return oldPosition;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    public void setSprite(String spriteName){
        sprite = BattleTanks.getTextureAtlas().createSprite(spriteName);
    }
}
