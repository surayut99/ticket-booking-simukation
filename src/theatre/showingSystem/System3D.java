package theatre.showingSystem;

import theatre.seat.Seat;

public class System3D extends ShowingSystem {
    public System3D(String systemType, double price, Seat[][] seats) {
        super(systemType, price, seats);
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 60);
    }
}
