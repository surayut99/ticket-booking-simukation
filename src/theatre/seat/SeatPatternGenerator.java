package theatre.seat;

import javafx.scene.layout.HBox;

public interface SeatPatternGenerator {
    HBox generateRowSeat(Seat[] seats, double imgSize);
}
