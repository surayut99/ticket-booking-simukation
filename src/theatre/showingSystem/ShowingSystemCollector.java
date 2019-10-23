package theatre.showingSystem;

import theatre.movies.Movies;
import theatre.seat.Seat;

public class ShowingSystemCollector {
    public static ShowingSystem[] showingSystems = new ShowingSystem[5];

    public static void addShowingSystem(int sequenceTheatre, double startPrice, String typeSystem, Seat[][] seats) {
        switch (typeSystem) {
            case "3D":
                System3D system3D = new System3D(typeSystem, startPrice, seats);
                system3D.increasePrice();
                showingSystems[sequenceTheatre] = system3D;
                break;
            case "4K":
                System4K system4K = new System4K(typeSystem, startPrice, seats);
                system4K.increasePrice();
                showingSystems[sequenceTheatre] = system4K;
                break;
            default:
                showingSystems[sequenceTheatre] = new ShowingSystem(typeSystem, startPrice, seats);
                break;
        }

        showingSystems[sequenceTheatre].addSeatDetail();
    }

    public static void addScheduleMovies(int sequenceTheatre, Movies ...movies) {
        showingSystems[sequenceTheatre].addSequenceMovies(movies);
    }

    public static int getSequenceTheatre(ShowingSystem s) {
        for (int i = 0; i < 5; i ++) {
            if (showingSystems[i] == s) return i;
        }
        return -1;
    }
}
