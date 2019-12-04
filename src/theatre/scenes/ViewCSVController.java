package theatre.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ViewCSVController {
    private String content;

    @FXML
    TextArea area;

    @FXML public void initialize() {
        area.setText(content);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
