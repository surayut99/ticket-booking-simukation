package theatre.seat;

import javafx.scene.layout.HBox;

public interface SeatPatternGenerator {
    HBox generateRowSeat(TestingSeat[] seats, double imgSize);
}
