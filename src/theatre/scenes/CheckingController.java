package theatre.scenes;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.seat.Seat;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.showingSystem.ShowingSystem;
import theatre.tools.AccountCollector;
import theatre.tools.EffectController;
import theatre.tools.PageController;

import java.io.IOException;
import java.util.ArrayList;

public class CheckingController {
    private Movies movie;
    private ShowingSystem showingSystem;
    private ArrayList<String> positions;
    private Schedule schedule;
    private double totalCost;
    private int no_Theatre;

    @FXML
    GridPane reservingDetail;
    @FXML
    Label title, date, movieStatus, length;
    @FXML
    ImageView poster;
    @FXML
    Text description, warningMessage;
    @FXML
    Button continueBtn;
    @FXML
    AnchorPane reservingData, movieData;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AnchorPane thisPage = (AnchorPane) PageController.getStackPages().peek();
                ScrollPane parent = PageController.getMainShowContent();
                thisPage.prefWidthProperty().bind(parent.widthProperty());
                thisPage.prefHeightProperty().bind(parent.heightProperty());

                poster.setImage(new Image(movie.getPosterLocation()));
                title.setText(movie.getTitle());
                date.setText("Release Date: " + ((ShowingMovies) movie).getCome_inDate().toString());
                length.setText("Length: " + movie.getLength());
                description.setText(movie.getDescription());
                PageController.getStackWaringMessages().push(null);
                analyseData(AccountCollector.getCurrentAccount() == null);
            }
        });
    }

    //analyse data, was passed from MovieDetailController class, and show on grid pane.
    private void analyseData(boolean isAbility) {
        no_Theatre = ShowingSystemCollector.getSequenceTheatre(showingSystem) + 1;
        String[] dataList = {no_Theatre + "",
                showingSystem.getSystemType(), schedule.getStartTime(), "", "", "", "", "-"
        };
        ArrayList<Seat> sampleSeats = showingSystem.getSampleSeats();
        double normalPrice = 0, premiumPrice = 0, vipPrice = 0;
        for (String position : positions) {
            if (no_Theatre < 3) {
                dataList[3] += position + " ";
                normalPrice += (sampleSeats.get(0).getPrice() + showingSystem.getPrice());
                continue;
            }

            if (position.charAt(0) == 'A') {
                dataList[5] += position + " ";
                vipPrice += (sampleSeats.get(0).getPrice() + showingSystem.getPrice());
            } else if (position.charAt(0) < 'D') {
                dataList[4] += position + " ";
                premiumPrice += (sampleSeats.get(1).getPrice() + showingSystem.getPrice());
            } else {
                dataList[3] += position + " ";
                normalPrice += (sampleSeats.get(2).getPrice() + showingSystem.getPrice());
            }
        }
        dataList[6] = positions.size() > 1 ? positions.size() + " Seats" : "1 Seat";
        dataList[7] = (normalPrice + premiumPrice + vipPrice) + "0 THB";
        for (int i = 0; i < 8; i++) {
            if (dataList[i].equals("")) dataList[i] = "-";
            ((Text) reservingDetail.getChildren().get(i)).setText(dataList[i]);
        }
        totalCost = normalPrice + premiumPrice + vipPrice;
        warningMessage.setVisible(isAbility);
        continueBtn.setVisible(!isAbility);
        continueBtn.setDisable(isAbility);
    }

    public void mouseActionContinue(ActionEvent event) {
        EffectController.createFadeTransition(PageController.peakPage(), 0.5, 0).play();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transactionScene.fxml"));
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TransactionController transactionController = loader.getController();
        transactionController.setNo_Theatre(no_Theatre);
        transactionController.setPositions(positions);
        transactionController.setSchedule(schedule);
        transactionController.setTotalCost(totalCost);
        transactionController.setMovie(movie);

        root.setOpacity(0);
        root.prefWidthProperty().bind(PageController.getMainShowContent().prefWidthProperty());
        PageController.getMainShowContent().setVvalue(0);
        PageController.getMainShowContent().setContent(root);
        PageController.getStackPages().push(root);
        PageController.getStackWaringMessages().push(null);
        EffectController.createFadeTransition(root, 0.5, 1).play();
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
