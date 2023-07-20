package gameStates;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//this game state plays only once when the game is executed
import tools.Constants;
import tools.GameStateManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class IntroState extends GameState{

    private BufferedImage logoImage;
    private Font textFont;
    private Color textColor;
    private Color rectColor;
    private int count;
    private int alphaNumb;

    public IntroState(GameStateManager gsm) {
        super(gsm);
        count = 0;
        alphaNumb = 255;
        textFont = new Font("",Font.BOLD,24);
        textColor = new Color(23,111,22);
        rectColor = new Color(0,0,0,alphaNumb);
        try{
            logoImage = ImageIO.read(this.getClass().getResourceAsStream("/other/ImageIcon.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkCount(){
        if (count > 60 && count < 150){
            alphaNumb -= 3;
            if(alphaNumb < 0){
                alphaNumb = 0;
            }
            rectColor = new Color(0,0,0,alphaNumb);
        }
        if (count > 210){
            alphaNumb += 3;
            if(alphaNumb > 255){
                alphaNumb = 255;
            }
            rectColor = new Color(0,0,0,alphaNumb);
        }
        if(count >= 310){
            gsm.setState(Constants.MENUSTATE);
        }
    }

    @Override
    public void tick() {
        count++;
        checkCount();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(logoImage, Constants.WIDTH / 2 - 75,
                Constants.HEIGHT / 2 - 75,150,150,null);
        g.setFont(textFont);
        g.setColor(textColor);
        g.drawString("Project Slime Slayer",70,40);
        g.drawString("By Javier Martín",100,270);
        g.setColor(rectColor);
        g.fillRect(0,0,Constants.WIDTH,Constants.HEIGHT);
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            gsm.setState(Constants.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
