package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen{

    private RunnerGame runnerGame;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private TextureRegion textureRegionBackground;
    private BitmapFont font96;
    private BitmapFont font32;


    private Stage stage;
    private Skin skin;

    public MenuScreen(RunnerGame runnerGame, SpriteBatch batch) {
        this.runnerGame = runnerGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("runner.pack");
        textureRegionBackground = atlas.findRegion("bg");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = -3;
        parameter.shadowColor = Color.BLACK;
        font32 = generator.generateFont(parameter);
        parameter.size = 96;
        font96 = generator.generateFont(parameter);
        generator.dispose();
        createGUI();
    }

    void createGUI(){
        stage = new Stage(runnerGame.getViewport(), batch);
        skin = new Skin(atlas);
        Gdx.input.setInputProcessor(stage);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("menuBtn");
        textButtonStyle.font = font32;
        skin.add("tbs", textButtonStyle);

        TextButton btnNewGame = new TextButton("START", skin, "tbs");
       // TextButton btnScoreGame = new TextButton("SCORE", skin, "tbs");
        TextButton btnExitGame = new TextButton("EXIT", skin, "tbs");
        btnNewGame.setPosition(520, 250);
        //btnScoreGame.setPosition(520, 250);
        btnExitGame.setPosition(520, 100);
        stage.addActor(btnNewGame);
        //stage.addActor(btnScoreGame);
        stage.addActor(btnExitGame);

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnerGame.switchScreens(RunnerGame.screens.GAME);
            }
        });

    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        batch.draw(textureRegionBackground, 0, 0);
        font96.draw(batch, "RUNNER GAME", 0, 600, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    public void update(float dt){
        stage.act(dt);
    }
    @Override
    public void resize(int width, int height) {
        runnerGame.getViewport().update(width, height,true);
        runnerGame.getViewport().apply();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font96.dispose();
        font32.dispose();
        atlas.dispose();
        skin.dispose();
        stage.dispose();
    }
}
