package theatre.showingSystem;

import theatre.movies.Movies;

import java.util.ArrayList;

public class TestingSchedule {
    private Movies movies;
    private String startTime;
    private ArrayList<String> reservedPositionSeat;
    private int totalSeat;
    private int numAvailableSeat;

    public TestingSchedule(Movies movies, String startTime, int totalSeat) {
        this.movies = movies;
        this.startTime = startTime;

        this.totalSeat = totalSeat;
        this.numAvailableSeat = totalSeat;
        this.reservedPositionSeat = new ArrayList<>();
    }

    public void setReservedSeat(ArrayList<String> seatPositions) {
//        for (String position : seatPositions) {
//            int row = position.charAt(0) - 65;
//            int column = Integer.parseInt(position.substring(1)) - 1;
//            seats[row][column].setReserve(true);
//        }
        reservedPositionSeat.addAll(seatPositions);
        numAvailableSeat -= seatPositions.size();
    }

    public Movies getMovies() {
        return movies;
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

    public ArrayList<String> getReservedPositionSeat() {
        return reservedPositionSeat;
    }
}
