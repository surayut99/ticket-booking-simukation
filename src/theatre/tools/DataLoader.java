package theatre.tools;

import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.showingSystem.ShowingSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataLoader {
    public static void loadReservingData() {
        try {
            FileReader reader = new FileReader(new File("data/account_data/reserving_data.csv"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            String currentUser = null;
            String currentStartTime = null;
            int currentTheatre = -1;
            while ((line = bufferedReader.readLine()) != null) {
                int contentAfterCommaIndex = line.indexOf(',') + 1;

                if (line.contains("username,")) {
                    currentUser = line.substring(contentAfterCommaIndex);
                }

                else if (line.contains("theatre")) {
                    currentTheatre = Integer.parseInt(line.substring(contentAfterCommaIndex)) - 1;
                }

                else if (line.contains("schedule")) {
                    currentStartTime = line.substring(contentAfterCommaIndex);
                }

                else if (line.contains("position")) {
                    String seatString = line.substring(contentAfterCommaIndex);
                    String[] seats = seatString.split(",");
                    ArrayList<Schedule> schedules = ShowingSystemCollector.showingSystems[currentTheatre].getSchedules();
                    Schedule selectedSchedule = null;
                    for (Schedule schedule : schedules) {
                        if (schedule.getStartTime().equals(currentStartTime)) {
                            selectedSchedule = schedule;
                            break;
                        }
                    }
                    selectedSchedule.addReservedSeat(seats);
                    if (currentUser.equals(AccountController.getUsername())) selectedSchedule.addReservedSeatUser(seats);
                }
            }

            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTheatre() {
        ShowingSystem[] systems = ShowingSystemCollector.showingSystems;
        for (ShowingSystem s : systems) {
            System.out.println(s.getSystemType());
            ArrayList<Schedule> schedules = s.getSchedules();
            for (Schedule schedule : schedules) {
                System.out.println(schedule.getMovies().getTitle());
                System.out.println(schedule.getStartTime());
                System.out.println(schedule.getNumAvailableSeat());
            }
        }
        System.out.println();
    }
}
