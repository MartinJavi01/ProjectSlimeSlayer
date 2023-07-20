package entities.enemies;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//main class that manages all the enemies, it has methods that check the health of the mob and sets the primitive
//stats of the mob
import entities.Entity;
import entities.player.Player;
import tileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public abstract class Enemy extends Entity {

    protected int hitDamage;
    protected int detectionRange;
    protected boolean shouldAttack;
    protected Player player;
    protected boolean shouldBeRemoved;

    public Enemy(TileMap tm, Player player) {
        super(tm);
        shouldAttack = false;
        this.player = player;
    }

    public void takeDamage(int damage){
        if(flinching){
            return;
        }
        knockbacking = true;
        knockBackTimer = System.nanoTime();
        flinching = true;
        flinchTimer = System.nanoTime();
        health -= damage;
        if(health < 0){
            health = 0;
        }
        if(health == 0){
            dead = true;
        }
    }

    public void followPlayer(){}

    public void checkEnemyOwnCollisions(ArrayList<Enemy> enemies){
    }

    public void tick(){
        if(shouldAttack){
            followPlayer();
        }
        super.tick();
    }
    public void render(Graphics2D g){
        super.render(g);
    }
    public int getHitDamage() {
        return hitDamage;
    }
    public Rectangle getDetectionRec(){
        return new Rectangle(getTotalX() - detectionRange * tileSize / 2,
                getTotalY() - detectionRange * tileSize / 2,
                detectionRange * tileSize,detectionRange * tileSize);
    }
    public void setShouldAttack(boolean b){
        shouldAttack = b;
    }
    public void stopEnemies(){
        left = right = up = down = false;
    }
    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }
}
