package core;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//this class manages the main frame of the game, also contains the timer of the game. Also implements the KeyListener
import tools.Constants;
import tools.GameStateManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private boolean running;
    private Thread thread;
    private BufferedImage image;
    private Graphics2D g2d;
    private int fpsWanted;
    private int fps;
    private int ticks;
    private GameStateManager gsm;
    private BufferedImage imageIcon;

    public GamePanel(){
        super.setFocusable(true);
        super.requestFocus();
        super.setPreferredSize(new Dimension(Constants.WIDTH * Constants.SCALE,
                Constants.HEIGHT * Constants.SCALE));
        try{
            imageIcon = ImageIO.read(this.getClass().getResourceAsStream("/other/ImageIcon.png"));
            Main.getWindow().setIconImage(imageIcon);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //method used to start the thread
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            super.addKeyListener(this);
            super.setFocusTraversalKeysEnabled(false);
            thread.start();
        }
    }

    public void iniciate(){
        image = new BufferedImage(Constants.WIDTH, Constants.HEIGHT,BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D)image.getGraphics();
        running = true;
        gsm = new GameStateManager();
        fpsWanted = 60;
        fps = 0;
        ticks = 0;
    }

    public void tick(){
        gsm.tick();
    }
    public void render(){
        gsm.render(g2d);
    }
    public void renderToScreen(){
        Graphics g = getGraphics();
        g.drawImage(image,0,0, Constants.WIDTH * Constants.SCALE,
                Constants.HEIGHT * Constants.SCALE,null);
        g.dispose();
    }

    @Override
    public void run() {

        iniciate();

        long startTime = System.nanoTime();
        double delta = 0;
        double nsPerTick = 1000000000 / fpsWanted;
        long timer = 0;

        //game timer
        while (running){
            long now = System.nanoTime();
            delta += (now - startTime) / nsPerTick;
            timer += now - startTime;
            startTime = now;

            //this runs 60 times per second
            if(delta >= 1){
                tick();
                ticks++;
                delta--;
            }
            //fps depends on the pc running the game
            render();
            renderToScreen();
            fps++;

            if(timer >= 1000000000){
                timer -= 1000000000;
                fps = 0;
                ticks = 0;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        gsm.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }
}
