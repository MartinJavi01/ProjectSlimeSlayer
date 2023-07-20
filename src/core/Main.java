package core;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
/*The main class of the project, just starts the Game Panel and returns the main frame of the game
in the case of needing it*/
import tools.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//main method
public class Main {
    private static JFrame window;
    public static void main(String[] args){
        window = new JFrame(Constants.NAME);
        window.setResizable(false);
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - Constants.WIDTH
                ,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - Constants.HEIGHT);
        window.pack();
        window.setVisible(true);
    }
    //return the main frame
    public static JFrame getWindow() {
        return window;
    }
}
