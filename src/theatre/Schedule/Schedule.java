package theatre.Schedule;

import theatre.ShowingSystem.ShowingSystem;
import theatre.movies.Movies;

public class Schedule {
    private int startHour = 11;
    private int startMinute = 0;


    public ShowingSystem createSchedule(Movies movie, String typeSystem) {
        String hour = changeIntToString(this.startHour), minute = changeIntToString(this.startMinute);
        ShowingSystem showingSystem = new ShowingSystem(typeSystem, movie, hour + ":" + minute);

        String[] movieLength = movie.getLength().split(":");
        int endHour = startHour + Integer.parseInt(movieLength[0]);
        int endMinute = startMinute + Integer.parseInt(movieLength[1]) + 15;

        if (endMinute > 59) {
            int exceedMinute = endMinute / 60;

            endHour += exceedMinute;
            endMinute = endMinute % 60;
        }

        if (endHour > 23) {
            endHour -= 24;
        }

        this.startHour = endHour;
        this.startMinute = endMinute;

        return showingSystem;
    }

    public String changeIntToString(int time) {
        if (time < 10) {
            return "0" + time;
        }

        return "" + time;
    }

    public void resetTime() {
        this.startHour = 11;
        this.startMinute = 0;
    }
}
