package spaceFighter.beans;

import helpers.Geometry;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author vesyj
 */
public class Ship extends Polygon {

    public final static double PUISSANCE = 0.05;
    public final static int VITESSE_MAX = 7;
    private double moveX;
    private double moveY;
    private double x;
    private double y;
    private boolean thrusting;

    public Ship(Color c) {
        super();
        moveX = 0;
        moveY = 0;
        x = 0;
        y = 0;
        thrusting = false;
        getPoints().addAll(0.0, -53.3, 30.0, 26.6, 0.0, 8.0, -30.0, 26.6);
        setFill(c);
    }
    
    public Ship(Color c, int x, int y, int rotate) {
        this(c);
        setLayoutX(x);
        setLayoutY(y);
        setRotate(rotate);
    }

    public double getMoveX() {
        return moveX;
    }

    public double getMoveY() {
        return moveY;
    }

    public boolean isThrusting() {
        return thrusting;
    }

    public void setMoveX(double moveX) {
        this.moveX = moveX;
    }

    public void setMoveY(double moveY) {
        this.moveY = moveY;
    }

    public void setThrusting(boolean thrusting) {
        this.thrusting = thrusting;
    }

    public void move() {
        if (thrusting) {
            double[] vecteur = Geometry.polar2Cartesian(180 - getRotate(), PUISSANCE);
            moveX += vecteur[0];
            moveY += vecteur[1];
            if (moveX > VITESSE_MAX) {
                moveX = VITESSE_MAX;
            }
            if (moveY > VITESSE_MAX) {
                moveY = VITESSE_MAX;
            }
            if (moveX < -VITESSE_MAX) {
                moveX = -VITESSE_MAX;
            }
            if (moveY < -VITESSE_MAX) {
                moveY = -VITESSE_MAX;
            }
        }
        x += moveX;
        y += moveY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return Math.sqrt(moveX * moveX + moveY * moveY) * 10;
    }

    @Override
    public String toString() {
        return "ship";
    }

}
