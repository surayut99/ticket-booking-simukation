package theatre.ShowingSystem;

import javafx.scene.control.Label;
import theatre.Seat.Seat;
import theatre.Seat.SeatGenerator;
import theatre.movies.Movies;


public class ShowingSystem {
    private String typeSystem;
    private Movies movies;
    private Seat[][] seats;
    private String timeStart;
    private Label timeLabel;

    private SeatGenerator seatGenerator;

    public ShowingSystem(String typeSystem, Movies movies, String timeStart) {
        this.typeSystem = typeSystem;
        this.movies = movies;
        this.timeStart = timeStart;

        seatGenerator = new SeatGenerator();
    }

    public String getTypeSystem() {
        return typeSystem;
    }

    public Movies getMovies() {
        return movies;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public Seat[][] getSeats() {
        return seats;
    }

    public void generateSeat(int row, int column, int starPrice) {
        if (this.typeSystem.contains("Normal")) {
            this.seats = seatGenerator.normalSeat(row, column, starPrice);
        }

        else if (this.typeSystem.contains("Hybrid") || this.typeSystem.contains("Couple")) {
            this.seats = seatGenerator.hybridSeat(row, column, starPrice);
        }
    }

    public void setLabel(Label label) {
        this.timeLabel = label;
    }

    public Label getTimeLabel() {
        return this.timeLabel;
    }
}