package theatre.showingSystem;

import theatre.seat.TestingSeat;

public class Testing3DSystem extends TestingShowingSystem {
    public Testing3DSystem(String systemType, double price, TestingSeat[][] seats) {
        super(systemType, price, seats);
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 60);
    }
}
