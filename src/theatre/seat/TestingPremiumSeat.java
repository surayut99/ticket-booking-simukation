package theatre.seat;

public class TestingPremiumSeat extends TestingSeat {
    private String typeSeat;

    public TestingPremiumSeat(String typeSeat, double price, String seatImgPath, String seatPosition, boolean reserve) {
        super(typeSeat, price, seatImgPath, seatPosition, reserve);
        this.typeSeat = typeSeat;
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 20);
    }
}
