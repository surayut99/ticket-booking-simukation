package theatre.tools;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class EffectController {

    public static Lighting clickAndChangLabelColor() {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0);

        return lighting;
    }

    public static void changeColorOnSelectedLabel(Label effectedLabel, double value) {
        Lighting lighting = (Lighting) effectedLabel.getEffect();

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(lighting.diffuseConstantProperty(), value, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(250), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public static void changeScaleOnSelectedNode(Node selectedPoster, double value) {
        ParallelTransition changeScale = new ParallelTransition();

        ScaleTransition xScale = new ScaleTransition(Duration.seconds(0.125), selectedPoster);
        xScale.setToX(value);

        ScaleTransition yScale = new ScaleTransition(Duration.seconds(0.125), selectedPoster);
        yScale.setToY(value);

        changeScale.getChildren().addAll(xScale, yScale);
        changeScale.play();
    }

    public static FadeTransition createFadeTransition(Node node, double duration, double endValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setToValue(endValue);

        return fadeTransition;
    }

    public static TranslateTransition createTransition(Node node, double duration, double endX, double endY) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), node);
        transition.setByY(endX);
        transition.setByY(endY);
        return transition;
    }

    public static Timeline createTranslateTimeLine(DoubleProperty valueProperty, double endValue, double duration) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(valueProperty, endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        return timeline;
    }
}