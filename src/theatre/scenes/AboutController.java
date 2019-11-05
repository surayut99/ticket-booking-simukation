package theatre.scenes;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import theatre.tools.EffectController;

public class AboutController {

    @FXML
    AnchorPane mainPane, showContents;

    @FXML
    public void initialize() {
        showContents.prefHeightProperty().bind(mainPane.heightProperty());
    }

    @FXML
    private void backToMain(ActionEvent event) {
        AnchorPane parent = (AnchorPane) mainPane.getParent();
        Timeline timeline = EffectController.createTranslateTimeLine(mainPane.translateYProperty(), mainPane.getHeight(), 0.25);
        timeline.setOnFinished(e -> parent.getChildren().remove(mainPane));
        timeline.play();
    }
}
