package theatre.tools;

import theatre.movies.ComingSoonMovies;
import theatre.movies.MovieCollector;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.seat.Seat;
import theatre.seat.SeatController;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.AccountData.Account;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataController {

    public static void loadUserData(){
        try {
            FileReader reader = new FileReader(new File("src/account_data/accounts.csv"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] accounts = line.split(",");
                line = bufferedReader.readLine();
                String[] data = line.split(",");
                Account account = new Account(
                        accounts[0],
                        accounts[1],
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        Double.parseDouble(data[5]));
                AccountCollector.addAccount(account.getUsername(), account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadReservingData() {
        try {
            FileReader reader = new FileReader(new File("src/account_data/BookingData.csv"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            String currentUser = null;
            String currentStartTime = null;
            String titleMovie = null;
            int currentTheatre = -1;
            while ((line = bufferedReader.readLine()) != null) {
                int contentAfterCommaIndex = line.indexOf(',') + 1;

                if (line.contains("username,"))
                    currentUser = line.substring(contentAfterCommaIndex);

                else if (line.contains("theatre,"))
                    currentTheatre = Integer.parseInt(line.substring(contentAfterCommaIndex)) - 1;

                else if (line.contains("movie,"))
                    titleMovie = line.substring(contentAfterCommaIndex);

                else if (line.contains("schedule,")) {
                    currentStartTime = line.substring(contentAfterCommaIndex);
                }

                else if (line.contains("positions,")) {
                    String seatString = line.substring(contentAfterCommaIndex);
                    String[] seats = seatString.split(" ");
                    ArrayList<Schedule> schedules = ShowingSystemCollector.getShowingSystems()[currentTheatre].getSchedules();
                    Schedule selectedSchedule = null;
                    for (Schedule schedule : schedules) {
                        if (schedule.getStartTime().equals(currentStartTime)) {
                            selectedSchedule = schedule;
                            break;
                        }
                    }
                    Account currentAccount = AccountCollector.getAccount(currentUser);
                    currentAccount.addBooking(currentTheatre, titleMovie, selectedSchedule, seats);
                    selectedSchedule.addReservedSeat(seats);
                }
            }
            bufferedReader.close();
            reader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create and add showing system object to ShowingSystemCollector.
    public static void loadShowingSystem() {
        String[] systemTypes = {"Normal", "4K", "4K", "3D", "3D"};
        String[] patternSeat = {"Normal", "Normal", "Hybrid", "Hybrid", "Hybrid"};
        int[][] numSeat = {{5, 8}, {8, 10}, {10, 12}, {8, 10}, {8, 8}};

        for (int i = 0; i < 5; i++) {
            Seat[][] seats = SeatController.createSeatArray(patternSeat[i], numSeat[i][0], numSeat[i][1], 200);
            ShowingSystemCollector.addShowingSystem(i, 20, systemTypes[i], seats);
        }
        loadMovies();
        loadScheduleShowtime();
    }

    //add movie object to MoviesCollector class
    private static void loadMovies() {
        if (!MovieCollector.moviesList.isEmpty()) return;

        LocalDate date = LocalDate.now();
        MovieCollector.moviesList.add(new ShowingMovies(
                "Spider-Man: Homeless", "02:02:02", date, "picture/poster/spider-man-homeless.jpg"
        ));
        MovieCollector.moviesList.add(new ShowingMovies(
                "Boosty And the Beast","01:56:49", date, "picture/poster/booty_and_the_beast.jpg"
        ));
        MovieCollector.moviesList.add(new ShowingMovies(
                "Once Upon Deadpool", "02:34:08", date, "picture/poster/once_upon_deadpool.jpg"
        ));


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateSoon = LocalDate.parse("09/09/2020", dateTimeFormatter);
        MovieCollector.moviesList.add(new ComingSoonMovies("INCEPTION", "--:--:--", dateSoon, "picture/poster/inception.jpg"));
    }

    //create and add schedule for each showing system.
    private static void loadScheduleShowtime() {
        Movies spiderman = MovieCollector.findMovie("spider");
        Movies boosty = MovieCollector.findMovie("boosty");
        Movies deadpool = MovieCollector.findMovie("deadpool");

        ShowingSystemCollector.addScheduleMovies(0, spiderman, spiderman, boosty, deadpool, boosty, deadpool, deadpool);
        ShowingSystemCollector.addScheduleMovies(1, deadpool, boosty, boosty, spiderman, deadpool, spiderman);
        ShowingSystemCollector.addScheduleMovies(2, boosty, spiderman, deadpool, deadpool, boosty, boosty);
        ShowingSystemCollector.addScheduleMovies(3, deadpool, spiderman, deadpool, spiderman, boosty, boosty);
        ShowingSystemCollector.addScheduleMovies(4, spiderman, boosty, spiderman, boosty, deadpool, deadpool);
    }

    public static void editReservingData(String theatre, String oldVersion, String newVersion, String startTime, String titleMovie) {
        List<String> oldData = new ArrayList<>();
        String username = AccountCollector.getCurrentAccount().getUsername();
        boolean edited = false;
        try {
            FileWriter writer = new FileWriter(new File("src/account_data/tmpBookingData.csv"));
            FileReader reader = new FileReader((new File("src/account_data/BookingData.csv")));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("username," + username)) {
                    oldData.add(line);
                    while ((line = bufferedReader.readLine()) != null) {
                        oldData.add(line);
                        if (line.isEmpty()) break;
                    }
                    for (int i = 0; i < oldData.size(); i++) {
                        if (oldData.get(i).contains("theatre," + theatre)) {
                            for (int j = i + 1; j < oldData.size(); j++) {
                                if (oldVersion.isEmpty() && oldData.get(j).contains("movie," + titleMovie)) {
                                    oldData.add(j + 1, "\t\tpositions," + newVersion);
                                    oldData.add(j + 1, "\t\tschedule," + startTime);
                                    edited = true;
                                    break;
                                }
                                if (oldData.get(j).isEmpty() || oldData.get(j).contains("theatre")) {
                                    oldData.add(j, "\t\tpositions," + newVersion);
                                    oldData.add(j, "\t\tschedule," + startTime);
                                    oldData.add(j, "\tmovie," + titleMovie);
                                    edited = true;
                                    break;
                                }
                                if (oldData.get(j).contains(startTime)) {
                                    String oldString = oldData.get(++j);
                                    String newString = oldString.replaceAll(oldVersion, newVersion);
                                    oldData.remove(j);
                                    oldData.add(j, newString);
                                    edited = true;
                                    break;
                                }

                            }
                        }
                        if (edited) break;
                        if (oldData.get(i).contains("theatre,")) {
                            String[] split = oldData.get(i).split(",");
                            if (Integer.parseInt(split[1]) > Integer.parseInt(theatre)) {
                                oldData.add(i, "\t\tpositions," + newVersion);
                                oldData.add(i, "\t\tschedule," + startTime);
                                oldData.add(i, "\tmovie," + titleMovie);
                                oldData.add(i, "theatre," + theatre);
                                break;
                            }
                        }
                        if (oldData.get(i).equals("")) {
                            oldData.add(i, "\t\tpositions," + newVersion);
                            oldData.add(i, "\t\tschedule," + startTime);
                            oldData.add(i, "\tmovie," + titleMovie);
                            oldData.add(i, "theatre," + theatre);
                            break;
                        }
                    }
                    String newData = String.join("\n", oldData);
                    writer.write(newData);
                    writer.flush();
                }
                writer.write(line + "\n");
                writer.flush();
            }
            reader.close();
            writer.close();
            bufferedReader.close();
            File file = new File("src/account_data/tmpBookingData.csv");
            File destFile = new File("src/account_data/BookingData.csv");
            destFile.delete();
            file.renameTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeReservingData(String theatre, String oldVersion, String newVersion, String startTime, String titleMovie) {
        List<String> oldData = new ArrayList<>();
        String username = AccountCollector.getCurrentAccount().getUsername();
        try {
            FileWriter writer = new FileWriter(new File("src/account_data/tmpBookingData.csv"));
            FileReader reader = new FileReader((new File("src/account_data/BookingData.csv")));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("username," + username)) {
                    writer.write(line + "\n");
                    writer.flush();

                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.isEmpty()) break;
                        if (line.contains("theatre," + theatre)) {
                            oldData.add(line);
                            while ((line = bufferedReader.readLine()) != null) {
                                if (line.isEmpty()) break;
                                if (line.contains("theatre")) break;
                                oldData.add(line);
                            }

                            for (int i = 0; i < oldData.size(); i++) {
                                if (oldData.get(i).contains("movie," + titleMovie)) {
                                    if (oldData.get(i + 1).contains("schedule," + startTime)) {
                                        if (newVersion.equals("")) {
                                            for (int j = 0; j < 2; j++) {
                                                oldData.remove(i + 1);
                                            }
                                            if (oldData.size() == i + 1) {
                                                oldData.remove(i);
                                                break;
                                            }
                                            if (oldData.get(i + 1).contains("schedule")) {
                                                break;
                                            }
                                            oldData.remove(i);
                                        }
                                        else {
                                            i = i + 2;
                                            String data = oldData.get(i);
                                            String newData = data.replaceAll(oldVersion, newVersion);
                                            oldData.set(i, newData);
                                            break;
                                        }
                                    }
                                }
                            }

                            String data = oldData.size() == 1? "" : String.join("\n", oldData) + "\n";
                            writer.write(data);
                            writer.flush();
                            break;
                        }
                        writer.write(line + "\n");
                        writer.flush();
                    }
                }
                writer.write(line + "\n");
                writer.flush();
            }
            reader.close();
            writer.close();
            bufferedReader.close();
            File file = new File("src/account_data/tmpBookingData.csv");
            File destFile = new File("src/account_data/BookingData.csv");
            destFile.delete();
            file.renameTo(destFile);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editBalance(String username, double balance) {
        try {
            FileWriter writer = new FileWriter(new File("src/account_data/tmp_accounts.csv"));
            FileReader reader = new FileReader(new File("src/account_data/accounts.csv"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                writer.write(line + "\n");
                String[] accounts = line.split(",");
                if (accounts[0].equals(username)) {
                    line = bufferedReader.readLine();
                    List<String> dataList = new ArrayList<>(Arrays.asList(line.split(",")));
                    Collections.reverse(dataList);
                    dataList.remove(0);
                    dataList.add(0, Double.toString(balance));
                    Collections.reverse(dataList);
                    String newData = String.join(",", dataList);
                    writer.write(newData + "\n");
                }
            }
            writer.close();
            reader.close();
            bufferedReader.close();

            File newFile = new File("src/account_data/tmp_accounts.csv");
            File oldFile = new File("src/account_data/accounts.csv");
            oldFile.delete();
            newFile.renameTo(oldFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}