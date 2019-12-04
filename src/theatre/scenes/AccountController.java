package theatre.scenes;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import theatre.seat.SeatController;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.AccountData.Account;
import theatre.tools.AccountCollector;
import theatre.tools.AccountData.SelectedMovies;
import theatre.tools.AccountData.SelectedTheatre;
import theatre.tools.ApplicationDisplay;
import theatre.tools.EffectController;
import theatre.tools.PageController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountController {
    private Account currentAccount;
    private ArrayList<SelectedTheatre> selectedTheatres;
    private Button preTheatre;
    private int theatre;
    private ArrayList<SelectedMovies> moviesList;
    private ApplicationDisplay applicationDisplay;

    @FXML
    HBox showTheatre;
    @FXML
    Text username, firstName, lastName, e_mail, balance, showReserving;
    @FXML
    VBox reserving;
    @FXML
    AnchorPane mainShowContent;
    @FXML
    ScrollPane scrollPane;

    @FXML
    public void initialize() {
        VBox contents = new VBox(5);
        VBox seatBox = new VBox(5);

        seatBox.setAlignment(Pos.CENTER);
        contents.setAlignment(Pos.CENTER);

        contents.getChildren().add(seatBox);

        mainShowContent.prefWidthProperty().bind(scrollPane.widthProperty());

        currentAccount = AccountCollector.getCurrentAccount();
        username.setText(currentAccount.getUsername());
        firstName.setText(currentAccount.getFirstName());
        lastName.setText(currentAccount.getLastName());
        e_mail.setText(currentAccount.getMail());
        balance.setText(currentAccount.getBalance() + "0 THB");

        applicationDisplay = new ApplicationDisplay(balance);
        currentAccount.addObserver(applicationDisplay);
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
        if (showReserving.getText().contains("SHOW")) {
            showReserving.setText("HIDE RESERVING DETAIL");
            selectedTheatres = currentAccount.getSelectedTheatres();
            for (SelectedTheatre t : selectedTheatres) {
                Button theatreBtn = new Button("THEATRE " + (t.getNo_theatre() + 1));
                theatreBtn.setFont(Font.font("System", FontWeight.BOLD, 18));
                theatreBtn.setTextFill(Color.WHITE);
                theatreBtn.setStyle("-fx-background-color: linear-gradient(#4b6cb7, #182848);" +
                        "-fx-background-radius: 25;");
                theatreBtn.setCursor(Cursor.HAND);
                theatreBtn.setEffect(new DropShadow());
                theatreBtn.setOnAction(this::actionOnTheatre);
                showTheatre.getChildren().add(theatreBtn);
            }
        }
        else {
            showReserving.setText("SHOW RESERVING DETAIL");
            showTheatre.getChildren().clear();
            reserving.getChildren().clear();
            preTheatre = null;
        }
    }

    @FXML
    private void actionOnTheatre(ActionEvent event) {
        Button button = (Button) event.getSource();
        theatre = Integer.parseInt(Character.toString(button.getText().charAt(8)));
        if (preTheatre != null) preTheatre.setDisable(false);
        button.setDisable(true);
        preTheatre = button;
        FadeTransition fade = EffectController.createFadeTransition(reserving, 0.125, 0);
        fade.setOnFinished(e -> {
            reserving.getChildren().clear();
            generateSimpleMockUp(button);
        });
        fade.play();

    }

    private void generateSimpleMockUp(Button button) {
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
            int num = 0;
            HBox rowBox = null;
            for (SelectedMovies m : moviesList) {
                if (num == 0) {
                    rowBox = new HBox(20);
                    rowBox.setAlignment(Pos.CENTER);
                    reserving.getChildren().add(rowBox);
                }

                HBox hBox = new HBox(10);
                ImageView imageView = new ImageView(new Image(new File(m.getMovies().getPosterLocation()).toURI().toString()));
                imageView.setFitHeight(300);
                imageView.setFitWidth(200);
                imageView.setEffect(new DropShadow());
                imageView.setPreserveRatio(false);
                hBox.getChildren().add(imageView);
                hBox.setAlignment(Pos.CENTER);

                VBox schedules = new VBox(10);
                schedules.getChildren().addAll(m.getBtnList());
                for (Button btn : m.getBtnList()) btn.setOnAction(this::actionOnSchedule);

                hBox.getChildren().add(schedules);

                rowBox.getChildren().add(hBox);
                if (++num == 4) num = 0;
            }
        }

        EffectController.createFadeTransition(reserving, 0.125, 1).play();
    }

    @FXML
    private void actionOnSchedule(ActionEvent event) {
        String startTime = ((Button) event.getSource()).getText();
        List<VBox> seatList = SeatController.generateSeat(theatre - 1, 60, true, null);
        Schedule schedule = ShowingSystemCollector.getShowingSystems()[theatre - 1].getScheduleByStartTime(startTime);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("personalBooking.fxml"));
            AnchorPane mainPane = loader.load();
            mainPane.prefWidthProperty().bind(scrollPane.widthProperty());
            scrollPane.setContent(mainPane);
            PersonalBookingController controller = loader.getController();
            controller.setSeatBox(seatList);
            controller.setSelectedSchedule(schedule);
            controller.setNo_theatre(theatre - 1);
            controller.setTempAnchorPane(mainShowContent);
            controller.setScrollPane(scrollPane);
            controller.setRefreshEvent(this::mouseClickOnShowReservingDetail);
        } catch (IOException e) {e.printStackTrace();}
    }

    @FXML
    private void actionOnTopUp(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("top_upScene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Top-Up");
            stage.setAlwaysOnTop(true);
            stage.show();

            scrollPane.setEffect(new ColorAdjust());
            scrollPane.getContent().setDisable(true);
            ColorAdjust colorAdjust = (ColorAdjust) scrollPane.getEffect();
            EffectController.createTranslateTimeLine(colorAdjust.brightnessProperty(), -0.5, 0.5).play();

            stage.setOnCloseRequest(e -> {
                EffectController.createTranslateTimeLine(colorAdjust.brightnessProperty(), 0, 1).play();
                scrollPane.getContent().setDisable(false);
                scrollPane.setEffect(null);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
