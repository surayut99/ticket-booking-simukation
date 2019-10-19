package theatre.showingSystem;

import theatre.seat.TestingSeat;

public class Testing4KSystem extends TestingShowingSystem {
    public Testing4KSystem(String systemType, double price, TestingSeat[][] seats) {
        super(systemType, price, seats);
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 40);
    }
}
