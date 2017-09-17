package spaceFighter;

import client.Nouklient;
import client.fxDialogs.ClientMakerDialog;
import client.fxDialogs.RoomChooser;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jordan
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ClientMakerDialog cmd = new ClientMakerDialog("spaceFighter", "Noukkis" + (int) (Math.random() * 100), "localhost", 5000);
        cmd.showAndWait().ifPresent((client) -> {
            RoomChooser rm = new RoomChooser(client, () -> {
                return client.getConnectedRoom() != null;
            });
            rm.showAndWait();
            if (client.getConnectedRoom() != null) {
                loadGame(stage, client);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void loadGame(Stage stage, Nouklient client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
            Parent root = loader.load();
            ViewCtrl ctrl = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.setFullScreen(true);
            stage.show();
            ctrl.init(client);

            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
