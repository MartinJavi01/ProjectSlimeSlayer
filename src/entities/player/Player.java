package entities.player;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//class that manages all the player collisions,animations and interactions with the entities and the tiles
import entities.Entity;
import entities.enemies.Enemy;
import tileMap.TileMap;
import tools.GlobalVariables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {


    private ArrayList<BufferedImage[]> sprites;
    private int[] numSpritesPerRow;

    private int walkingDown;
    private int walkingRight;
    private int walkingLeft;
    private int walkingUp;
    private int meleeDown;
    private int meleeRight;
    private int meleeLeft;
    private int meleeUp;
    private int bowDown;
    private int bowRight;
    private int bowLeft;
    private int bowUp;
    private int idle;

    private boolean attacking;
    private int punchRange;
    private int punchDamage;
    private boolean attackingBow;

    public Player(TileMap tm) {
        super(tm);

        width = 16;
        height = 24;
        cwidth = 10;
        cheight = 16;

        moveSpeed = 0.5;
        maxMoveSpeed = 1.5;
        stopSpeed = 0.5;

        walkingDown = 0;
        walkingRight = 1;
        walkingLeft = 2;
        walkingUp = 3;
        meleeDown = 4;
        meleeRight = 5;
        meleeLeft = 6;
        meleeUp = 7;
        bowDown = 8;
        bowRight = 9;
        bowLeft = 10;
        bowUp = 11;
        idle = 12;

        attacking = false;
        punchRange = 8;
        punchDamage = 2;
        attackingBow = false;
        flinching = false;
        health = maxHealth = 10;
        dead = false;

        flinchTime = 60;

        health = GlobalVariables.currentHealth;

        try {
            tileSet = ImageIO.read(this.getClass().getResourceAsStream("/sprites/player/ForestHero.png"));
            numSpritesPerRow = new int[]{6, 6, 6, 6, 3, 3, 3, 3, 3, 3, 3, 3};
            sprites = new ArrayList<BufferedImage[]>();
            for (int row = 0; row < numSpritesPerRow.length; row++) {
                BufferedImage[] bi = new BufferedImage[numSpritesPerRow[row]];
                for (int col = 0; col < bi.length; col++) {
                    bi[col] = tileSet.getSubimage(col * width, row * height, width, height);
                }
                sprites.add(bi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentAction = idle;
        anim.setFrames(sprites.get(meleeDown));
        anim.setDelay(-1);
    }

    public void checkAttackingStuff() {
        if (attacking) {
            if (lookingDirection == 0 && currentAction != meleeUp) {
                currentAction = meleeUp;
                anim.setFrames(sprites.get(meleeUp));
                anim.setDelay(120);
                currentAction = meleeUp;
            } else if (lookingDirection == 2 && currentAction != meleeDown) {
                currentAction = meleeDown;
                anim.setFrames(sprites.get(meleeDown));
                anim.setDelay(120);
                currentAction = meleeDown;
            } else if (lookingDirection == 3 && currentAction != meleeLeft) {
                currentAction = meleeLeft;
                anim.setFrames(sprites.get(meleeLeft));
                anim.setDelay(120);
                currentAction = meleeLeft;
            } else if (lookingDirection == 1 && currentAction != meleeRight) {
                currentAction = meleeRight;
                anim.setFrames(sprites.get(meleeRight));
                anim.setDelay(120);
                currentAction = meleeRight;
            } else if (currentAction == idle) {
                anim.setFrames(sprites.get(meleeDown));
                anim.setDelay(120);
                currentAction = meleeDown;
            }
        }
        if (attacking && anim.getTimesPlayed() >= 1) {
            attacking = false;
        }
    }

    private void checkAnimation() {
        if (attacking || attackingBow) {
            return;
        }
        if (down) {
            if (currentAction != walkingDown) {
                currentAction = walkingDown;
                anim.setFrames(sprites.get(walkingDown));
                anim.setDelay(90);
                lookingDirection = 2;
            }
        } else if (left) {
            if (currentAction != walkingLeft) {
                currentAction = walkingLeft;
                anim.setFrames(sprites.get(walkingLeft));
                anim.setDelay(90);
                lookingDirection = 3;
            }
        } else if (right) {
            if (currentAction != walkingRight) {
                currentAction = walkingRight;
                anim.setFrames(sprites.get(walkingRight));
                anim.setDelay(90);
                lookingDirection = 1;
            }
        } else if (up) {
            if (currentAction != walkingUp) {
                currentAction = walkingUp;
                anim.setFrames(sprites.get(walkingUp));
                anim.setDelay(90);
                lookingDirection = 0;
            }
        } else {
            if (currentAction != idle) {
                currentAction = idle;
                if(lookingDirection == 0){
                    anim.setFrames(sprites.get(meleeUp));
                    anim.setDelay(-1);
                }
                if(lookingDirection == 1){
                    anim.setFrames(sprites.get(meleeRight));
                    anim.setDelay(-1);
                }
                if(lookingDirection == 2){
                    anim.setFrames(sprites.get(meleeDown));
                    anim.setDelay(-1);
                }
                if(lookingDirection == 3){
                    anim.setFrames(sprites.get(bowLeft));
                    anim.setDelay(-1);
                }
            }
        }
    }

    public void checkEnemyCollisions(ArrayList<Enemy> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            if(this.getRect().intersects(e.getDetectionRec())){
                e.setShouldAttack(true);
            }else{
                e.setShouldAttack(false);
            }

            if(attacking && anim.getCurrentFrame() > 0){
                if(lookingDirection == 0 && this.dir0Rec().intersects(e.getRect())){
                    e.takeDamage(punchDamage);
                    e.setKnockBackDirection(lookingDirection);
                }
                if(lookingDirection == 1 && this.dir1Rec().intersects(e.getRect())){
                    e.takeDamage(punchDamage);
                    e.setKnockBackDirection(lookingDirection);
                }
                if(lookingDirection == 2 && this.dir2Rec().intersects(e.getRect())){
                    e.takeDamage(punchDamage);
                    e.setKnockBackDirection(lookingDirection);
                }
                if(lookingDirection == 3 && this.dir3Rec().intersects(e.getRect())){
                    e.takeDamage(punchDamage);
                    e.setKnockBackDirection(lookingDirection);
                }
            }

            if(e.getRect().intersects(this.getRect()) && !e.isDead()){
                this.takeDamage(e.getHitDamage());
                super.setKnockBackDirection(e.getLookingDirection());
            }
        }
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
        GlobalVariables.currentHealth -= damage;
        if(health <= 0){
            health = 0;
            dead = true;
        }
    }

    public void tick() {
        if ((left && up) || (left && down) || (right && up) || (right && down)) {
            moveSpeed = 0.6;
            maxMoveSpeed = 1.1;
        } else {
            moveSpeed = 1;
            maxMoveSpeed = 1.5;
        }

        super.generalGetNextPos();

        if (attacking || attackingBow) {
            entityVector.setVector(0, 0);
        }

        super.checkTileMapCollision();
        super.setPosition(xtemp, ytemp);
        this.checkAnimation();
        this.checkAttackingStuff();
        super.tick();
    }

    public void render(Graphics2D g) {
        super.render(g);
    }

    public void setAttacking() {
        attacking = true;
    }
    public void stopPlayer(){
        left = right = up = down = false;
    }
    public Rectangle dir0Rec(){
        return new Rectangle(getTotalX(),getTotalY() - punchRange,cwidth,punchRange);
    }
    public Rectangle dir1Rec(){
        return new Rectangle(getTotalX() + cwidth,getTotalY(),punchRange,cheight);
    }
    public Rectangle dir2Rec(){
        return new Rectangle(getTotalX(),getTotalY() + height,cwidth,punchRange);
    }
    public Rectangle dir3Rec(){
        return new Rectangle(getTotalX() - punchRange,getTotalY(),punchRange,cheight);
    }
}