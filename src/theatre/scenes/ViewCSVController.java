package theatre.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ViewCSVController {
    @FXML
    TextArea area;

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    @FXML public void initialize() {
        area.setText(content);
    }
}
