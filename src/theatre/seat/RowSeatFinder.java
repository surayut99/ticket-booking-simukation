package theatre.seat;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public interface RowSeatFinder {
    HBox findPosition(int row, VBox box);
}
