package theatre.showingSystem;

import theatre.movies.Movies;
import theatre.seat.Seat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowingSystem {
    private String systemType;
    private double price;
    private ArrayList<Schedule> schedules;
    private ArrayList<String> seatTypes;
    private ArrayList<Double> seatPrices;
    private Seat[][] seats;

    public ShowingSystem(String systemType, double price, Seat[][] seats) {
        this.seats = seats;
        this.price = price;
        this.systemType = systemType;
        this.schedules = new ArrayList<>();
        this.seatTypes = new ArrayList<>();
        this.seatPrices = new ArrayList<>();
    }

    public void addSequenceMovies(Movies...movies) {
        for (Movies movie : movies) {
            if (schedules.size() == 0) {
                schedules.add(new Schedule(movie, "10:00", (seats.length * seats[0].length)));
                continue;
            }

            String startTime = schedules.get(schedules.size() - 1).getStartTime();
            List<String> times = new ArrayList<>(Arrays.asList(schedules.get(schedules.size() - 1).getMovies().getLength().split(":")));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
            startTime = df.format(LocalTime.parse(startTime).plusHours(Integer.parseInt(times.get(0))));
            startTime = df.format(LocalTime.parse(startTime).plusMinutes(Integer.parseInt(times.get(1))));
            startTime = df.format(LocalTime.parse(startTime).plusMinutes(15));
            if (LocalTime.parse(startTime).getHour() < 10) {return;}
            schedules.add(new Schedule(movie, startTime, (seats.length * seats[0].length)));
        }
    }

    public String getSystemType() {
        return systemType;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public ArrayList<String> getSeatTypes() {
        return seatTypes;
    }

    public ArrayList<Double> getSeatPrices() {
        return seatPrices;
    }

    public Seat[][] getSeats() {
        return this.seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addSeatDetail() {
        String currentType = "no-type";
        for (Seat[] seat : seats) {
            if (!seat[0].getSeatType().equals(currentType)) {
                currentType = seat[0].getSeatType();
                seatTypes.add(currentType);
                seatPrices.add(seat[0].getPrice());
            }
        }
    }
}
