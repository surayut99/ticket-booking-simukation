package theatre.TestingPackage.TestingSchedule;

import theatre.movies.Movies;
import theatre.TestingPackage.TestingSeat.TestingSeat;

import java.util.ArrayList;

public class TestingSchedule {
    private Movies movies;
    private TestingSeat[][] seats;
    private String startTime;
    private int totalSeat;
    private int numAvailableSeat;

    public TestingSchedule(Movies movies, TestingSeat[][] seats, String startTime) {
        this.movies = movies;
        this.seats = seats;
        this.startTime = startTime;

        this.totalSeat = seats.length * seats[0].length;
        this.numAvailableSeat = totalSeat;
    }

    public void setReservedSeat(ArrayList<String> seatPositions) {
        for (String position : seatPositions) {
            int row = position.charAt(0) - 65;
            int column = Integer.parseInt(position.substring(1)) - 1;
            seats[row][column].setReserve(true);
        }
    }

    public void setNumAvailableSeat(int numReserved) {
        this.numAvailableSeat -= numReserved;
    }

    public Movies getMovies() {
        return movies;
    }

    public TestingSeat[][] getSeats() {
        return seats;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public int getNumAvailableSeat() {
        return numAvailableSeat;
    }
}
