package theatre.Seat;

public class SeatGenerator {

    public Seat[][] normalSeat(int row, int column, int price) {
        Seat[][] seats = new Seat[row][column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                seats[i][j] = new Seat("Normal", "/picture/seat/normal.png", price, false);
                seats[i][j].setSeatPosition(generateSeatPosition(i, j + 1));
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
                    seats[i][j].setSeatPosition(generateSeatPosition(i, j + 1));
                }
            }

            else if (i < 5) {
                for (int j = 0; j < column; j++) {
                    seats[i][j] = new Seat("Premium", "picture/seat/premium.png", startPrice + 20, false);
                    seats[i][j].setSeatPosition(generateSeatPosition(i, j + 1));
                }
            }

            else {
                for (int j = 0; j < column; j++) {
                    seats[i][j] = new Seat("Normal", "picture/seat/normal.png", startPrice, false);
                    seats[i][j].setSeatPosition(generateSeatPosition(i, j + 1));
                }
            }
        }

        return seats;
    }

    public String generateSeatPosition(int row, int position) {
        char startAlpha = 'A';
        char alpha = (char) (startAlpha + row);
        String alphaString = alpha + "";

        return alphaString + position;
    }
}