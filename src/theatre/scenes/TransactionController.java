package theatre.scenes;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.*;
import theatre.tools.AccountData.Account;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;

public class TransactionController {
    private ShowingMovies movie;
    private int no_Theatre;
    private double totalCost;
    private ArrayList<String> positions;
    private Schedule schedule;
    private Account currentAccount;

    @FXML
    GridPane gridDetail;
    @FXML
    VBox movieDetail, accountDetail;
    @FXML
    Text warningMessage;
    @FXML
    HBox detailPart;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentAccount = AccountCollector.getCurrentAccount();
                ((Label) movieDetail.getChildren().get(0)).setText(movie.getTitle());
                ((Label) movieDetail.getChildren().get(1)).setText("Release Date: " + movie.getCome_inDate().toString());
                ((Label) movieDetail.getChildren().get(2)).setText("Length: " + movie.getLength());
                String[] data = {Integer.toString(no_Theatre), ShowingSystemCollector.getShowingSystems()[no_Theatre - 1].getSystemType(),
                        schedule.getStartTime(), String.join(" ", positions), totalCost + "0 THB"};
                String[] accountData = {
                        currentAccount.getUsername(),
                        currentAccount.getFirstName(),
                        currentAccount.getLastName(),
                        currentAccount.getBalance() + "0 THB"
                };

                for (int i = 0; i < 5; i++) {
                    Text text = new Text(data[i]);
                    text.setFont(Font.font("System", FontWeight.BOLD, 20));
                    text.setFill(Color.WHITE);
                    gridDetail.add(text, 1, i);
                }

                for (int i = 0; i < 4; i++) ((Text) accountDetail.getChildren().get(i)).setText(accountData[i]);

                ScrollPane parent = PageController.getMainShowContent();
                AnchorPane thisPage = (AnchorPane) PageController.getStackPages().peek();
                thisPage.prefWidthProperty().bind(parent.widthProperty());
                thisPage.prefHeightProperty().bind(parent.heightProperty());
            }
        });
    }

    @FXML
    public void actionOnSubmit(ActionEvent event) {
        String topic = "You are going to submit.";
        String description = "";
        AnchorPane warning = NodeCreator.createWarningAnchorPane(this::actionOnChoiceSubmit, topic, description);
        WarningController.showWarning(warning);
    }

    @FXML
    private void actionOnChoiceSubmit(ActionEvent event) {
        Button button = (Button) event.getSource();
        WarningController.hideWarning();
        if (button.getText().equals("Yes")) {
            ArrayList<String> oldPositionList = currentAccount.getUserSeatPositions(no_Theatre - 1, movie, schedule);


            try {
//                currentAccount.purchase(totalCost);
                String[] arrPosition = positions.toArray(new String[positions.size()]);
                schedule.addReservedSeat(arrPosition);
                String oldData = oldPositionList == null ? "" : String.join(" ", oldPositionList);
                currentAccount.addBooking(no_Theatre - 1, movie.getTitle(), schedule, arrPosition);
                String newData = String.join(" ", currentAccount.getUserSeatPositions(no_Theatre - 1, movie, schedule));
                DataController.editReservingData(Integer.toString(no_Theatre), oldData, newData, schedule.getStartTime(), schedule.getMovies().getTitle());
                PageController.goHomepage();
                PageController.getMainShowContent().setContent(PageController.peakPage());
                PageController.getLastHomePage().setStyle(
                        "-fx-background-color: #1F618D;\n" +
                        "-fx-background-radius: 50;"
                );
                EffectController.createFadeTransition(PageController.peakPage(), 0.5, 1).play();
            } catch (IllegalArgumentException e) {
                warningMessage.setText(e.getMessage());
            }
        }
    }

    public void setNo_Theatre(int no_Theatre) {
        this.no_Theatre = no_Theatre;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setMovie(Movies movie) {
        this.movie = (ShowingMovies) movie;
    }
}
