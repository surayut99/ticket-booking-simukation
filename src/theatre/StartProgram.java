package theatre;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class StartProgram {

    @FXML Label clickToContinue, welcomeLabel, sampleText;

    @FXML StackPane stackPane;

    @FXML AnchorPane startAnchor;

    @FXML public void initialize() {
        startAnchor.prefWidthProperty().bind(stackPane.widthProperty());
        startAnchor.prefHeightProperty().bind(stackPane.heightProperty());

        FadeTransition fadeAnchor = new FadeTransition(Duration.seconds(1), startAnchor);
        fadeAnchor.setToValue(1);
        fadeAnchor.setDelay(Duration.seconds(1));

        FadeTransition fadeWelcome = new FadeTransition(Duration.seconds(0.5), welcomeLabel);
        fadeWelcome.setToValue(1);

        FadeTransition fadeSample = new FadeTransition(Duration.seconds(1), sampleText);
        fadeSample.setToValue(1);

        FadeTransition fadeClick = new FadeTransition(Duration.seconds(1), clickToContinue);
        fadeClick.setToValue(1);

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(fadeAnchor, fadeWelcome, fadeSample, fadeClick);
        sequentialTransition.play();
        sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clickToContinue.setDisable(false);
            }
        });


    }


    @FXML public void clickOnAction(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        Scene scene = clickToContinue.getScene();

        root.translateXProperty().set(scene.getWidth());
        stackPane.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}