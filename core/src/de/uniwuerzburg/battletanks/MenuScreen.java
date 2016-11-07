package de.uniwuerzburg.battletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {
	
	final BattleTanks game;
	
	Stage stage;
	ImageButton startButton;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
	OrthographicCamera camera;
	
	TiledMap map;
	
	boolean gameStarted = false;
	
	
	public MenuScreen(final BattleTanks game){
		this.game = game;
		gameStarted = false;
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
        create();
	}
	

	public void create() {
		stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        
        
        //Start Button mit Texturen und Listener
        Texture startButtonTexture = new Texture(Gdx.files.internal("startButton.png"));
        TextureRegion startButtonTextureRegion = new TextureRegion(startButtonTexture);
        TextureRegionDrawable startButtonTextRegionDrawable = new TextureRegionDrawable(startButtonTextureRegion);
        startButton = new ImageButton(startButtonTextRegionDrawable);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed");
                gameStarted = true;
            }
        });
        
        stage.addActor(startButton);
	}
	
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        
        stage.draw();
        
        if(gameStarted){
        	game.setScreen(new GameScreen(game));
        	dispose();
        }

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	

}
