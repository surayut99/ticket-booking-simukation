package theatre.seat;

public class PremiumSeat extends Seat {
    private String typeSeat;

    public PremiumSeat(String typeSeat, double price, String seatImgPath, String seatPosition, boolean reserve) {
        super(typeSeat, price, seatImgPath, seatPosition, reserve);
        this.typeSeat = typeSeat;
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 20);
    }
}
