package theatre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class Theatre {
    private EffectedPointer effectedPointer = new EffectedPointer();
    private ObservableList<Label> labelList = FXCollections.observableArrayList();
    private Label preLabel;

    @FXML Label showingOption, comingSoonOption, accountOption, logInOption;

    @FXML public void initialize() {
        labelList.addAll(showingOption, comingSoonOption, accountOption, logInOption);
        preLabel = showingOption;
    }

    @FXML public void mouseEnterOnLabel(MouseEvent event) {
        for (int i = 0; i < labelList.size(); i++) {
            if (labelList.get(i).isHover() && !labelList.get(i).getStyle().equals("-fx-background-color: #1F618D;")) {
                effectedPointer.createChangingTimeline(labelList.get(i), 0);
            }
        }
    }

    @FXML public void mouseExitOnLabel(MouseEvent event) {
        for (int i = 0; i < labelList.size(); i++) {
            if (!labelList.get(i).isHover()) {
                effectedPointer.createChangingTimeline(labelList.get(i), 2);
            }
        }
    }

    @FXML public void mouseClickOnLabel(MouseEvent event) {
        for (int i = 0; i < labelList.size(); i++) {
            if (labelList.get(i).isHover() && labelList.get(i) != preLabel) {
                preLabel.setStyle("-fx-background-color: null");
                labelList.get(i).setEffect(effectedPointer.clickAndchangLabelColor());
                labelList.get(i).setStyle("-fx-background-color: #1F618D;");
                preLabel = labelList.get(i);
                break;
            }
        }
    }
}