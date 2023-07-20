package tileMap;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//this class is used to implement the maps and the tileSheets for the game
import tools.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {

    private double x;
    private double y;

    private double xmin;
    private double ymax;
    private double xmax;
    private double ymin;

    private int tileSize;
    private int width;
    private int height;
    private int[][] map;
    private int numCols;
    private int numRows;

    private BufferedImage tileSet;
    private int numTilesUpToDown;
    private int numTilesAcross;
    private Tile[][] tiles;

    private int numColsToDraw;
    private int numRowsToDraw;
    private int colOffset;
    private int rowOffset;

    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = Constants.HEIGHT / tileSize + 2;
        numColsToDraw = Constants.WIDTH / tileSize + 2;
    }

    /*this method implements the map by telling the compiler what tile to render by searching its
    number int the text file*/
    public void loadMap(String s) {
        try {
            InputStream in = this.getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());

            width = numCols * tileSize;
            height = numRows * tileSize;

            xmin = Constants.WIDTH - width;
            xmax = 0;
            ymin = Constants.HEIGHT - height;
            ymax = 0;

            map = new int[numRows][numCols];

            String delims = "\\s+";

            for (int row = 0; row < numRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for (int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*This method implements the tiles and checks if they are normal or blocked*/
    public void loadTiles(String s){
        try{
            tileSet = ImageIO.read(this.getClass().getResourceAsStream(s));
            numTilesAcross = tileSet.getWidth() / tileSize;
            numTilesUpToDown = tileSet.getHeight() / tileSize;

            tiles = new Tile[numTilesUpToDown][numTilesAcross];
            BufferedImage subImage;
            for(int col = 0;col < numTilesAcross;col++){
                for(int row = 0;row < numTilesUpToDown;row++){
                    subImage = tileSet.getSubimage(col * tileSize,row * tileSize,tileSize,tileSize);
                    if(row == 0 || row == 1){
                        tiles[row][col] = new Tile(subImage,Constants.NORMAL);
                    }else if(row == 2 || row == 3){
                        tiles[row][col] = new Tile(subImage,Constants.BLOCKED);
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public int getType(int row,int col){
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    public void setPosition(double x,double y){

        this.x += (x - this.x);
        this.y += (y - this.y);

        fixBounds();

        colOffset = (int)-this.x / tileSize;
        rowOffset = (int)-this.y / tileSize;
    }

    //this method is used to delimit the tileMap movement around the map
    public void fixBounds(){
        if(x < xmin){
            x = xmin;
        }
        if(x > xmax){
            x = xmax;
        }
        if(y < ymin){
            y = ymin;
        }
        if(y > ymax){
            y = ymax;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public void render(Graphics2D g) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
            if (row >= numRows) {
                break;
            }
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
                if (col >= numCols) {
                    break;
                }

                if (map[row][col] == 0) {
                    continue;
                }
                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
            }
        }
    }
}
