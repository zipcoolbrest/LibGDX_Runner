package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameScreen implements Screen{

    private RunnerGame runnerGame;
    private SpriteBatch batch;
    private TextureRegion textureBackground;
    private TextureRegion textureSend;
    private TextureRegion textureCactus;
    private TextureRegion textureBird;
    private TextureAtlas atlas;
    private Stage stage;
    private Skin skin;
    private boolean pause;
    private Group endGameGroup;

    private BitmapFont font32;
    private BitmapFont font96;

    private float groundHeight = 190.0f;
    private float playerAnchor = 200.0f;

    private Music music;
    private Sound jumpSound;

    private boolean gameOver;

    private  Player player;
    private Enemy[] enemies;

    private float time;

    TextureAtlas getAtlas() {
        return atlas;
    }

    float getGroundHeight() {
        return groundHeight;
    }

    float getPlayerAnchor() {
        return playerAnchor;
    }

    GameScreen(RunnerGame runnerGame, SpriteBatch batch) {
        this.runnerGame = runnerGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("runner.pack");
        textureBackground = atlas.findRegion("bg");
        textureSend = atlas.findRegion("ground");
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("laser.wav"));
        player = new Player(this, jumpSound);
        enemies = new Enemy[5];
        textureCactus = atlas.findRegion("cactus");
        textureBird = atlas.findRegion("bird");
        music = Gdx.audio.newMusic(Gdx.files.internal("Jumping bat.wav"));
        music.setLooping(true);
        music.play();
        enemies[0] = new Enemy(textureCactus, new Vector2(1400, groundHeight));
        for (int i = 1; i < 5 ; i++) {
            enemies[i] = new Enemy(textureCactus, new Vector2(enemies[i-1].getPosition().x + MathUtils.random(400, 900), groundHeight));
        }
        gameOver = false;
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
        HighScoreSystem.createTable();
        HighScoreSystem.loadTable();
        Gdx.input.setInputProcessor(null);
        pause = false;
        createGUI();
    }

    void createGUI(){
        stage = new Stage(runnerGame.getViewport(), batch);
        skin = new Skin(atlas);

     //GAME BUTTONS
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("menuBtn");
        textButtonStyle.font = font32;
        skin.add("tbs", textButtonStyle);
        TextButton btnPause = new TextButton("PAUSE", skin, "tbs");
        TextButton btnJump = new TextButton("JUMP", skin, "tbs");
        btnPause.setPosition(1000, 600);
        btnJump.setPosition(1000, 50);

        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pause = !pause;
            }
        });
        btnJump.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.tryToJump();
            }
        });
        stage.addActor(btnPause);
        stage.addActor(btnJump);


    // END_GAME_MENU
        skin.add("nameField", new Texture("nameField.bmp"));
        skin.add("cursor", new Texture("cursor.bmp"));
        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = font32;
        tfs.background = skin.getDrawable("nameField");
        tfs.fontColor = Color.WHITE;

        skin.add("tfs", tfs);

        final TextField field = new TextField("", skin, "tfs");
        field.setWidth(560);
        field.setPosition(20, 230);

        tfs.cursor = skin.getDrawable("cursor");

        TextButton btnExitToMenu = new TextButton("MENU", skin, "tbs");
        TextButton btnRestart = new TextButton("RESTART", skin, "tbs");
        btnRestart.setPosition(20, 20);
        btnExitToMenu.setPosition(320, 20);
        btnRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = field.getText();
                if (name.isEmpty()) {
                    name = "Player";
                }
                HighScoreSystem.updateTable(name, (int) player.getScore());
                restart();
            }
        });
        btnExitToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = field.getText();
                if (name.isEmpty()) {
                    name = "Player";
                }
                HighScoreSystem.updateTable(name, (int) player.getScore());
                runnerGame.switchScreens(RunnerGame.screens.MENU);
            }
        });

        endGameGroup = new Group();
        Image image = new Image(new Texture("end_game_panel.bmp"));
        endGameGroup.addActor(image);
        endGameGroup.addActor(field);
        endGameGroup.addActor(btnRestart);
        endGameGroup.addActor(btnExitToMenu);
        endGameGroup.setVisible(false);
        endGameGroup.setPosition(1280 /2 - 600 /2, 720 /2 - 300 /2);

        stage.addActor(endGameGroup);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(textureBackground, 0, 0);
        for (int i = 0; i < 8; i++) {
            batch.draw(textureSend, i*200 - player.getPosition().x%200, 0);
        }
        player.render(batch);
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].render(batch, player.getPosition().x - playerAnchor);
        }
        font32.draw(batch, "TOP SCORE: " + HighScoreSystem.getTopPlayreName() + " " + HighScoreSystem.getTopPlayerScore(), 22, 702);
        font32.draw(batch, "SCORE: " + (int)player.getScore(), 22, 662);
        if(player.isDoubleJumpAvailable()){
            font32.draw(batch, "DOUBLE JUMP READY", 0, 100, 1280, 1, false);
        }
        if(gameOver){
            font96.draw(batch,"GAME OVER", 0, 382, 1280, 1, false);
            font32.setColor(1,1,1, 0.5f + 0.5f*(float)Math.sin(time*5.0f));
            font32.draw(batch,"Tap To RESTART", 0, 282, 1280, 1, false);
            font32.setColor(1,1,1,1);
        }
        batch.end();
        stage.draw();
    }



    private float getRightestEnemy(){
        float maxValue = 0.0f;
        for (int i = 0; i < enemies.length; i++) {
            if(enemies[i].getPosition().x > maxValue){
                maxValue = enemies[i].getPosition().x;
            }
        }
        return maxValue;
    }

    private void restart(){
        endGameGroup.setVisible(false);
        gameOver = false;
        time = 0.0f;
        enemies[0].setup(textureCactus, 1400, groundHeight, 0, 0);
        for (int i = 1; i < enemies.length ; i++) {
            enemies[i].setup(textureCactus, enemies[i-1].getPosition().x + MathUtils.random(400, 900), groundHeight, 0, 0);
        }
        player.restart();
    }


    private void generateEnemy(int index){
        int maxType = 0;
        if (time > 10.0f){
            maxType = 1;
        }

        Enemy.Type type = Enemy.Type.values()[(int)(Math.random()*(maxType+1))];

        switch (type){
            case CACTUS:
                enemies[index].setup(textureCactus, getRightestEnemy() + MathUtils.random(400, 900), groundHeight, 0, 0 );
                break;
            case BIRD:
                enemies[index].setup(textureBird, getRightestEnemy() + MathUtils.random(400, 900), groundHeight+120, -100, 0 );
                break;
        }
    }

    private void update(float dt){
        if(!pause){
            time += dt;
            if(!gameOver) {
                player.update(dt);
                for (int i = 0; i < enemies.length; i++) {
                    enemies[i].update(dt);
                    if (enemies[i].getPosition().x < player.getPosition().x - playerAnchor - 80) {
                        generateEnemy(i);
                    }
                }
                for (int i = 0; i < enemies.length; i++) {
                    if (enemies[i].getEnemyHitArea().overlaps(player.getPlayerHitArea())) {
                        gameOver = true;
                        endGameGroup.setVisible(true);
                       // HighScoreSystem.updateTable("Player", (int)player.getScore());
                        break;
                    }
                }
            }else {
//                if( Gdx.input.justTouched()){
//                    restart();
//                }
            }
        }
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
        atlas.dispose();
        music.dispose();
        jumpSound.dispose();
        font96.dispose();
        font32.dispose();
        skin.dispose();
        stage.dispose();
    }
}
