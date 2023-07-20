package entities;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//The main abstract class that manages all the entities of the game, it has the main methods for rendering the image
//of the sprite and detecting collisions
import tileMap.TileMap;
import tools.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {

    protected double x;
    protected double y;

    protected double addX;
    protected double addY;

    protected double xmap;
    protected double ymap;

    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;

    protected int tileSize;
    protected int width;
    protected int height;
    protected int cwidth;
    protected int cheight;
    protected int currentCol;
    protected int currentRow;

    protected Animation anim;
    protected TileMap tm;
    protected EntityVector entityVector;
    protected BufferedImage tileSet;
    protected int currentAction;

    protected boolean topRight;
    protected boolean topLeft;
    protected boolean bottomRight;
    protected boolean bottomLeft;

    protected boolean down;
    protected boolean up;
    protected boolean left;
    protected boolean right;

    protected double moveSpeed;
    protected double maxMoveSpeed;
    protected double stopSpeed;
    protected double health;
    protected double maxHealth;
    protected boolean dead;

    protected boolean showColliders;
    protected boolean flinching;
    protected long flinchTimer;
    protected long flinchTime;
    protected int lookingDirection;
    protected boolean knockbacking;
    protected long knockBackTime;
    protected long knockBackTimer;
    protected int knockBackDirection;

    public Entity(TileMap tm){
        this.tm = tm;
        entityVector = new EntityVector();
        anim = new Animation();
        tileSize = tm.getTileSize();
        showColliders = false;
        knockBackTime = 25;
    }
    public Rectangle getRect(){
        return new Rectangle(getTotalX() +(int)addX,getTotalY() + (int) addY,cwidth,cheight);
    }
    public Rectangle getUpRectangle(){
        return new Rectangle((int)(this.x - cwidth / 2 + addX),(int)(this.y - cheight / 2 + addY - 1),cwidth,1);
    }
    public Rectangle getDownRectangle(){
        return new Rectangle((int)(this.x - cwidth / 2 + addX),(int)(this.y - cheight / 2 + addY + cheight),cwidth,1);
    }
    public Rectangle getLeftRectangle(){
        return new Rectangle((int)(this.x - cwidth / 2 + addX - 1),(int)(this.y - cheight / 2 + addY),1,cheight);
    }
    public Rectangle getRightRecangle(){
        return new Rectangle((int)(this.x - cwidth / 2 + addX + cwidth),(int)(this.y - cheight / 2 + addY),1,cheight);
    }

    public boolean intersects(Entity e){
        Rectangle rec1 = this.getRect();
        Rectangle rec2 = e.getRect();
        return rec1.intersects(rec2);
    }
    public void calculateCorners(double x,double y){
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;

        int tl = tm.getType(topTile, leftTile);
        int tr = tm.getType(topTile, rightTile);
        int bl = tm.getType(bottomTile, leftTile);
        int br = tm.getType(bottomTile, rightTile);

        topLeft = tl == Constants.BLOCKED;
        topRight = tr == Constants.BLOCKED;
        bottomLeft = bl == Constants.BLOCKED;
        bottomRight = br == Constants.BLOCKED;

        if((this.y < cheight / 2 + 1) && up){
            entityVector.setVector(entityVector.getX(),0);
        }else if((this.y > tm.getNumRows() * tm.getTileSize() - cheight / 2 - 4) && down){
            entityVector.setVector(entityVector.getX(),0);
        }else if((this.x < cwidth / 2 + 1) && left){
            entityVector.setVector(0,entityVector.getY());
        }else if((this.x > tm.getNumCols() * tm.getTileSize() - cwidth / 2 - 4) && right){
            entityVector.setVector(0,entityVector.getY());
        }
    }
    public void checkTileMapCollision(){
        currentCol = (int)x / tileSize;
        currentRow = (int)y / tileSize;

        xdest = x + entityVector.getX();
        ydest = y + entityVector.getY();

        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest);
        if(entityVector.getY() < 0) {
            if(topLeft || topRight) {
                entityVector.setVector(entityVector.getX(),0);
                ytemp = currentRow * tileSize + cheight / 2;
            }
            else {
                ytemp = ydest;
            }
        }
        if(entityVector.getY() > 0) {
            if(bottomLeft || bottomRight) {
                entityVector.setVector(entityVector.getX(),0);
                ytemp = (currentRow + 1) * tileSize - cheight / 2;
            }
            else {
                ytemp = ydest;
            }
        }
        calculateCorners(xdest, y);
        if(entityVector.getX() < 0) {
            if(topLeft || bottomLeft) {
                entityVector.setVector(0,entityVector.getY());
                xtemp = currentCol * tileSize + cwidth / 2;
            }
            else {
                xtemp = xdest;
            }
        }
        if(entityVector.getX() > 0) {
            if(topRight || bottomRight) {
                entityVector.setVector(0,entityVector.getY());
                xtemp = (currentCol +1) * tileSize - cwidth / 2;
            }
            else {
                xtemp = xdest;
            }
        }
    }
    public void generalGetNextPos(){
        if(knockbacking){
            int knockbackBoost;
            if(maxMoveSpeed < 1){
                knockbackBoost = 5;
            }else{
                knockbackBoost = 3;
            }
            if(knockBackDirection == 0){
                entityVector.setVector(0,-maxMoveSpeed * knockbackBoost);
            }
            if(knockBackDirection == 1){
                entityVector.setVector(maxMoveSpeed * knockbackBoost,0);
            }
            if(knockBackDirection == 2){
                entityVector.setVector(0,maxMoveSpeed * knockbackBoost);
            }
            if(knockBackDirection == 3){
                entityVector.setVector(-maxMoveSpeed * knockbackBoost,0);
            }
        }else{
            if(right){
                entityVector.increaseVector(moveSpeed,0);
                if(entityVector.getX() > maxMoveSpeed){
                    entityVector.setVector(maxMoveSpeed,entityVector.getY());
                }
            }
            if(!right && entityVector.getX() > 0){
                entityVector.decreaseVector(stopSpeed,0);
                if(entityVector.getX() < 0){
                    entityVector.setVector(0,entityVector.getY());
                }
            }

            if(left){
                entityVector.decreaseVector(moveSpeed,0);
                if(entityVector.getX() < -maxMoveSpeed){
                    entityVector.setVector(-maxMoveSpeed,entityVector.getY());
                }
            }
            if(!left && entityVector.getX() < 0){
                entityVector.increaseVector(stopSpeed,0);
                if(entityVector.getX() > 0){
                    entityVector.setVector(0,entityVector.getY());
                }
            }

            if(down){
                entityVector.increaseVector(0,moveSpeed);
                if(entityVector.getY() > maxMoveSpeed){
                    entityVector.setVector(entityVector.getX(),maxMoveSpeed);
                }
            }
            if(!down && entityVector.getY() > 0){
                entityVector.decreaseVector(0,stopSpeed);
                if(entityVector.getY() < 0){
                    entityVector.setVector(entityVector.getX(),0);
                }
            }
            if(up){
                entityVector.decreaseVector(0,moveSpeed);
                if(entityVector.getY() < - maxMoveSpeed){
                    entityVector.setVector(entityVector.getX(),-maxMoveSpeed);
                }
            }
            if(!up && entityVector.getY() < 0){
                entityVector.increaseVector(0,stopSpeed);
                if(entityVector.getY() > 0){
                    entityVector.setVector(entityVector.getX(),0);
                }
            }
        }
    }
    public void setMapPosition(){
        xmap = tm.getX();
        ymap = tm.getY();
    }
    public void tick(){
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 10000000;
            if(elapsed > flinchTime){
                flinching = false;
            }
        }
        if(knockbacking){
            long elapsed = (System.nanoTime() - knockBackTimer) / 1000000;
            if(elapsed > knockBackTime){
                knockbacking = false;
            }
        }
        anim.tick();
    }
    public void render(Graphics2D g){

        if(flinching && !dead){
            long elapsed = (System.nanoTime() - flinchTimer) / 10000000;
            if(elapsed % 2 == 0){
                return;
            }
        }
        setMapPosition();
        g.drawImage(anim.getImage(),(int)(x + xmap - width / 2),(int)(y + ymap - height / 2),null);
        if(showColliders){
            g.setColor(Color.black);
            g.draw(this.getRect());
        }
    }

    public void setPosition(double x,double y){
        this.x = x;
        this.y = y;
    }

    public void setTilePosition(int tileX,int tileY){
        this.x = tileX * tileSize;
        this.y = tileY * tileSize;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setShowColliders(boolean showColliders) {
        this.showColliders = showColliders;
    }

    public boolean isShowingColliders() {
        return showColliders;
    }

    public double getX() {
        return x;
    }

    public int getTotalX(){
        return (int)(x + xmap - width / 2);
    }

    public int getTotalY(){
        return (int)(y + ymap - height / 2);
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDead() {
        return dead; }

    public boolean isGoingDown() {
        return down;
    }

    public boolean isGoingRight() {
        return right;
    }

    public boolean isGoingLeft() {
        return left;
    }

    public boolean isGoingUp() {
        return up;
    }

    public boolean isFlinching() {
        return flinching;
    }

    public int getLookingDirection(){
        return lookingDirection;
    }

    public void setKnockBackDirection(int i){
        knockBackDirection = i;
    }
}
