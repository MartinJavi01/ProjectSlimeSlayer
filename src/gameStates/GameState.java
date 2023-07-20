package gameStates;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//abstract class done for managing all the game states
import tools.GameStateManager;

import java.awt.*;

public abstract class GameState {

    protected GameStateManager gsm;

    public GameState(GameStateManager gsm){
        this.gsm = gsm;
    }

    //main methods of the game states
    public abstract void tick();
    public abstract void render(Graphics2D g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);
}
