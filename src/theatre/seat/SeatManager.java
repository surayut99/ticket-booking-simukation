package theatre.seat;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SeatManager {
    public static Seat[][] createSeatArray(String pattern, int row, int column, double startPrice) {
        Seat[][] seats = new Seat[row][column];

        for (int i = 0; i < row; i++) {
            char letter = (char) ('A' + i);
            for (int j = 0; j < column; j++) {
                String position = Character.toString(letter) + (j + 1);
                if (pattern.equals("Normal")) {
                    seats[i][j] = createSeatObject("Normal", position, startPrice);
                    continue;
                }

                if (pattern.equals("Hybrid")) {
                    if (i == 0) { seats[i][j] = createSeatObject("VIP", position, startPrice); }
                    else if (i == 1 || i == 2) { seats[i][j] = createSeatObject("Premium", position, startPrice); }
                    else seats[i][j] = createSeatObject("Normal", position, startPrice);
                }
            }
        }
        return seats;
    }

    private static Seat createSeatObject(String type, String position, double startPrice) {
        switch (type) {
            case "Premium":
                PremiumSeat premiumSeat = new PremiumSeat(type, startPrice, "picture/seat/premium.png", position, false);
                premiumSeat.increasePrice();
                return premiumSeat;

            case "VIP":
                VIPSeat vipSeat = new VIPSeat(type, startPrice, "picture/seat/vip.png", position, false);
                vipSeat.increasePrice();
                return vipSeat;

            default:
                return new Seat(type, startPrice, "picture/seat/normal.png", position, false);
        }
    }

    public static AnchorPane getGroupImageByPosition(RowSeatFinder pattern, VBox box, int row, int column, String patternSeat) {
        if (patternSeat.equals("Normal") || patternSeat.equals("Hybrid")) {
            return (AnchorPane) (pattern.findPosition(row, box)).getChildren().get(column);
        }

        HBox rowBox = pattern.findPosition(row, box);
        rowBox = (HBox) rowBox.getChildren().get(column / 2);
        return (AnchorPane) rowBox.getChildren().get(column % 2);
    }
}
