package tools;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
/*this class manages all the states of the game.States are the menu and the different scenes of the game.
this class has a pause boolean that when it is activated it makes the main methods stop working and makes a pause
effect.*/
import gameStates.*;

import java.awt.*;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;
    private boolean paused;
    private PauseState pausedState;

    public GameStateManager() {
        gameStates = new GameState[Constants.NUMSTATES];
        currentState = Constants.INTROSTATE;
        paused = false;
        pausedState = new PauseState(this);
        loadState(currentState);
    }

    public void loadState(int state) {
        if (state == Constants.MENUSTATE) {
            gameStates[currentState] = new MenuState(this);
        }
        if (state == Constants.INTROSTATE) {
            gameStates[currentState] = new IntroState(this);
        }
        if (state == Constants.FORESTATE) {
            gameStates[currentState] = new Forest(this);
        }
        if (state == Constants.DUNGEONSTATE) {
            gameStates[currentState] = new Dungeon(this);
        }
    }

    public void unloadState(int state) {
        gameStates[state] = null;
    }

    //this method makes null the state that is currently on and starts the state written in the argument
    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    /*the tick, render, keyPressed, and keyReleased methods are used to manage the control of the game and they are
    called by the GamePanel class.*/

    public void tick() {
        if (paused) {
            pausedState.tick();
        } else {
            if (gameStates[currentState] != null) {
                gameStates[currentState].tick();
            }
        }
    }

    public void render(Graphics2D g) {
        if (paused) {
            pausedState.render(g);
        } else {
            if (gameStates[currentState] != null) {
                gameStates[currentState].render(g);
            }
        }
    }

    public void keyPressed(int k) {
        if (paused) {
            pausedState.keyPressed(k);
        } else {
            if (gameStates[currentState] != null) {
                gameStates[currentState].keyPressed(k);
            }
        }
    }

    public void keyReleased(int k) {
        if (paused) {
            pausedState.keyReleased(k);
        } else {
            if (gameStates[currentState] != null) {
                gameStates[currentState].keyReleased(k);
            }
        }
    }

    public void setPaused(boolean b) {
        paused = b;
    }
}