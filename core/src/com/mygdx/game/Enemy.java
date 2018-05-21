package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

class Enemy {
    public enum  Type{
        CACTUS, BIRD
    }
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private Circle enemyHitArea;
    private final int WIDTH = 80;
    private final int HEIGHT = 80;


    Vector2 getPosition() {
        return position;
    }

    Circle getEnemyHitArea() {
        return enemyHitArea;
    }

    Enemy(TextureRegion texture, Vector2 position) {
        this.texture = texture;
        this.position = position;
        this.velocity = new Vector2(0,0);
        this.enemyHitArea = new Circle(position.x + WIDTH /2, position.y + HEIGHT /2, WIDTH /3);
    }

    void setup(TextureRegion texture, float x, float y, float vx, float vy){
        this.texture = texture;
        this.position.set(x,y);
        this.velocity.set(vx, vy);
        this.enemyHitArea.setPosition(position.x + WIDTH /2, position.y + HEIGHT /2);
    }

    void render(SpriteBatch batch, float worldX){
        batch.draw(texture, position.x - worldX, position.y);
    }

    void update(float dt){
        position.mulAdd(velocity, dt);
        enemyHitArea.setPosition(position.x + WIDTH /2, position.y + HEIGHT /2);
    }
}
