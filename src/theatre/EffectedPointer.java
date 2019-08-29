package theatre;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.util.Duration;

public class EffectedPointer{

    public void createChangingTimeline(Label effectedLabel, double value) {
        Lighting lighting = (Lighting) effectedLabel.getEffect();

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(lighting.diffuseConstantProperty(), value, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(250), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public Lighting clickAndchangLabelColor() {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0);

        return lighting;
    }
}
