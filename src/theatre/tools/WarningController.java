package theatre.tools;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class WarningController {
    public static void showWarning(Node warning) {
        PageController.getMainShowContent().setEffect(new ColorAdjust(0, 0, -0.7, 0));
        Label warningLabel = new Label();
        warningLabel.setGraphic(warning);
        VBox box = new VBox();
        box.getChildren().add(warningLabel);
        box.setAlignment(Pos.CENTER);
        NodeCreator.setAlignmentNodeOnAnchorPane(box, 0.0, 0.0, 0.0, 0.0);
        //show warning message.
        ((AnchorPane) PageController.getStackPages().firstElement()).getChildren().add(box);
    }

    public static void hideWarning() {
        AnchorPane firstPane = (AnchorPane) PageController.getStackPages().firstElement();
        firstPane.getChildren().remove(1);
        PageController.getMainShowContent().setEffect(null);
    }
}
