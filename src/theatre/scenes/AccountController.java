package theatre.scenes;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import theatre.seat.SeatController;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.AccountData.Account;
import theatre.tools.AccountCollector;
import theatre.tools.AccountData.SelectedMovies;
import theatre.tools.AccountData.SelectedTheatre;
import theatre.tools.EffectController;
import theatre.tools.PageController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountController {
    private Account currentAccount;
    private ArrayList<SelectedTheatre> selectedTheatres;
    private Button preTheatre, editBtn, saveBtn, closeBtn;
    private int theatre;
    private VBox seatBox, contents;
    private List<Schedule> currentSchedule;
    private ArrayList<SelectedMovies> moviesList;

    @FXML
    HBox showTheatre;
    @FXML
    Text username, firstName, lastName, e_mail, balance;
    @FXML
    VBox reserving;
    @FXML
    AnchorPane mainShowContent;
    @FXML
    ScrollPane scrollPane;

    @FXML
    public void initialize() {
        contents = new VBox(5);
        seatBox = new VBox(5);
        editBtn = new Button("EDIT");
        saveBtn = new Button("SAVE");
        closeBtn = new Button("CLOSE");

        seatBox.setAlignment(Pos.CENTER);
        contents.setAlignment(Pos.CENTER);

        contents.getChildren().add(seatBox);

        mainShowContent.prefWidthProperty().bind(scrollPane.widthProperty());
//        mainShowContent.prefHeightProperty().bind(scrollPane.heightProperty());

        currentAccount = AccountCollector.getCurrentAccount();
        username.setText(currentAccount.getUsername());
        firstName.setText(currentAccount.getFirstName());
        lastName.setText(currentAccount.getLastName());
        e_mail.setText(currentAccount.getMail());
        balance.setText(currentAccount.getBalance() + "0 THB");
//        printAccountDetail();
    }

    @FXML
    public void backToMainScene(ActionEvent event) {
        AnchorPane parent = (AnchorPane) PageController.getStackPages().firstElement();
        ScrollPane thisPage = (ScrollPane) parent.getChildren().get(parent.getChildren().size() - 1);
        Timeline timeline = EffectController.createTranslateTimeLine(thisPage.translateYProperty(), thisPage.getHeight(), 0.25);
        timeline.setOnFinished(e -> parent.getChildren().remove(thisPage));
        timeline.play();
    }

    @FXML
    public void mouseClickOnShowReservingDetail(MouseEvent event) {
        Text choice = (Text) event.getSource();
        if (choice.getText().contains("SHOW")) {
            choice.setText("HIDE RESERVING DETAIL");
            selectedTheatres = currentAccount.getSelectedTheatres();
            for (SelectedTheatre t : selectedTheatres) {
                Button theatreBtn = new Button("THEATRE " + (t.getNo_theatre() + 1));
                theatreBtn.setOnAction(this::actionOnTheatre);
                showTheatre.getChildren().add(theatreBtn);
            }
        }
        else {
            choice.setText("SHOW RESERVING DETAIL");
            showTheatre.getChildren().clear();
            reserving.getChildren().clear();
            preTheatre = null;
        }
    }

    @FXML
    private void actionOnTheatre(ActionEvent event) {
        Button button = (Button) event.getSource();
        if (preTheatre != null) preTheatre.setDisable(false);
        button.setDisable(true);
        preTheatre = button;
        reserving.getChildren().clear();
        theatre = Integer.parseInt(Character.toString(button.getText().charAt(8)));

        SelectedTheatre selectedTheatre = null;
        for (SelectedTheatre t : selectedTheatres) {
            String no_theatre = Integer.toString(t.getNo_theatre() + 1);
            if (button.getText().equals("THEATRE " + no_theatre)) {
                selectedTheatre = t;
                break;
            }
        }
        if (selectedTheatre != null) {
            moviesList = selectedTheatre.getSelectedMoviesList();
        }
        if (moviesList != null) {
            for (SelectedMovies m : moviesList) {
                HBox hBox = new HBox(20);
                ImageView imageView = new ImageView(m.getMovies().getPosterLocation());
                imageView.setFitHeight(300);
                imageView.setFitWidth(200);
                imageView.setEffect(new DropShadow());
                imageView.setPreserveRatio(false);
                hBox.getChildren().add(imageView);
                currentSchedule = m.getSchedules();

                VBox schedules = new VBox(10);
                schedules.getChildren().addAll(m.getBtnList());
                for (Button btn : m.getBtnList()) btn.setOnAction(this::actionOnSchedule);

                hBox.getChildren().add(schedules);
                reserving.getChildren().add(hBox);
            }
        }
    }

    @FXML
    private void actionOnSchedule(ActionEvent event) {
        String startTime = ((Button) event.getSource()).getText();
        List<VBox> seatList = SeatController.generateSeat(theatre - 1, 60, true, null);
        Schedule schedule = ShowingSystemCollector.getShowingSystems()[theatre - 1].getScheduleByStartTime(startTime);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("personalBooking.fxml"));
            AnchorPane mainPane = loader.load();
            mainPane.prefWidthProperty().bind(mainShowContent.widthProperty());
            mainPane.prefHeightProperty().bind(mainShowContent.heightProperty());
            PersonalBookingController controller = loader.getController();
            controller.setSeatBox(seatList);
            controller.setSelectedSchedule(schedule);
            controller.setNo_theatre(theatre - 1);
            mainShowContent.getChildren().add(mainPane);
        } catch (IOException e) {e.printStackTrace();}
    }
}
