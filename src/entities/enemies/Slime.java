package entities.enemies;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//class of the enemy of the game, the slime, it manages the animation and has a random moving system
import entities.player.Player;
import tileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;

public class Slime extends Enemy{

    private ArrayList<BufferedImage[]> sprites;
    private int[] numSpritesPerRow;
    private long doNothingTimer;
    private Random rand;

    private int movingUp;
    private int movingDown;
    private int movingLeft;
    private int movingRight;
    private int idle;
    private int dying;
    private int destroyed;

    public Slime(TileMap tm,Player player) {
        super(tm,player);

        rand = new Random();

        width = 16;
        height = 16;
        cwidth = 14;
        cheight = 14;

        movingUp = 0;
        movingDown = 1;
        movingLeft = 2;
        movingRight = 3;
        dying = 4;
        destroyed = 5;
        idle = 6;

        flinchTime = 40;
        hitDamage = 1;

        health = maxHealth = 4;
        moveSpeed = maxMoveSpeed = stopSpeed = 0.5;
        detectionRange = 7;
        knockBackTime = 25;

        try{
            BufferedImage tileSet = ImageIO.read(this.getClass().getResourceAsStream("/sprites/enemies/slime/Slime.png"));
            sprites = new ArrayList<BufferedImage[]>();
            numSpritesPerRow = new int[]{4,4,4,4};
            for(int i = 0;i < numSpritesPerRow.length;i++){
                BufferedImage[] bi = new BufferedImage[numSpritesPerRow[i]];
                for(int j = 0;j < numSpritesPerRow[i];j++){
                    bi[j] = tileSet.getSubimage(j * width,i * height,width,height);
                }
                sprites.add(bi);
            }

            BufferedImage deathTileset = ImageIO.read(this.getClass()
                    .getResourceAsStream("/sprites/enemies/slime/SlimeDeath.png"));
            for(int i = 0;i < 2;i++){
                BufferedImage[] bi = new BufferedImage[5];
                for(int j = 0;j < bi.length;j++){
                    bi[j] = deathTileset.getSubimage(j * width,i * height,width,height);
                }
                sprites.add(bi);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        anim.setFrames(sprites.get(movingDown));
        anim.setDelay(100);
        currentAction = idle;
    }
    public void followPlayer(){
        if(player.getTotalY() + player.getHeight() / 4 < getTotalY()){
            up = true;
            down = false;
        }else if(player.getTotalY() + player.getHeight() / 4 > getTotalY()){
            down = true;
            up = false;
        }

        if(player.getTotalY() + player.getHeight() / 4 - getTotalY() < 1 &&
                player.getTotalY() + player.getHeight() / 4 - getTotalY() > -1){
            entityVector.setVector(entityVector.getX(),0);
            up = down = false;
        }
        if(player.getTotalX() < getTotalX()){
            left = true;
            right = false;
        }else if(player.getTotalX() > getTotalX()){
            right = true;
            left = false;
        }

        if(player.getTotalX() - getTotalX() < 1 && player.getTotalX() - getTotalX() > -1){
            entityVector.setVector(0,entityVector.getY());
            right = left = false;
        }
    }

    private void checkAnimation(){
        if(dead){
            left = right = up = down = false;
            if(currentAction != destroyed){
                if(currentAction != dying){
                    currentAction = dying;
                    anim.setFrames(sprites.get(dying));
                    anim.setDelay(150);
                }
            }
            if(anim.getCurrentFrame() == 4 && currentAction != destroyed){
                currentAction = destroyed;
                anim.setFrames(sprites.get(destroyed));
                anim.setDelay(500);
            }
            if(currentAction == destroyed && anim.getCurrentFrame() == 4){
                shouldBeRemoved = true;
            }
        }else{
            if(up){
                if(currentAction != movingUp){
                    currentAction = movingUp;
                    anim.setFrames(sprites.get(movingUp));
                    anim.setDelay(100);
                    lookingDirection = 0;
                }
            }else if(right){
                if(currentAction != movingRight){
                    currentAction = movingRight;
                    anim.setFrames(sprites.get(movingRight));
                    anim.setDelay(100);
                    lookingDirection = 1;
                }
            }else if(down){
                if(currentAction != movingDown){
                    currentAction = movingDown;
                    anim.setFrames(sprites.get(movingDown));
                    anim.setDelay(100);
                    lookingDirection = 2;
                }
            }else if(left){
                if(currentAction != movingLeft){
                    currentAction = movingLeft;
                    anim.setFrames(sprites.get(movingLeft));
                    anim.setDelay(100);
                    lookingDirection = 3;
                }
            }
        }
    }

    private void checkNotAttacking(){

        long elapsed = (System.nanoTime() - doNothingTimer) / 1000000;
        int maxTime;

        if(left || right || up || down){
            moveSpeed = 0.2;
            maxTime = 1000;
        }else{
            moveSpeed = 0.5;
            maxTime = 2000;
        }

        if(elapsed > maxTime){
            int move = rand.nextInt(5);
            if(move == 0){
                up = true;
                left = right = down = false;
                doNothingTimer = System.nanoTime();
            }else if(move == 1){
                right = true;
                left = down = up = false;
                doNothingTimer = System.nanoTime();
            }else if(move == 2){
                down = true;
                up = right = left = false;
                doNothingTimer = System.nanoTime();
            }else if(move == 3){
                left = true;
                up = down = right = false;
                doNothingTimer = System.nanoTime();
            }else if(move == 4){
                right = left = up = down = false;
                doNothingTimer = System.nanoTime();
            }
        }else{
            return;
        }
    }

    public void checkEnemyOwnCollisions(ArrayList<Enemy> enemies){
        for(int i = 0;i < enemies.size();i++){
            Enemy e = enemies.get(i);
            if(this.up && this.getUpRectangle().intersects(e.getDownRectangle())){
                up = false;
                entityVector.setVector(entityVector.getX(),0);
            }
            if(this.down && this.getDownRectangle().intersects(e.getUpRectangle())){
                down = false;
                entityVector.setVector(entityVector.getX(),0);
            }
            if(this.left && this.getLeftRectangle().intersects(e.getRightRecangle())){
                left = false;
                entityVector.setVector(0,entityVector.getY());
            }
            if(this.right && this.getRightRecangle().intersects(e.getLeftRectangle())){
                right = false;
                entityVector.setVector(0,entityVector.getY());
            }
        }
    }

    public void tick(){
        if(!shouldAttack) {
            this.checkNotAttacking();
        }
        this.checkAnimation();
        super.generalGetNextPos();
        super.checkTileMapCollision();
        super.setPosition(xtemp,ytemp);
        super.tick();
    }
    public void render(Graphics2D g){
        super.render(g);
    }
}
