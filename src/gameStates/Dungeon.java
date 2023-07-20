package gameStates;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//final game state of the game, this was added just for testing the scene changing was going well
import entities.enemies.Enemy;
import entities.enemies.Slime;
import entities.player.Player;
import tileMap.TileMap;
import tools.Constants;
import tools.GameStateManager;
import tools.GlobalVariables;
import tools.MessageEngine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Dungeon extends GameState{

    private TileMap tm;
    private Player player;
    private boolean transitionEventOn;
    private ArrayList<Rectangle> eventRectangles;
    private ArrayList<MessageEngine> messages;
    private ArrayList<Enemy> enemies;
    private int eventCount;

    public Dungeon(GameStateManager gsm) {
        super(gsm);
        tm = new TileMap(32);
        tm.loadTiles("/tilesets/Dungeon.png");
        tm.loadMap("/maps/DungeonMap.txt");

        player = new Player(tm);
        player.setTilePosition(6,14);
        tm.setPosition(Constants.WIDTH / 2 - player.getX(),Constants.HEIGHT / 2 - player.getY());
        GlobalVariables.comesFromCave = true;
        transitionEventOn = true;
        eventRectangles = new ArrayList<Rectangle>();
        messages = new ArrayList<MessageEngine>();
        enemies = new ArrayList<Enemy>();
        eventCount = 0;

        populateEnemies();
    }

    private void populateEnemies(){
        Point[] slimePoints = new Point[]{new Point(4,7),new Point(12,9),new Point(23,10),
        new Point(34,12)};

        for(int i = 0;i < slimePoints.length;i++){
            Slime s = new Slime(tm,player);
            s.setTilePosition(slimePoints[i].x,slimePoints[i].y);
            enemies.add(s);
        }

    }

    @Override
    public void tick() {
        if(transitionEventOn){
            transitionEvent();
            return;
        }
        if(player.getX() > 189 && player.getX() < 199 &&
                player.getY() > 540 && player.getY() < 550){
            exitEvent();
            return;
        }
        if(player.isDead()){
            GlobalVariables.currentHealth = GlobalVariables.MAX_HEALTH;
            gsm.setState(Constants.MENUSTATE);
        }
        player.tick();
        player.checkEnemyCollisions(enemies);
        for(int i = 0;i < enemies.size();i++){
            enemies.get(i).checkEnemyOwnCollisions(enemies);
            enemies.get(i).tick();
            if(enemies.get(i).shouldBeRemoved()){
                enemies.remove(enemies.get(i));
            }
        }
        tm.setPosition(Constants.WIDTH / 2 - player.getX(),Constants.HEIGHT / 2 - player.getY());
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.black);
        g.fillRect(0,0,Constants.WIDTH,Constants.HEIGHT);
        tm.render(g);
        for(int i = 0;i < enemies.size();i++){
            enemies.get(i).render(g);
        }
        player.render(g);
        if(transitionEventOn){
            int angle = 360 - eventCount * 4;
            if(angle < 0) angle = 0;

            g.setColor(Color.black);
            g.fillArc(-Constants.WIDTH / 2,-Constants.HEIGHT / 2,
                    Constants.WIDTH * 2,Constants.HEIGHT * 2,360,angle);
        }
        g.setColor(Color.black);
        for(int i = 0;i < eventRectangles.size();i++){
            g.fill(eventRectangles.get(i));
        }
    }

    @Override
    public void keyPressed(int k) {
            if (k == KeyEvent.VK_D) {
                player.setRight(true);
            }else if (k == KeyEvent.VK_A) {
                player.setLeft(true);
            }else if (k == KeyEvent.VK_W) {
                player.setUp(true);
            }else if (k == KeyEvent.VK_S) {
                player.setDown(true);
            }else if (k == KeyEvent.VK_J) {
                player.setAttacking();
            }else if(k == KeyEvent.VK_ESCAPE){
            player.stopPlayer();
            gsm.setPaused(true);
        }
    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_D){
            player.setRight(false);
        }
        if(k == KeyEvent.VK_A){
            player.setLeft(false);
        }
        if(k == KeyEvent.VK_W){
            player.setUp(false);
        }
        if(k == KeyEvent.VK_S){
            player.setDown(false);
        }
    }

    //////
    //Events
    /////

    private void transitionEvent(){
        eventCount++;
        if(eventCount == 90){
            transitionEventOn = false;
            eventCount = 0;
        }
    }
    private void exitEvent(){
        eventCount++;
        if(eventCount == 1){
            Rectangle rec1 = new Rectangle(-Constants.WIDTH / 2,0,Constants.WIDTH / 2,Constants.HEIGHT);
            Rectangle rec2 = new Rectangle(Constants.WIDTH,0,Constants.WIDTH / 2,Constants.HEIGHT);
            eventRectangles.add(rec1);
            eventRectangles.add(rec2);
        }
        if(eventCount > 1 && eventCount < 90){
            eventRectangles.get(0).x += 3;
            eventRectangles.get(1).x -= 3;
            if(eventRectangles.get(0).x > 0){
                eventRectangles.get(0).x = 0;
            }
            if(eventRectangles.get(1).x < Constants.WIDTH / 2){
                eventRectangles.get(1).x = Constants.WIDTH / 2;
            }
        }
        if(eventCount == 90){
            eventCount = 0;
            gsm.setState(Constants.FORESTATE);
        }
    }
}
