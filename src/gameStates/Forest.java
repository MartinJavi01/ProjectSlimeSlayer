package gameStates;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//where the player starts the game and gets the first instructions to know what to do
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

public class Forest extends GameState{

    private TileMap tm;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<MessageEngine> messages;
    private ArrayList<Rectangle> eventRectangles;
    private int eventCount;
    private boolean mesVisible;
    private boolean caveEnter;
    private boolean normalTransitionEventOn;

    public Forest(GameStateManager gsm) {
        super(gsm);
        tm = new TileMap(32);
        tm.loadTiles("/tilesets/ForestTileset.png");
        tm.loadMap("/maps/ForestMap.txt");
        tm.setPosition(0,0);

        player = new Player(tm);

        enemies = new ArrayList<Enemy>();
        populateEnemies();

        eventCount = 0;
        mesVisible = false;
        caveEnter = false;

        messages = new ArrayList<MessageEngine>();
        eventRectangles = new ArrayList<Rectangle>();

        iniciatePlayer();
    }

    private void iniciatePlayer(){
        if(GlobalVariables.comesFromCave){
            player.setTilePosition(35,6);
            tm.setPosition(Constants.WIDTH / 2 - player.getX(),Constants.HEIGHT / 2 - player.getY());
        }else{
            player.setTilePosition(3,3);
        }
        normalTransitionEventOn = true;
        GlobalVariables.comesFromCave = false;
    }

    private void populateEnemies(){

        Point[] slimePoints = new Point[]{new Point(11,3),new Point(3,12),new Point(16,5),
        new Point(21,11),new Point(25,7),new Point(37,3)};

        for(int i = 0;i < slimePoints.length;i++){
            Slime s = new Slime(tm,player);
            s.setTilePosition(slimePoints[i].x,slimePoints[i].y);
            enemies.add(s);
        }
    }

    @Override
    public void tick() {
        if(normalTransitionEventOn){
            normalTransitionEvent();
            return;
        }
        if(player.isDead()){
            GlobalVariables.currentHealth = GlobalVariables.MAX_HEALTH;
            gsm.setState(Constants.MENUSTATE);
        }
        if(player.getX() > 1100 && player.getX() < 1110 &&
                player.getY() > 175 && player.getY() < 183) {
            caveEnterEvent();
            return;
        }
        if(!GlobalVariables.introduccionEvent){
            introducctionEvent();
            return;
        }
        player.tick();
        player.checkEnemyCollisions(enemies);
        for(int i = 0;i < enemies.size();i++){
            enemies.get(i).checkEnemyOwnCollisions(enemies);
            enemies.get(i).tick();
            if(enemies.get(i).shouldBeRemoved()){
                enemies.remove(enemies.get(i));
                i--;
            }
        }
        tm.setPosition(Constants.WIDTH / 2 - player.getX(),Constants.HEIGHT / 2 - player.getY());
    }

    @Override
    public void render(Graphics2D g) {
        tm.render(g);
        for(int i = 0;i < enemies.size();i++){
            enemies.get(i).render(g);
        }
        player.render(g);
        for(int i = 0;i < eventRectangles.size();i++){
            g.setColor(Color.black);
            g.fill(eventRectangles.get(i));
        }
        for(int i = 0;i < messages.size();i++){
            messages.get(i).render(g);
        }
        if(caveEnter){
            g.setColor(Color.black);
            int angle = eventCount * 4;
            if(angle > 360) angle = 360;

            g.fillArc(-Constants.WIDTH / 2,-Constants.HEIGHT / 2,Constants.WIDTH * 2,Constants.HEIGHT * 2,0,angle);
        }
    }

