package entities;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//class used to obtain the sprites of an animation and send them to the entity class for rendering them,it sets too the
//delay between the frames of an animation
import java.awt.image.BufferedImage;

public class Animation {

    private BufferedImage[] frames;
    private long delay;
    private int timesPlayed;
    private long startTime;
    private int currentFrame;

    public Animation(){
        timesPlayed = 0;
    }
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        timesPlayed = 0;
        startTime = System.nanoTime();
        currentFrame = 0;
    }
    public void setDelay(long delay){
        this.delay = delay;
    }
    public void tick(){
        if(delay == -1){
            return;
        }
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay){
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame =0;
            timesPlayed++;
            startTime = System.nanoTime();
        }
    }
    public BufferedImage getImage(){
        return frames[currentFrame];
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }
    public int getCurrentFrame(){
        return currentFrame;
    }
    public void setFrame(int i){
        currentFrame = i;
    }
}
