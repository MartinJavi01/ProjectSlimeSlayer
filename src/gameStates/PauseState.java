package gameStates;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
/*this state works in a different way than the others,when the others are paused, the main methods of the game
 *focus on this class*/
import tools.Constants;
import tools.GameStateManager;
import tools.GlobalVariables;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseState extends GameState{

    private String[] options;
    private int currentOption;
    private Color bgColor;
    private Font optionsFont;
    private boolean bgDraw;

    public PauseState(GameStateManager gsm) {
        super(gsm);
        options = new String[]{"Continue","Exit"};
        currentOption = 0;
        bgColor = new Color(0,0,0,30);
        optionsFont = new Font("",Font.PLAIN,16);
        bgDraw = false;
    }

    @Override
    public void tick() {}

    @Override
    public void render(Graphics2D g) {
        if(!bgDraw) {
            g.setColor(bgColor);
            g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
            bgDraw = true;
        }
        g.setColor(Color.white);
        g.fillRect(10,10,90,70);
        g.setColor(Color.black);
        g.fillRect(13,13,84,64);

        g.setFont(optionsFont);
        for(int i = 0;i < options.length;i++){
            if(currentOption == i){
                g.setColor(Color.white);
                g.drawString(">",15,30 + currentOption * 40);
            }else{
                g.setColor(Color.gray);
            }
            g.drawString(options[i],26,30 + i * 40);
        }
    }

    private void select(){
        if(currentOption == 0){
            bgDraw = false;
            gsm.setPaused(false);
        }
        if(currentOption == 1){
            bgDraw = false;
            gsm.setPaused(false);
            GlobalVariables.introduccionEvent = false;
            gsm.setState(Constants.MENUSTATE);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN){
            currentOption++;
            if(currentOption == options.length){
                currentOption = 0;
            }
        }
        if(k == KeyEvent.VK_W || k == KeyEvent.VK_UP){
            currentOption--;
            if(currentOption == -1){
                currentOption = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_ESCAPE){
            bgDraw = false;
            gsm.setPaused(false);
        }
    }

    @Override
    public void keyReleased(int k) { }
}
