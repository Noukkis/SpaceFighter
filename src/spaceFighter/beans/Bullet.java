/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceFighter.beans;

import helpers.Geometry;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Jordan
 */
public class Bullet extends Ellipse {

    private double moveX;
    private double moveY;
    private final double VITESSE;

    public Bullet(Polygon ship) {
        super(ship.getLayoutX(), ship.getLayoutY(), 4, 4);
        this.VITESSE = 10;
        super.setFill(Color.WHITE);
        this.moveX = Geometry.polar2Cartesian(180 - ship.getRotate(), VITESSE)[0];
        this.moveY = Geometry.polar2Cartesian(180 - ship.getRotate(), VITESSE)[1];
    }

    public boolean move(Pane pane) {
        if (super.getCenterX() > pane.getWidth() || super.getCenterY() > pane.getHeight() || super.getCenterX() < 0 || super.getCenterY() < 0) {
            return false;
        }
        super.setCenterX(super.getCenterX() + moveX);
        super.setCenterY(super.getCenterY() + moveY);
        return true;
    }
}
