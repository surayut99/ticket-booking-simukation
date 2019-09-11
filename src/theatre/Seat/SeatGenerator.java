package theatre.Seat;

import javafx.scene.image.ImageView;

public class SeatGenerator {

    public Seat[][] normalSeat(int row, int column, int price) {
        Seat[][] seats = new Seat[row][column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                seats[i][j] = new Seat("Normal", "/picture/seat/normal.png", price, false);
//                createSeatImage(seats[i][j]);
            }
        }

        return seats;
    }

    public Seat[][] hybridSeat(int row, int column, int startPrice) {
        Seat[][] seats = new Seat[row][column];

        for (int i = 0; i < row; i++) {
            if (i < 2) {
                for (int j = 0; j < column; j++) {
                    seats[i][j] = new Seat("VIP","picture/seat/vip.png", startPrice + 40, false);
//                    createSeatImage(seats[i][j]);
                }
            }

            else if (i < 5) {
                for (int j = 0; j < column; j++) {
                    seats[i][j] = new Seat("Premium", "picture/seat/premium.png", startPrice + 20, false);
//                    createSeatImage(seats[i][j]);
                }
            }

            else {
                for (int j = 0; j < column; j++) {
                    seats[i][j] = new Seat("Normal", "picture/seat/normal.png", startPrice, false);
//                    createSeatImage(seats[i][j]);
                }
            }
        }

        return seats;
    }

    private void createSeatImage(Seat seat) {
        ImageView imgSeat = new ImageView(seat.getSeatImgPath());
        imgSeat.setFitHeight(50);
        imgSeat.setFitWidth(50);

        seat.setSeatImg(imgSeat);
    }
}
