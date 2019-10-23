package theatre.seat;

public class Seat {
    private String seatType;
    private double price;
    private String seatImgPath;
    private String seatPosition;
    private boolean reserve;

    public Seat(String seatType, double price, String seatImgPath, String seatPosition, boolean reserve) {
        this.seatType = seatType;
        this.price = price;
        this.seatImgPath = seatImgPath;
        this.seatPosition = seatPosition;
        this.reserve = reserve;
    }

    public double getPrice() {
        return price;
    }

    public String getSeatImgPath() {
        return seatImgPath;
    }

    public String getSeatPosition() {
        return seatPosition;
    }

    public String getSeatType() {
        return seatType;
    }

    public boolean isReserve() {
        return reserve;
    }

    public void setReserve(boolean reserve) {
        this.reserve = reserve;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
