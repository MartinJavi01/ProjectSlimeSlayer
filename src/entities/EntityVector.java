package entities;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
//class used for the main movement of the entities
public class EntityVector {

    private double x;
    private double y;

    public void increaseVector(double dx,double dy){
        this.x += dx;
        this.y += dy;
    }
    public void decreaseVector(double dx,double dy){
        this.x -= dx;
        this.y -= dy;
    }
    public void setVector(double x,double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
