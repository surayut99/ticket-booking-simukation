package theatre.TestingPackage.TestingShowingSystem;

import theatre.movies.Movies;
//import theatre.Schedule;
import theatre.TestingPackage.TestingSchedule.TestingSchedule;
import theatre.TestingPackage.TestingSeat.TestingSeat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestingShowingSystem {
    private String systemType;
    private double price;
    private ArrayList<TestingSchedule> schedules;
    private TestingSeat[][] seats;

    public TestingShowingSystem(String systemType, double price, TestingSeat[][] seats) {
        this.seats = seats;
        this.price = price;
        this.systemType = systemType;
        this.schedules = new ArrayList<>();
    }

    public void addSequenceMovies(Movies movie) {
        if (schedules.size() == 0) {
            schedules.add(new TestingSchedule(movie, seats, "08:00"));
            return;
        }

        String startTime = schedules.get(schedules.size() - 1).getStartTime();
        List<String> times = new ArrayList<>(Arrays.asList(schedules.get(schedules.size() - 1).getMovies().getLength().split(":")));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        startTime = df.format(LocalTime.parse(startTime).plusHours(Integer.parseInt(times.get(0))));
        startTime = df.format(LocalTime.parse(startTime).plusMinutes(Integer.parseInt(times.get(1))));
        startTime = df.format(LocalTime.parse(startTime).plusMinutes(15));
        schedules.add(new TestingSchedule(movie, seats, startTime));
    }

    public ArrayList<TestingSchedule> getSchedules() {
        return schedules;
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
}
