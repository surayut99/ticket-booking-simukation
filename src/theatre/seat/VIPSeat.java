package theatre.seat;

public class VIPSeat extends Seat {
    private String typeSeat;

    public VIPSeat(String typeSeat, double price, String seatImgPath, String seatPosition, boolean reserve) {
        super(typeSeat, price, seatImgPath, seatPosition, reserve);
        super.setPrice(increasePrice());
        this.typeSeat = typeSeat;
    }

    public double increasePrice() {
        return super.getPrice() + 40;
    }
}
