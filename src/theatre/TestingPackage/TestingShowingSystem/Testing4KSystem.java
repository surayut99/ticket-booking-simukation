package theatre.TestingPackage.TestingShowingSystem;

import theatre.movies.Movies;
import theatre.TestingPackage.TestingSeat.TestingSeat;

import java.util.ArrayList;

public class Testing4KSystem extends TestingShowingSystem {
    public Testing4KSystem(String systemType, double price, TestingSeat[][] seats, ArrayList<Movies> sequenceMovies, ArrayList<String> sequenceStartingTime) {
        super(systemType, price, seats);
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 40);
    }
}
