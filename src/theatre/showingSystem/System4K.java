package theatre.showingSystem;

import theatre.seat.Seat;

public class System4K extends ShowingSystem {
    public System4K(String systemType, double price, Seat[][] seats) {
        super(systemType, price, seats);
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 40);
    }
}
