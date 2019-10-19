package theatre.TestingPackage.TestingShowingSystem;

import theatre.movies.Movies;
import theatre.TestingPackage.TestingSeat.TestingSeat;

import java.util.ArrayList;

public class Testing3DSystem extends TestingShowingSystem{
    public Testing3DSystem(String systemType, double price, TestingSeat[][] seats, ArrayList<Movies> sequenceMovies, ArrayList<String> sequenceStartingTime) {
        super(systemType, price, seats);
    }

    public void increasePrice() {
        super.setPrice(super.getPrice() + 60);
    }
}
