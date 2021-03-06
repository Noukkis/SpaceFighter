/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceFighter;

import client.Nouklient;
import client.Nouklient.PluginInstructionReceivedEvent;
import spaceFighter.beans.Bullet;
import spaceFighter.beans.Ship;
import helpers.Geometry;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Jordan
 */
public class ViewCtrl implements Initializable {

    @FXML
    private Pane pane;

    Image bgImg;

    private Ship ship;

    private double mouseX;
    private double mouseY;

    Nouklient client;

    private boolean playable;

    private ArrayList<Bullet> bullets;
    private HashMap<String, Ship> ships;
    private ArrayList lost;

    @FXML
    private Label lblVitesse;
    @FXML
    private Pane bgPane;

    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (playable) {
            switch (event.getCode().getName()) {
                case "Space":
                    ship.setThrusting(true);
            }
        } else if (!ship.isThrusting()) {
            playable = true;
            pane.getChildren().add(ship);
        }
    }

    @FXML
    private void clicked(MouseEvent event) {
        if (playable) {
            Bullet bullet = new Bullet(ship);
            bullets.add(bullet);
            pane.getChildren().add(bullet);
        }
    }

    @FXML
    private void onDrag(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        switch (event.getCode().getName()) {
            case "Space":
                ship.setThrusting(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bgImg = new Image("spaceFighter/resources/bg.png");
        playable = true;
        lost = new ArrayList();
        ships = new HashMap<>();
        bullets = new ArrayList<>();
        mouseX = 0;
        mouseY = 0;
        ship = new Ship(Color.STEELBLUE);
        pane.getChildren().add(ship);
    }

    private void actualize() {
        if (mouseX != Geometry.getCenterX(ship) || mouseY != Geometry.getCenterY(ship)) {
            ship.setRotate(360 - Math.toDegrees(Math.atan2(ship.getLayoutX() - mouseX, ship.getLayoutY() - mouseY)));
        }

        String speed = new DecimalFormat("0.00").format(ship.getSpeed());
        lblVitesse.setText(speed + "km/s");
        ship.move();
        bgPane.setLayoutX(bgPane.getLayoutX() - ship.getMoveX() / 1.1);
        bgPane.setLayoutY(bgPane.getLayoutY() - ship.getMoveY() / 1.1);
        if (bgPane.getLayoutX() > 0) {
            bgPane.setLayoutX(-2 * bgImg.getWidth() - ship.getMoveX() / 1.1);
        }
        if (bgPane.getLayoutY() > 0) {
            bgPane.setLayoutY(-2 * bgImg.getHeight() + bgPane.getLayoutY() - ship.getMoveY() / 1.1);
        }
        if (bgPane.getLayoutX() < -2 * bgImg.getWidth()) {
            bgPane.setLayoutX(-ship.getMoveX() / 1.1);
        }
        if (bgPane.getLayoutY() < -2 * bgImg.getHeight()) {
            bgPane.setLayoutY(-ship.getMoveY() / 1.1);
        }
        for (Bullet bullet : bullets) {
            if (!bullet.move(pane)) {
                lost.add(bullet);
            }
        }

        synchronized (client) {
            for (Ship sp : ships.values()) {
                sp.move();
                double x = (sp.getX() - ship.getX() + ship.getLayoutX());
                double y = (sp.getY() - ship.getY() + ship.getLayoutY());
                sp.setLayoutX(x);
                sp.setLayoutY(y);
            }
        }
        pane.getChildren().removeAll(lost);
        bullets.removeAll(lost);
        lost.clear();
        networkActu();
    }

    public void init(Nouklient client) {
        this.client = client;
        Scene s = bgPane.getScene();
        bgPane.setPrefHeight(s.getHeight() + 2 * bgImg.getHeight());
        bgPane.setPrefWidth(s.getWidth() + 2 * bgImg.getWidth());
        bgPane.setLayoutX(0);
        bgPane.setLayoutY(0);
        BackgroundImage bg = new BackgroundImage(bgImg, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        bgPane.setBackground(new Background(bg));

        ship.setLayoutX(Geometry.getCenterX(pane) - Geometry.getCenterX(ship));
        ship.setLayoutY(Geometry.getCenterY(pane) - Geometry.getCenterY(ship));

        client.addListener((event) -> {
            if (event instanceof Nouklient.PluginInstructionReceivedEvent) {
                synchronized (client) {
                    String[] arr = ((PluginInstructionReceivedEvent) event).getInstruction().split("::");
                    double[] infos = toDouble(Base64.getDecoder().decode(arr[1]));
                    double x = (infos[0] - ship.getX() + ship.getLayoutX());
                    double y = (infos[1] - ship.getY() + ship.getLayoutY());
                    double rotate = infos[2];
                    if (ships.containsKey(arr[0])) {
                        Ship sp = ships.get(arr[0]);
                        sp.setLayoutX(x);
                        sp.setLayoutY(y);
                        sp.setRotate(rotate);
                        sp.setMoveX(infos[3]);
                        sp.setMoveX(infos[4]);
                    } else {
                        Ship sp = new Ship(Color.RED, x, y, rotate);
                        ships.put(arr[0], sp);
                        Platform.runLater(() -> pane.getChildren().add(sp));
                    }
                }
            }
        });

        Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(10), (ActionEvent) -> actualize()));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void stop() {
        lost.addAll(pane.getChildren());
        playable = false;
        init(client);
    }

    private void networkActu() {
        double[] infos = {ship.getX(), ship.getY(), ship.getRotate(), ship.getMoveX(), ship.getMoveY()};
        client.sendInstruction(Base64.getEncoder().encodeToString(toByteArray(infos)));
    }

    public static byte[] toByteArray(double[] values) {
        ByteBuffer bb = ByteBuffer.allocate(values.length * 8);
        for (double d : values) {
            bb.putDouble(d);
        }
        return bb.array();
    }

    public static double[] toDouble(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        double[] doubles = new double[bytes.length / 8];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = bb.getDouble();
        }
        return doubles;
    }
}
