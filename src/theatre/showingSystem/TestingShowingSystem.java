package theatre.showingSystem;

import theatre.movies.Movies;
import theatre.seat.TestingSeat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestingShowingSystem {
    private String systemType;
    private double price;
    private ArrayList<TestingSchedule> schedules;
    private ArrayList<String> seatTypes;
    private ArrayList<Double> seatPrices;
    private TestingSeat[][] seats;

    public TestingShowingSystem(String systemType, double price, TestingSeat[][] seats) {
        this.seats = seats;
        this.price = price;
        this.systemType = systemType;
        this.schedules = new ArrayList<>();
    }

    public void addSequenceMovies(Movies...movies) {
        for (Movies movie : movies) {
            if (schedules.size() == 0) {
                schedules.add(new TestingSchedule(movie, "10:00", (seats.length * seats[0].length)));
                continue;
            }

            String startTime = schedules.get(schedules.size() - 1).getStartTime();
            List<String> times = new ArrayList<>(Arrays.asList(schedules.get(schedules.size() - 1).getMovies().getLength().split(":")));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
            startTime = df.format(LocalTime.parse(startTime).plusHours(Integer.parseInt(times.get(0))));
            startTime = df.format(LocalTime.parse(startTime).plusMinutes(Integer.parseInt(times.get(1))));
            startTime = df.format(LocalTime.parse(startTime).plusMinutes(15));
            if (LocalTime.parse(startTime).getHour() < 10) {return;}
            schedules.add(new TestingSchedule(movie, startTime, (seats.length * seats[0].length)));
        }
    }

    public String getSystemType() {
        return systemType;
    }

    public ArrayList<TestingSchedule> getSchedules() {
        return schedules;
    }

    public ArrayList<String> getSeatTypes() {
        return seatTypes;
    }

    public ArrayList<Double> getSeatPrices() {
        return seatPrices;
    }

    public TestingSeat[][] getSeats() {
        return this.seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSeatTypes(ArrayList<String> seatTypes) {
        this.seatTypes = seatTypes;
    }

    public void setSeatPrices(ArrayList<Double> seatPrices) {
        this.seatPrices = seatPrices;
    }
}
