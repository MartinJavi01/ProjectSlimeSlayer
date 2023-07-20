package tileMap;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
/*this class is used to set the tiles of the scene between normal tiles or blocked tiles*/
import java.awt.image.BufferedImage;

public class Tile {

    private BufferedImage image;
    private int type;

    //constructor for setting up the tile type and the image that it should have
    public Tile(BufferedImage image,int type){
        this.image = image;
        this.type = type;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getType() {
        return type;
    }
}
