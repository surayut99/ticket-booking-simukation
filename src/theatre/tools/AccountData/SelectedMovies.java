package theatre.tools.AccountData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import theatre.movies.Movies;
import theatre.showingSystem.Schedule;
import theatre.tools.NodeCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectedMovies {
    private Movies movies;
    private ArrayList<Schedule> schedules;
    private ArrayList<String> bookedSeat;
    private ObservableList<Button> btnList;

    public SelectedMovies(Movies movies) {
        this.movies = movies;
        this.schedules = new ArrayList<>();
        this.bookedSeat = new ArrayList<>();
        btnList = FXCollections.observableArrayList();
    }

    public Movies getMovies() {
        return movies;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void addSchedule(Schedule schedule, String[] position) {
        if (schedules.contains(schedule)) {
            extendPosition(schedule, position);
            return;
        }

        Button scheduleBtn = new Button(schedule.getStartTime());
        scheduleBtn.setFont(Font.font("System", FontWeight.BOLD, 24));
        scheduleBtn.setTextFill(Color.BLACK);
        scheduleBtn.setCursor(Cursor.HAND);
        scheduleBtn.setStyle("-fx-background-color: linear-gradient(#FDC830,#F37335);\n" +
                "    -fx-background-radius: 25;\n" +
                "    -fx-background-insets: 0;");
        scheduleBtn.setEffect(new DropShadow());

        btnList.add(scheduleBtn);
        schedules.add(schedule);
        bookedSeat.add(String.join(",", position));
    }

    public void removeSeat(Schedule schedule, String[] position) {
        int index = schedules.indexOf(schedule);
        ArrayList<String> oldPosition = new ArrayList<>(Arrays.asList(bookedSeat.get(index).split(",")));
        ArrayList<String> removing = new ArrayList<>(Arrays.asList(position));
        oldPosition.removeAll(removing);
        if (oldPosition.isEmpty()) {
            bookedSeat.remove(index);
            schedules.remove(index);
            btnList.remove(index);
            return;
        }
        bookedSeat.set(index, String.join(",", oldPosition));
    }

    public ArrayList<String> getBookedSeat() {
        return bookedSeat;
    }

//    public ArrayList<String> getBookedSeatByStartTime(Schedule schedule) {
//        int index = schedules.indexOf(schedule);
//        String position = bookedSeat.get(index);
//        return new ArrayList<>(Arrays.asList(position.split(",")));
//    }

    private void extendPosition(Schedule schedule, String[] position) {
        int index = schedules.indexOf(schedule);

        String old = bookedSeat.get(index);
        String newStr = String.join(",", position);
        old += "," + newStr;
        List<String> list = new ArrayList<>(Arrays.asList(old.split(",")));
        Collections.sort(list);
        bookedSeat.set(index, String.join(",", list));
    }

    public ObservableList<Button> getBtnList() {
        return btnList;
    }
}
