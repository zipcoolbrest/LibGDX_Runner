package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

class Player {

    private GameScreen gameScreen;
    private TextureRegion [][] texturePlayer;
    private Vector2 position;
    private Vector2 velocity;
    private float score;
    private float time;
    private Circle playerHitArea;
    private Sound jumpSound;
    private float doubleJumpTime;
    private boolean jumpRequest;
    private float angle;

    private final int HEIGHT = 100;
    private final int WIDTH = 100;

    boolean isDoubleJumpAvailable(){
        return doubleJumpTime <= 0.0f;
    }

    void tryToJump(){
        jumpRequest = true;
    }

    float getScore() {
        return score;
    }

    Vector2 getPosition() {
        return position;
    }

    Circle getPlayerHitArea() {
        return playerHitArea;
    }

    Player(GameScreen gameScreen, Sound jumpSound){
        this.gameScreen = gameScreen;
        this.texturePlayer = gameScreen.getAtlas().findRegion("runner").split(WIDTH, HEIGHT);
        this.position = new Vector2(0, 190);
        this.velocity = new Vector2(240.0f, 0.0f);
        this.score = 0;
        this.playerHitArea = new Circle(position.x +WIDTH /2, position.y+ HEIGHT /2, WIDTH /3);
        this.jumpSound = jumpSound;
    }

    void restart(){
        position.set(0, gameScreen.getGroundHeight());
        score = 0;
        doubleJumpTime = 5.0f;
        velocity.set(240.0f, 0.0f);
        playerHitArea.setPosition(position.x + WIDTH /2, position.y + HEIGHT /2);
    }

    void render(SpriteBatch batch){
        int frame = (int)(time / 0.1f);
        frame = frame % 6;
        batch.draw(texturePlayer[0][frame], gameScreen.getPlayerAnchor(), position.y, 50, 50, WIDTH, HEIGHT, 1, 1, angle);
    }

    void update(float dt){
        if(angle > 0.0f){
            if(angle > 360){
                angle += 0.2f + dt;
            }else {
                angle += 7.0f + dt;
            }
        }
        if (position.y > gameScreen.getGroundHeight()){
            velocity.y -= 720.0f * dt;
        }else{
            angle = 0.0f;
            position.y = gameScreen.getGroundHeight();
            velocity.y = 0.0f;
            time += velocity.x * dt / 300.0f;

        }
        if( doubleJumpTime > 0){
            doubleJumpTime -= dt;
        }
        if(jumpRequest && (position.y <= gameScreen.getGroundHeight() || doubleJumpTime <= 0.0f)){
            velocity.y = 420.0f;
            jumpSound.play();
            if(position.y > gameScreen.getGroundHeight()){
                doubleJumpTime = 3.0f;
                angle = 1.0f;
            }
        }
        position.mulAdd(velocity, dt);
        velocity.x += 8.0f * dt;
        score += velocity.x*dt / 5.0f;
        playerHitArea.setPosition(position.x + WIDTH /2, position.y + HEIGHT /2);
        jumpRequest = false;
    }

}

