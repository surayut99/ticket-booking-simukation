package theatre.scenes;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import theatre.movies.ComingSoonMovies;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.seat.SeatController;
import theatre.seat.Seat;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystem;
import theatre.showingSystem.ShowingSystemCollector;
import theatre.tools.*;
import theatre.tools.AccountData.Account;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MovieDetailController {
    private Movies selectedMovie;
    private ShowingSystem[] showingSystems;
    private Label preScheduleLabel, currentScheduleLabel;
    private ArrayList<AnchorPane> reservedSeatList, currentSelectedSeat;
    private AnchorPane warningMessage;
    private Schedule selectedSchedule;
    private int preTheatre, currentTheatre;


    @FXML
    Label title, date, movieStatus, length;
    @FXML
    ImageView poster;
    @FXML
    AnchorPane seatPart, detailField, mainAnchorPane;
    @FXML
    VBox mainShowDetail, detailSeat, showSeat;
    @FXML
    GridPane tableSchedule;
    @FXML
    Button continueButton;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                title.setText(selectedMovie.getTitle());
                length.setText("Length: " + selectedMovie.getLength());
                poster.setImage(new Image(new File(selectedMovie.getPosterLocation()).toURI().toString()));

                if (selectedMovie.getClass() == ShowingMovies.class) {
                    ShowingMovies movie = (ShowingMovies) selectedMovie;
                    date.setText("Release Date: " + movie.getCome_inDate().toString());
                    movieStatus.setText("(Showing)");
                } else {
                    mainShowDetail.getChildren().remove(1, 4);
                    ComingSoonMovies movies = (ComingSoonMovies) selectedMovie;
                    Label soonLabel = NodeCreator.createLabel("Showtime will be coming soon", 32, "#FFFFFF");
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setInput(new Glow());
                    soonLabel.setEffect(dropShadow);
                    NodeCreator.setAlignmentNodeOnAnchorPane(soonLabel, 123., 180., null, null);
                    AnchorPane parent = (AnchorPane) tableSchedule.getParent();
                    parent.getChildren().add(soonLabel);
                    date.setText("Release Date: " + movies.getComingSoonDate().toString());
                    movieStatus.setText("(Coming-soon)");
                    detailSeat.getChildren().clear();
                    seatPart.getChildren().clear();
                }
                preTheatre = -1;
                currentTheatre = -1;
                reservedSeatList = new ArrayList<>();
                currentSelectedSeat = new ArrayList<>();
                showingSystems = ShowingSystemCollector.getShowingSystems();
                String topic = "You are going back to previous page.";
                String description = "You have selected some seat on this page, If you go back your data you did will lose";
                warningMessage = NodeCreator.createWarningAnchorPane(MovieDetailController.this::ActionOnWarningOption, topic, description);
                PageController.getStackWaringMessages().push(null);
                showSchedule();
            }
        });
    }

    @FXML
    private void mouseClickOnScheduleLabel(MouseEvent event) {
        currentScheduleLabel = (Label) event.getSource();
        currentScheduleLabel.setDisable(true);
        if (!currentSelectedSeat.isEmpty()) {
            String topic = "You have just selected seat in this showtime.";
            String description = "If you change showtime or the theatre, the seat you selected will be unchecked.";
            warningMessage = NodeCreator.createWarningAnchorPane(this::ActionOnWarningOption, topic, description);
            WarningController.showWarning(warningMessage);
        } else {
            initialShowSeat();
        }
    }

    @FXML
    private void mouseClickOnSeat(MouseEvent event) {
        AnchorPane selectedSeat = (AnchorPane) event.getSource();
        if (selectedSeat.getChildren().size() == 2) {
            if (currentSelectedSeat.size() == 10) {
                Label warningSelection = NodeCreator.createLabel("Selection is limited at 10 seats per time", 24, "#FFFFFF");
                warningSelection.setStyle("-fx-background-color: rgba(0,0,0,0.8);\n" +
                        "-fx-background-radius: 10;");
                warningSelection.setOpacity(0);
                NodeCreator.setAlignmentNodeOnAnchorPane(warningSelection, null, 284., 264., 100.);
                AnchorPane firstPane = (AnchorPane) PageController.getStackPages().firstElement();
                firstPane.getChildren().add(warningSelection);

                Timeline fadeIn = EffectController.createTranslateTimeLine(warningSelection.opacityProperty(), 1, 0.5);
                fadeIn.play();
                fadeIn.setOnFinished(e -> {
                    Timeline fadeOut = EffectController.createTranslateTimeLine(warningSelection.opacityProperty(), 0, 0.5);
                    fadeOut.setDelay(Duration.seconds(2));
                    fadeOut.setOnFinished(k -> firstPane.getChildren().remove(warningSelection));
                    fadeOut.play();
                });
                return;
            }

            ImageView checkImg = NodeCreator.createImageView(30, 30, "/picture/check.png", true);
            AnchorPane.setLeftAnchor(checkImg, 17.5);
            AnchorPane.setBottomAnchor(checkImg, 30.);
            selectedSeat.getChildren().add(checkImg);
            currentSelectedSeat.add(selectedSeat);
        } else {
            selectedSeat.getChildren().remove(2);
            currentSelectedSeat.remove(selectedSeat);
        }
        checkAvailableContinueButton();
    }

    @FXML
    private void mouseClickOnMovieDetail(MouseEvent event) {
        Label label = (Label) event.getSource();
        if (label.getText().equals("See more detail")) {
            for (Node node : detailField.getChildren()) EffectController.createFadeTransition(node, 0.25, 0).play();

            Label moreDetail = NodeCreator.createLabel(selectedMovie.getDescription(), 16, "#ffffff");
            NodeCreator.setAlignmentNodeOnAnchorPane(moreDetail, 0., 0., 0., 0.);
            moreDetail.setPadding(new Insets(10, 10, 10, 10));
            moreDetail.setAlignment(Pos.TOP_LEFT);
            moreDetail.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
            moreDetail.setWrapText(true);
            detailField.getChildren().add(moreDetail);
            label.setText("Hide detail");
        } else {
            int index = selectedMovie instanceof ShowingMovies? 3: 4;
            FadeTransition fadeTransition = EffectController.createFadeTransition(detailField.getChildren().get(index), 0.5, 0);
            fadeTransition.play();

            detailField.getChildren().remove(index);
            label.setText("See more detail");
            for (Node node : detailField.getChildren()) EffectController.createFadeTransition(node, 0.5, 1).play();
        }
    }

    @FXML
    private void mouseEnterOnMoviePoster(MouseEvent event) {
        AnchorPane groupPoster = (AnchorPane) event.getSource();
        ImageView poster = (ImageView) groupPoster.getChildren().get(0);
        ImageView playIcon = (ImageView) groupPoster.getChildren().get(1);
        playIcon.setImage(new Image(new File("data/movieData/player_tools_icon/play_icon.png").toURI().toString()));

        ColorAdjust colorAdjustImg = (ColorAdjust) poster.getEffect();
        EffectController.createTranslateTimeLine(colorAdjustImg.brightnessProperty(), -0.5, 0.25).play();
        EffectController.createTranslateTimeLine(playIcon.opacityProperty(), 1, 0.25).play();
    }

    @FXML
    private void mouseExitOnMoviePoster(MouseEvent event) {
        AnchorPane groupPoster = (AnchorPane) event.getSource();
        ImageView poster = (ImageView) groupPoster.getChildren().get(0);
        ImageView playIcon = (ImageView) groupPoster.getChildren().get(1);
        ColorAdjust colorAdjustImg = (ColorAdjust) poster.getEffect();
        EffectController.createTranslateTimeLine(colorAdjustImg.brightnessProperty(), 0, 0.25).play();
        EffectController.createTranslateTimeLine(playIcon.opacityProperty(), 0, 0.25).play();
    }

    @FXML
    private void mouseClickToShowTrailer(MouseEvent event) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/theatre/scenes/trailerScene.fxml"));
        try {
            Parent root = loader.load();
            stage.setTitle(selectedMovie.getTitle() + " - Trailer");
            stage.setScene(new Scene(root));
            TrailerController controller = loader.getController();
            controller.setPath(selectedMovie.getVdoPath());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionOnWarningOption(ActionEvent event) {
        Button button = (Button) event.getSource();
        WarningController.hideWarning();
        mainShowDetail.setDisable(false);
        if (button.getText().equals("Yes")) {
            initialShowSeat();
            checkAvailableContinueButton();
        } else currentScheduleLabel.setDisable(false);
    }

    @FXML
    private void ActionOnContinueButton(ActionEvent event) {
        EffectController.createFadeTransition(PageController.peakPage(), 0.5, 0).play();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkingScene.fxml"));
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> positions = new ArrayList<>();
        for (AnchorPane anchorPane : currentSelectedSeat)
            positions.add(((Label) anchorPane.getChildren().get(1)).getText());

        Collections.sort(positions);
        CheckingController checkingController = loader.getController();
        checkingController.setMovie(selectedMovie);
        checkingController.setPositions(positions);
        checkingController.setShowingSystem(showingSystems[currentTheatre]);
        checkingController.setSchedule(selectedSchedule);

        root.setOpacity(0);
        root.prefWidthProperty().bind(PageController.getMainShowContent().prefWidthProperty());
        PageController.getMainShowContent().setVvalue(0);
        PageController.getMainShowContent().setContent(root);
        PageController.getStackPages().push(root);
        EffectController.createFadeTransition(root, 0.5, 1).play();
    }

    //initialize show seat, check if the showtime is from the same theatre.
    private void initialShowSeat() {
        if (preScheduleLabel != null) {
            preScheduleLabel.setDisable(false);
        }
        preScheduleLabel = currentScheduleLabel;
        currentTheatre = GridPane.getRowIndex(currentScheduleLabel) == null ?
                0 : GridPane.getRowIndex(currentScheduleLabel);

        selectedSchedule = null;
        for (Schedule s : showingSystems[currentTheatre].getSchedules()) {
            if (s.getStartTime().equals(currentScheduleLabel.getText())) {
                selectedSchedule = s;
                break;
            }
        }
        resetSeatStatus();

        if (preTheatre == currentTheatre) {
            showSeatStatus(selectedSchedule);
            showSeatDetail(); // show total seat and available seat in showtime you select.
            return;
        }

        detailSeat.getChildren().clear();
        Label showingSystemType = NodeCreator.createLabel(
                "Showing System: " + showingSystems[currentTheatre].getSystemType(), 24, "#ffffff");
        detailSeat.getChildren().add(showingSystemType);
        detailSeat.getChildren().add(new HBox(50));
        ((HBox) detailSeat.getChildren().get(1)).setAlignment(Pos.CENTER);
        showSeat();
        showSeatStatus(selectedSchedule);
        preTheatre = currentTheatre;
    }

    //show system detail, seat price, total seat and number of available seat in showtime user selected.
    private void showSeatDetail() {
        Label statSeat = NodeCreator.createLabel(
                "Total: " + selectedSchedule.getTotalSeat() + " Available: " + selectedSchedule.getNumAvailableSeat(),
                18, "#ffffff");
        statSeat.setAlignment(Pos.CENTER_LEFT);

        if (preTheatre == currentTheatre) {
            showSeat.getChildren().set(showSeat.getChildren().size() - 1, statSeat);
            return;
        }

        showSeat.getChildren().add(statSeat);
        ArrayList<Seat> seats = showingSystems[currentTheatre].getSampleSeats();
        HBox seatDetail = (HBox) detailSeat.getChildren().get(1);

        for (Seat seat : seats) {
            double totalPrice = seat.getPrice() + showingSystems[currentTheatre].getPrice();
            String detail = seat.getSeatType() + ": " + totalPrice + "0 THB";
            ImageView imgSeat = NodeCreator.createImageView(50, 50, seat.getSeatImgPath(), true);
            Label price = NodeCreator.createLabel(detail, 18, "#ffffff");
            HBox boxDetail = new HBox(10);
            boxDetail.getChildren().addAll(imgSeat, price);
            boxDetail.setAlignment(Pos.CENTER_LEFT);
            boxDetail.setEffect(new DropShadow());
            seatDetail.getChildren().add(boxDetail);
        }
    }

    //Show seat image and position.
    private void showSeat() {
        showSeat.getChildren().setAll(SeatController.generateSeat(currentTheatre, 60, false, this::mouseClickOnSeat));
        showSeatDetail();
    }

    //show the showtime of the movies.
    private void showSchedule() {
        for (int i = 0; i < showingSystems.length; i++) {
            ArrayList<Schedule> schedules = showingSystems[i].getSchedules();
            int column = 0;
            for (Schedule schedule : schedules) {
                if (schedule.getMovies() == selectedMovie) {
                    Label scheduleLabel = NodeCreator.createTimeLabel(schedule.getStartTime());
                    scheduleLabel.setOnMouseClicked(this::mouseClickOnScheduleLabel);
                    tableSchedule.add(scheduleLabel, column++, i);
                }
            }
        }
    }

    //show available seat when change showtime.
    private void showSeatStatus(Schedule schedule) {
        ArrayList<String> positionList = new ArrayList<>();
        if (AccountCollector.getCurrentAccount() != null) {
            Account currentAccount = AccountCollector.getCurrentAccount();
            positionList = currentAccount.getUserSeatPositions(currentTheatre, selectedMovie, schedule);

        }

        for (String position : schedule.getReservedPositionSeat()) {
            int row = position.charAt(0) - 65;
            int column = Integer.parseInt(position.substring(1));
            AnchorPane reservedPosition = SeatController.findPosition(showSeat, row, column - 1, currentTheatre);
            reservedPosition.setDisable(true);
            reservedPosition.setEffect(new ColorAdjust(0, 0, -0.5, 0));
            if (positionList.contains(position)) {
                ImageView checkImg = NodeCreator.createImageView(30, 30, "/picture/check.png", true);
                checkImg.setEffect(new ColorAdjust(0,1,0.5,0));
                AnchorPane.setLeftAnchor(checkImg, 17.5);
                AnchorPane.setBottomAnchor(checkImg, 30.);
                reservedPosition.getChildren().add(checkImg);
            }
            reservedSeatList.add(reservedPosition);
        }
    }

    //reset unavailable seat before call showSeatStatus().
    private void resetSeatStatus() {
        for (AnchorPane anchorPane : reservedSeatList) {
            if (anchorPane.getChildren().size() == 3)
                anchorPane.getChildren().remove(2);
            anchorPane.setDisable(false);
            anchorPane.setEffect(null);
        }

        for (AnchorPane anchorPane : currentSelectedSeat) anchorPane.getChildren().remove(2);

        currentSelectedSeat.clear();
        reservedSeatList.clear();
    }

    //check if user have selected more seat and the continue button will be available.
    private void checkAvailableContinueButton() {
        if (!currentSelectedSeat.isEmpty()) {
            continueButton.setDisable(false);
            EffectController.createFadeTransition(continueButton, 0.25, 1).play();
            PageController.getStackWaringMessages().pop();
            PageController.getStackWaringMessages().push(warningMessage);
            return;
        }
        continueButton.setDisable(true);
        EffectController.createFadeTransition(continueButton, 0.25, 0).play();
        PageController.getStackWaringMessages().pop();
        PageController.getStackWaringMessages().push(null);
    }

    //*** set objects, were passed from previous page ***//
    public void setSelectedMovie(Movies selectedMovie) {
        this.selectedMovie = selectedMovie;
    }
    //*** set objects, were passed from previous page ***//
}