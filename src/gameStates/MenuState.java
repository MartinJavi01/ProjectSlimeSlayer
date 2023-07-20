package gameStates;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//the main menu where the player can decide playing or closing the game
import tileMap.TileMap;
import tools.Constants;
import tools.GameStateManager;
import tools.GlobalVariables;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState{

    private TileMap tm;
    private Font titleFont;
    private Color titleColor;
    private Font textFont;
    private Font selectedFont;
    private String[] options;
    private int currentOption;
    private Rectangle[] rects;
    private boolean finishEvent;
    private int count;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        tm = new TileMap(32);
        tm.loadMap("/maps/MenuMap.txt");
        tm.loadTiles("/tilesets/ForestTileset.png");
        tm.setPosition(0,0);

        titleFont = new Font("",Font.PLAIN,32);
        titleColor = new Color(111,34,12);
        currentOption = 0;
        textFont = new Font("",Font.PLAIN,24);
        options = new String[]{"Play","Exit"};
        selectedFont = new Font("",Font.BOLD,24);

        count = 0;
        finishEvent = false;
        rects = new Rectangle[4];
        rects[0] = new Rectangle(0,-Constants.HEIGHT / 2,Constants.WIDTH,Constants.HEIGHT/2);
        rects[1] = new Rectangle(-Constants.WIDTH / 2,0,Constants.WIDTH / 2,Constants.HEIGHT);
        rects[2] = new Rectangle(0,Constants.HEIGHT,Constants.WIDTH,Constants.HEIGHT/2);
        rects[3] = new Rectangle(Constants.WIDTH,0,Constants.WIDTH/2,Constants.HEIGHT);
    }

    @Override
    public void tick() {
        if(finishEvent){
            finishEvent();
        }
    }

    @Override
    public void render(Graphics2D g) {
        tm.render(g);
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Slime Slayer",105,80);
        for(int i = 0;i < options.length;i++){
            if(i == currentOption){
                g.setFont(selectedFont);
                g.setColor(Color.cyan);
                g.drawString(">>",35 + i * 215,220);
            }else{
                g.setFont(textFont);
                g.setColor(Color.blue);
            }
            g.drawString(options[i],65 + i * 215,220);
        }
        if(finishEvent){
            for(int i = 0;i < rects.length;i++){
                g.setColor(Color.black);
                g.fill(rects[i]);
            }
        }
    }

    public void selection(){
        if(currentOption == 0){
            finishEvent = true;
        }else if(currentOption == 1){
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_D || k == KeyEvent.VK_LEFT){
            currentOption++;
            if(currentOption == options.length){
                currentOption = 0;
            }
        }
        if(k == KeyEvent.VK_A || k == KeyEvent.VK_RIGHT){
            currentOption--;
            if(currentOption == -1){
                currentOption = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if(k == KeyEvent.VK_ENTER){
            selection();
        }
    }

    @Override
    public void keyReleased(int k) {

    }


    /*
    Events are used for "cinematic" scenes
    */
    //Events

    public void finishEvent(){
        count++;
        if(rects[0].y < 0){
            rects[0].y += 2;
        }
        if(rects[1].x < 0){
            rects[1].x += 2;
        }
        if(rects[2].y > Constants.HEIGHT / 2){
            rects[2].y -= 2;
        }
        if(rects[3].x > Constants.WIDTH / 2){
            rects[3].x -= 2;
        }
        if(count >= 110){
            gsm.setState(Constants.FORESTATE);
        }
    }
}