    @Override
    public void keyPressed(int k) {
            if(k == KeyEvent.VK_ENTER && messages.get(0).isVisible()) {
                messages.get(0).increasePage();
            }
            if(!GlobalVariables.blockInput) {
                if (k == KeyEvent.VK_D) {
                    player.setRight(true);
                }
                if (k == KeyEvent.VK_A) {
                    player.setLeft(true);
                }
                if (k == KeyEvent.VK_W) {
                    player.setUp(true);
                }
                if (k == KeyEvent.VK_S) {
                    player.setDown(true);
                }
                if (k == KeyEvent.VK_J) {
                    player.setAttacking();
                }
                if(k == KeyEvent.VK_ESCAPE){
                    for(int i = 0;i < enemies.size();i++){
                        enemies.get(i).stopEnemies();
                    }
                    player.stopPlayer();
                    gsm.setPaused(true);
                }
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

    //Events
    private void normalTransitionEvent(){
        eventCount++;
        if(eventCount == 1){
            Rectangle rec1 = new Rectangle(0,0,Constants.WIDTH / 2,Constants.HEIGHT);
            Rectangle rec2 = new Rectangle(Constants.WIDTH / 2,0,Constants.WIDTH / 2,Constants.HEIGHT);
            eventRectangles.add(rec1);
            eventRectangles.add(rec2);
        }
        if(eventCount > 1 && eventCount < 90){
            eventRectangles.get(0).x -= 3;
            eventRectangles.get(1).x += 3;
        }
        if(eventCount == 90){
            eventCount = 0;
            normalTransitionEventOn = false;
            for(int i = 0;i < 2;i++){
                eventRectangles.remove(0);
            }
        }
    }

    private void introducctionEvent(){
        if(eventCount == 0){
            for(int i = 0;i < eventRectangles.size();i++){
                eventRectangles.remove(i);
            }
            GlobalVariables.blockInput = true;
            MessageEngine message1 = new MessageEngine("Hi! Thanks for downloading my game, hope you/like it!//" +
                    "So, controls are basic stuff. Just use WASD to move/around and use J to nail the enemies with" +
                    " your/dagger. Believe me, it's going to be easy./Your goal is going to a dungeon" +
                    " that is at the east./Not really hard. Good luck with that.",
                    12,5,240,288,44);
            Rectangle rec1 = new Rectangle(0,-50,Constants.WIDTH,50);
            Rectangle rec2 = new Rectangle(0,Constants.HEIGHT,Constants.WIDTH,50);
            messages.add(message1);
            eventRectangles.add(rec1);
            eventRectangles.add(rec2);
            eventCount++;
        }
        if(eventCount >= 1 && eventCount < 60){
            eventRectangles.get(0).y += 0.2;
            eventRectangles.get(1).y -= 0.2;
            if(eventRectangles.get(0).y > -25){
                eventRectangles.get(0).y = -25;
            }
            if(eventRectangles.get(1).y < Constants.HEIGHT - 25){
                eventRectangles.get(1).y = Constants.HEIGHT - 25;
            }
            eventCount++;
        }
        if(eventCount == 60){
            if(!mesVisible){
                messages.get(0).setVisible(true);
                mesVisible = true;
            }
            if(!messages.get(0).shouldBeVisible()){
                eventCount++;
                mesVisible = false;
                messages.get(0).setVisible(false);
                messages.remove(messages.get(0));
            }
        }
        if(eventCount > 60 && eventCount < 110){
            eventRectangles.get(0).y -= 1;
            eventRectangles.get(1).y += 1;
            if(eventRectangles.get(0).y < -50){
                eventRectangles.get(0).y = -50;
            }
            if(eventRectangles.get(1).y > Constants.HEIGHT){
                eventRectangles.get(1).y = Constants.HEIGHT;
            }
            eventCount++;
        }
        if(eventCount == 110){
            for(int i = 0;i < eventRectangles.size();i++){
                eventRectangles.remove(eventRectangles.get(i));
            }
            eventCount = 0;
            GlobalVariables.blockInput = false;
            GlobalVariables.introduccionEvent = true;
        }
    }
    private void caveEnterEvent(){
        eventCount++;
        if(eventCount < 120){
            GlobalVariables.blockInput = true;
            caveEnter = true;
            player.setUp(false);
            player.setDown(false);
            player.setLeft(false);
            player.setRight(false);
        }else if(eventCount > 120){
            GlobalVariables.blockInput = false;
            eventCount = 0;
            caveEnter = false;
            gsm.setState(Constants.DUNGEONSTATE);
        }
    }
}