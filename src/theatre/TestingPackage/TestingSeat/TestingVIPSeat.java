package theatre.TestingPackage.TestingSeat;

public class TestingVIPSeat extends TestingSeat {
    private String typeSeat;

    public TestingVIPSeat(String typeSeat, double price, String seatImgPath, String seatPosition, boolean reserve) {
        super(typeSeat, price, seatImgPath, seatPosition, reserve);
        super.setPrice(increasePrice());
        this.typeSeat = typeSeat;
    }

    public double increasePrice() {
        return super.getPrice() + 40;
    }
}
