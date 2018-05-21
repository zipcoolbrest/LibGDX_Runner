package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RunnerGame extends Game {

	public enum screens{
		MENU, GAME, SCORE
	}

	private SpriteBatch batch;
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private Viewport viewport;

	Viewport getViewport() {
		return viewport;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(this, batch);
		menuScreen = new MenuScreen(this, batch);
		viewport = new FitViewport(1280, 720);
		setScreen(menuScreen);
		switchScreens(screens.MENU);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();

		getScreen().render(dt);
	}



	public void switchScreens(screens type){
		Screen currentScreen = getScreen();
		if(currentScreen != null){
			currentScreen.dispose();
		}
		switch(type){
			case GAME:
				setScreen(gameScreen);
				break;
			case MENU:
				setScreen(menuScreen);
				break;
			case SCORE:
				setScreen(menuScreen);
				break;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		getScreen().dispose();
	}
}
