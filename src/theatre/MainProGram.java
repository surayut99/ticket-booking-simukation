package theatre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainProgram extends Application {

    @Override
    public void start(Stage primaryStage) {
        String path = "/theatre/scenes/startScene.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            primaryStage.setTitle("SAMPLE CINEMA");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Can not find path: " + path);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
