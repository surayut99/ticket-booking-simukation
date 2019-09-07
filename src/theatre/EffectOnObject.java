package theatre;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class EffectOnObject {

    public Lighting clickAndchangLabelColor() {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0);

        return lighting;
    }

    public void changeColorOnSelectedLabel(Label effectedLabel, double value) {
        Lighting lighting = (Lighting) effectedLabel.getEffect();

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(lighting.diffuseConstantProperty(), value, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(250), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void changeScaleOnSelectedNode(Node selectedPoster, double value) {
        ParallelTransition changeScale = new ParallelTransition();

        ScaleTransition xScale = new ScaleTransition(Duration.seconds(0.125), selectedPoster);
        xScale.setToX(value);

        ScaleTransition yScale = new ScaleTransition(Duration.seconds(0.125), selectedPoster);
        yScale.setToY(value);

        changeScale.getChildren().addAll(xScale, yScale);
        changeScale.play();
    }

    public void fadeMoviesInRow(AnchorPane anchorPane) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), anchorPane);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> finishTransition());
    }

    public void finishTransition() {
    }


}