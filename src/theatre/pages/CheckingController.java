package theatre.pages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.showingSystem.ShowingSystem;
import theatre.tools.PageController;

import java.util.ArrayList;

public class CheckingController {
    private Movies movie;
    private ShowingSystem showingSystem;
    private ArrayList<String> positions;
    private Schedule schedule;

    @FXML GridPane reservingDetail;
    @FXML Label title, date, movieStatus, length;
    @FXML ImageView poster;
    @FXML Text description;

    @FXML public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                poster.setImage(new Image(movie.getLocationPoster()));
                title.setText(movie.getTitle());
                date.setText("Release Date: " + ((ShowingMovies)movie).getCome_inDate().toString());
                length.setText("Length: " + movie.getLength());
                PageController.stackWaringMessages.push(null);
                analyseData();
            }
        });
    }
    // analyse data, was passed from MovieDetailController class, and show on grid pane.
    public void analyseData() {
        int no_Theatre = ShowingSystemCollector.getSequenceTheatre(showingSystem) + 1;
        String[] dataList = {no_Theatre + "",
                showingSystem.getSystemType(), schedule.getStartTime(), "", "", "", "", "-"
        };
        ArrayList<Double> seatPrice = showingSystem.getSeatPrices();
        double normalPrice = 0, premiumPrice = 0, vipPrice = 0;
        for (String position : positions) {
            if (no_Theatre < 3) {
                dataList[3] += position + " ";
                normalPrice += (seatPrice.get(0) + showingSystem.getPrice());
                continue;
            }

            if (position.charAt(0) == 'A') {
                dataList[5] += position + " ";
                vipPrice += (seatPrice.get(0) + showingSystem.getPrice());
            }
            else if (position.charAt(0) < 'D') {
                dataList[4] += position + " ";
                premiumPrice += (seatPrice.get(1) + showingSystem.getPrice());
            }
            else {
                dataList[3] += position + " ";
                normalPrice += (seatPrice.get(2) + showingSystem.getPrice());
            }
        }
        dataList[6] = positions.size() > 1? positions.size() + " Seats" : "1 Seat";
        dataList[7] = (normalPrice + premiumPrice + vipPrice) + " THB";
        for (int i = 0; i < 8; i++) {
            if (dataList[i].equals("")) dataList[i] = "-";
            ((Text)reservingDetail.getChildren().get(i)).setText(dataList[i]);
        }
    }

    // set objects, were passed from MovieDetailController class *** //
    public void setMovie(Movies movie) {
        this.movie = movie;
    }

    public void setShowingSystem(ShowingSystem showingSystem) {
        this.showingSystem = showingSystem;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    // *** //
}
