/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;


import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Shape;

/**
 *
 * @author Jordan
 */
public class Geometry {

    public static double getCenterX(Region n) {
        return n.getLayoutX() + n.getWidth() / 2;
    }

    public static double getCenterY(Region n) {
        return n.getLayoutY() + n.getHeight() / 2;
    }

    public static Point2D getCenter(Region n) {
        return new Point2D(getCenterX(n), getCenterY(n));
    }

    public static double getCenterX(Shape n) {
        return n.getLayoutBounds().getWidth() / 2;
    }

    public static double getCenterY(Shape n) {
        return n.getLayoutBounds().getHeight() / 2;
    }

    public static Point2D getCenter(Shape n) {
        return new Point2D(getCenterX(n), getCenterY(n));
    }

    public static double[] polar2Cartesian(double angle, double force) {
        double[] res = {force * Math.sin(Math.toRadians(angle)), force * Math.cos(Math.toRadians(angle))};
        return res;
    }

    public static double cartesian2Angle(double x, double y) {
        if (x > 0 && y >= 0) {
            return Math.atan(y / x);
        } else if (x > 0 && y < 0) {
            return Math.atan(y / x + 2 * Math.PI);
        } else if (x < 0) {
            return Math.atan(y / x + Math.PI);
        } else if (x == 0 && y > 0) {
            return Math.PI / 2;
        } else {
            return 3 * Math.PI / 2;
        }
    }

    public static double random(double min, double max) {
        return new Random().nextDouble() * (max - min) + min;
    }

    public static int random(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static boolean randomBool() {
        return Math.random() > 0.5;
    }

    public static double[] randomOut(Pane pane) {
        boolean x = randomBool();
        boolean y = randomBool();
        double[] res = {random(0, pane.getWidth()), random(0, pane.getHeight())};
        if (x) {
            res[0] += pane.getWidth() * 3;
        } else {
            res[0] -= pane.getWidth() * 3;
        }
        if (y) {
            res[1] += pane.getHeight() * 3;
        } else {
            res[1] -= pane.getHeight() * 3;
        }
        return res;
    }

    public static double[] AsteroidGen(int r) {
        int n = random(r/3, r / 2) * 2;
        int deform = 10;
        double[] points = new double[n];
        for (int i = 1; i <= n; i += 2) {
            points[i - 1] = r * Math.cos(2 * Math.PI * i / n) + random(-deform, deform);
            points[i] = r * Math.sin(2 * Math.PI * i / n) + random(-deform, deform);
        }
        return points;
    }
}
