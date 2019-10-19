package theatre;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class EffectOnObject {

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

    public static  void changeScaleOnSelectedNode(Node selectedPoster, double value) {
        ParallelTransition changeScale = new ParallelTransition();

        ScaleTransition xScale = new ScaleTransition(Duration.seconds(0.125), selectedPoster);
        xScale.setToX(value);

        ScaleTransition yScale = new ScaleTransition(Duration.seconds(0.125), selectedPoster);
        yScale.setToY(value);

        changeScale.getChildren().addAll(xScale, yScale);
        changeScale.play();
    }

    public static  void fadeInNode(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), node);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public static  void fadeOutNode(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), node);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }


    public static  void changeColorSelectedSeat(Node node) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(-1);
        colorAdjust.setSaturation(1);
        colorAdjust.setHue(0.6);

        node.setEffect(colorAdjust);
    }

    public static  DropShadow createDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(21);
        dropShadow.setHeight(21);
        dropShadow.setRadius(10);

        return dropShadow;
    }
}