package theatre.pages;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import theatre.movies.ComingSoonMovies;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.seat.RowSeatFinder;
import theatre.seat.SeatManager;
import theatre.seat.SeatPatternGenerator;
import theatre.seat.Seat;
import theatre.showingSystem.Schedule;
import theatre.showingSystem.ShowingSystem;
import theatre.tools.EffectController;
import theatre.tools.NodeCreator;
import theatre.tools.PageController;

import java.io.IOException;
import java.util.*;

public class MovieDetailController {
    private Movies selectedMovie;
    private ShowingSystem[] showingSystems;
    private Label preScheduleLabel, currentScheduleLabel;
    private ArrayList<AnchorPane> reservedSeatList;
    private ArrayList<AnchorPane> currentSelectedSeat;
    private int preTheatre, currentTheatre, currentSchedule;
    private AnchorPane warningMessage;

    @FXML Label title, date, movieStatus, length;
    @FXML ImageView poster;
    @FXML AnchorPane seatPart, detailField, mainAnchorPane;
    @FXML VBox mainShowDetail, detailSeat ,showSeat;
    @FXML GridPane tableSchedule;
    @FXML Button continueButton;

    @FXML public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                title.setText(selectedMovie.getTitle());
                length.setText("Length: " + selectedMovie.getLength());
                poster.setImage(new Image(selectedMovie.getLocationPoster()));

                if (selectedMovie.getClass() == ShowingMovies.class) {
                    ShowingMovies movie = (ShowingMovies) selectedMovie;
                    date.setText("Release Date: " + movie.getCome_inDate().toString());
                    movieStatus.setText("(Showing)");
                }

                else {
                    ComingSoonMovies movies = (ComingSoonMovies) selectedMovie;
                    date.setText("Release Date: " + movies.getComingSoonDate().toString());
                    movieStatus.setText("(Coming-soon)");
                    detailSeat.getChildren().clear();
                    seatPart.getChildren().clear();
                }
                preTheatre = -1;
                currentTheatre = -1;
                currentSchedule = -1;
                preScheduleLabel = null;
                reservedSeatList = new ArrayList<>();
                currentSelectedSeat = new ArrayList<>();
                warningMessage = PageController.createWarningMessage();
                ((Button) warningMessage.getChildren().get(3)).setOnAction(MovieDetailController.this::ActionOnWarningOption);
                ((Button) warningMessage.getChildren().get(4)).setOnAction(MovieDetailController.this::ActionOnWarningOption);
                PageController.stackWaringMessages.push(null);
                showSchedule();
            }
        });
    }

    @FXML private void mouseClickOnScheduleLabel(MouseEvent event) {
        currentScheduleLabel = (Label) event.getSource();
        currentScheduleLabel.setDisable(true);
        if (!currentSelectedSeat.isEmpty()) {
            String topic = "You have just selected seat in this showtime.";
            String description = "If you change showtime or the theatre, the seat you selected will be unchecked.";
            AnchorPane warning = NodeCreator.createWarningAnchorPane(topic, description);
            ((Button) warning.getChildren().get(3)).setOnAction(this::ActionOnWarningOption);
            ((Button) warning.getChildren().get(4)).setOnAction(this::ActionOnWarningOption);
            mainShowDetail.setDisable(true);
            ((AnchorPane) PageController.stackPages.firstElement()).getChildren().add(warning);
        }
        else {initialShowSeat();}
    }

    @FXML private void mouseClickOnSeat(MouseEvent event) {
        AnchorPane selectedSeat = (AnchorPane) event.getSource();
        switch (selectedSeat.getChildren().size()) {
            case 2:
                ImageView checkImg = NodeCreator.createImageView(30,30, "/picture/check.png", true);
                AnchorPane.setLeftAnchor(checkImg, 15.0);
                AnchorPane.setBottomAnchor(checkImg, 30.);
                selectedSeat.getChildren().add(checkImg);
                currentSelectedSeat.add(selectedSeat);
                break;

            case 3:
                selectedSeat.getChildren().remove(2);
                currentSelectedSeat.remove(selectedSeat);
                break;
        }

        checkAvailableContinueButton();
    }

    @FXML private void mouseClickOnMovieDetail(MouseEvent event) {
        Label label = (Label) event.getSource();
        switch (label.getText()) {
            case "See more detail":
                for (Node node : detailField.getChildren()) EffectController.createFadeTransition(node, 0.25, 0).play();

                Label moreDetail = NodeCreator.createLabel("TestDetail", 16, "#ffffff");
                NodeCreator.setAlignmentNodeOnAnchorPane(moreDetail,0.,0.,0.,0.);
                moreDetail.setPadding(new Insets(10,10,10,10));
                moreDetail.setAlignment(Pos.TOP_LEFT);
                moreDetail.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
                detailField.getChildren().add(moreDetail);
                label.setText("Hide detail");
                break;

            case "Hide detail":
                EffectController.createFadeTransition(detailField.getChildren().get(3), 0.5, 0).play();
                detailField.getChildren().remove(3);
                for (Node node : detailField.getChildren()) EffectController.createFadeTransition(node, 0.5, 1).play();
                label.setText("See more detail");
                break;
        }
    }

    @FXML private void ActionOnWarningOption(ActionEvent event) {
        String option = ((Button) event.getSource()).getText();
        switch (option) {
            case "Yes":
                ((AnchorPane) PageController.stackPages.firstElement()).getChildren().remove(1);
                if (((AnchorPane) PageController.stackPages.firstElement()).getChildren().contains(warningMessage)) {
                    PageController.stackWaringMessages.pop();
                    PageController.stackWaringMessages.push(null);
                    PageController.back();
                    break;
                }
                PageController.stackWaringMessages.pop();
                PageController.stackWaringMessages.push(null);
                initialShowSeat();
                checkAvailableContinueButton();
                break;
            case "No":
                ((AnchorPane) PageController.stackPages.firstElement()).getChildren().remove(1);
                mainAnchorPane.getChildren().get(0).setEffect(null);
                currentScheduleLabel.setDisable(false);
                preScheduleLabel.setDisable(true);
                break;
        }
        mainShowDetail.setDisable(false);
    }

    @FXML private void ActionOnContinueButton(ActionEvent event) {
        EffectController.createFadeTransition(PageController.peakPage(), 0.5, 0).play();
        PageController.mainShowContent.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkingScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> positions = new ArrayList<>();
        for (AnchorPane anchorPane : currentSelectedSeat) positions.add(((Label) anchorPane.getChildren().get(1)).getText());

        Collections.sort(positions);
        CheckingController checkingController = loader.getController();
        checkingController.setMovie(selectedMovie);
        checkingController.setPositions(positions);
        checkingController.setShowingSystem(showingSystems[currentTheatre]);
        checkingController.setSchedule(showingSystems[currentTheatre].getSchedules().get(currentSchedule));

        root.setOpacity(0);
        PageController.mainShowContent.getChildren().add(root);
        PageController.stackPages.push(root);
        EffectController.createFadeTransition(root ,0.5, 1).play();
    }

    //initialize show seat, check if the showtime is from the same theatre.
    private void initialShowSeat() {
        currentSelectedSeat.clear();
        if (preScheduleLabel != null) {preScheduleLabel.setDisable(false);}
        preScheduleLabel = currentScheduleLabel;
        currentTheatre = GridPane.getRowIndex(currentScheduleLabel) == null ?
                0 : GridPane.getRowIndex(currentScheduleLabel);
        currentSchedule = GridPane.getColumnIndex(currentScheduleLabel) == null ?
                0 : GridPane.getColumnIndex(currentScheduleLabel);

        if (preTheatre == currentTheatre) {
            resetSeatStatus();
            showSeatStatus(showingSystems[currentTheatre].getSchedules().get(currentSchedule));
            showSeatDetail(""); // show total seat and available seat in showtime you select.
            return;
        }

        detailSeat.getChildren().clear();
        Label showingSystemType = NodeCreator.createLabel(
                "Showing System: " + showingSystems[currentTheatre].getSystemType(), 24, "#ffffff");
        detailSeat.getChildren().add(showingSystemType);
        detailSeat.getChildren().add(new HBox(50));
        ((HBox)detailSeat.getChildren().get(1)).setAlignment(Pos.CENTER_LEFT);
        showSeat();
        showSeatStatus(showingSystems[currentTheatre].getSchedules().get(currentSchedule));
        preTheatre = currentTheatre;
    }

    //show system detail, seat price, total seat and number of available seat in showtime user selected.
    private void showSeatDetail(String imgPath) {
        if (imgPath.equals("")) {
            if (showSeat.getChildren().get(showSeat.getChildren().size() - 1) instanceof Label) {showSeat.getChildren().remove(showSeat.getChildren().size() - 1);}
            Schedule schedules = showingSystems[currentTheatre].getSchedules().get(currentSchedule);
            int available = schedules.getNumAvailableSeat();
            Label label = NodeCreator.createLabel("Total: " + (schedules.getTotalSeat()) + " Available: " + available, 18, "#ffffff");
            label.setAlignment(Pos.CENTER_LEFT);
            showSeat.getChildren().add(label);
            return;
        }

        HBox seatDetail = (HBox) detailSeat.getChildren().get(1);
        ImageView imgSeat = NodeCreator.createImageView(50,50, imgPath, true);
        ArrayList<String> seatTypes = showingSystems[currentTheatre].getSeatTypes();
        ArrayList<Double> seatPrices = showingSystems[currentTheatre].getSeatPrices();
        int index = seatTypes.size() - 1 - seatDetail.getChildren().size();
        String detail = seatTypes.get(index) + ": " + (seatPrices.get(index) + showingSystems[currentTheatre].getPrice() + "0 THB");
        Label label = NodeCreator.createLabel(detail, 18, "#ffffff");
        HBox boxDetail = new HBox(10);

        boxDetail.getChildren().addAll(imgSeat, label);
        boxDetail.setAlignment(Pos.CENTER_LEFT);
        boxDetail.setEffect(new DropShadow());
        seatDetail.getChildren().addAll(boxDetail);
    }

    //Show seat image and position.
    private void showSeat() {
        showSeat.getChildren().clear();
        Seat[][] seats = showingSystems[currentTheatre].getSeats();
        String currentSeatType = "";
        VBox typeBox = null;
        double imgSize = 60;

        SeatPatternGenerator normalPattern = new SeatPatternGenerator() {
            @Override
            public HBox generateRowSeat(Seat[] seats, double imgSize) {
                HBox rowSeats = new HBox(5);
                rowSeats.setPrefHeight(imgSize);
                rowSeats.setAlignment(Pos.CENTER);
                for (int i = 0; i < seats.length; i++) {
                    AnchorPane groupImg = NodeCreator.createSeatIcon(imgSize, seats[i].getSeatPosition(), seats[i].getSeatImgPath());
                    if (seats[i].isReserve()) {
                        groupImg.setDisable(true);
                        groupImg.setEffect(new ColorAdjust(0,0,-0.5,0));
                    }
                    groupImg.setOnMouseClicked(MovieDetailController.this::mouseClickOnSeat);
                    rowSeats.getChildren().add(groupImg);
                }
                return rowSeats;
            }
        };

        SeatPatternGenerator couplePattern = new SeatPatternGenerator() {
            @Override
            public HBox generateRowSeat(Seat[] seats, double imgSize) {
                HBox rowSeats = new HBox(25);
                HBox coupleBox = new HBox(5);
                coupleBox.setPrefHeight(imgSize);
                coupleBox.setAlignment(Pos.CENTER);
                for (int i = 0; i < seats.length; i++) {
                    AnchorPane groupImg = NodeCreator.createSeatIcon(imgSize, seats[i].getSeatPosition(), seats[i].getSeatImgPath());
                    if (seats[i].isReserve()) {
                        groupImg.setDisable(true);
                        groupImg.setEffect(new ColorAdjust(0,0,-0.5,0));
                    }

                    groupImg.setOnMouseClicked(MovieDetailController.this::mouseClickOnSeat);
                    coupleBox.getChildren().add(groupImg);
                    if (coupleBox.getChildren().size() == 2) {
                        rowSeats.getChildren().add(coupleBox);
                        coupleBox = new HBox(5);
                        coupleBox.setPrefHeight(imgSize);
                        coupleBox.setAlignment(Pos.CENTER);
                    }
                }
                return rowSeats;
            }
        };

        for (int i = seats.length - 1; i >= 0; i--) {
            if (!currentSeatType.equals(seats[i][0].getSeatType())) {
                typeBox = new VBox();
                typeBox.setPadding(new Insets(20, 0, 20, 0));
                typeBox.setEffect(new DropShadow());
                currentSeatType = seats[i][0].getSeatType();
                showSeat.getChildren().add(typeBox);
                showSeatDetail(seats[i][0].getSeatImgPath());
            }

            HBox rowSeat = currentTheatre == 4?
                    couplePattern.generateRowSeat(seats[i], imgSize) : normalPattern.generateRowSeat(seats[i], imgSize);

            rowSeat.setAlignment(Pos.CENTER);
            rowSeat.prefWidthProperty().bind(showSeat.widthProperty());
            typeBox.getChildren().add(rowSeat);
        }
        showSeatDetail("");
    }

    //show the showtimes of the movies.
    private void showSchedule() {
        for (int i = 0; i < showingSystems.length; i++) {
            ArrayList<Schedule> schedules = showingSystems[i].getSchedules();
            Iterator<Schedule> iterator = schedules.iterator();
            int column = 0;
            while (iterator.hasNext()) {
                Schedule schedule = iterator.next();
                if (schedule.getMovies() == selectedMovie) {
                    Label scheduleLabel = NodeCreator.createTimeLabel(schedule.getStartTime());
                    scheduleLabel.setOnMouseClicked(this::mouseClickOnScheduleLabel);
                    tableSchedule.add(scheduleLabel, column++, i);
                }
            }
        }
    }

    //show available seat when change showtime in the same theatre.
    private void showSeatStatus(Schedule schedule) {
        RowSeatFinder normalPattern = new RowSeatFinder() {
            @Override
            public HBox findPosition(int row, VBox box) {
                VBox typeBox = (VBox) box.getChildren().get(0);
                ObservableList<Node> nodes = typeBox.getChildren();
                int size = nodes.size() - 1;
                return (HBox) nodes.get(size - row);
            }
        };
        RowSeatFinder hybridPattern = new RowSeatFinder() {
            @Override
            public HBox findPosition(int row, VBox box) {
                ObservableList<Node> typeBoxes = FXCollections.observableArrayList(box.getChildren());
                FXCollections.reverse(typeBoxes);
                typeBoxes.remove(0);
                int maxRange = 0;

                for (Node node : typeBoxes) {
                    VBox checking = (VBox) node;
                    maxRange += checking.getChildren().size();
                    if (row < maxRange) {
                        ObservableList<Node> rowBoxes = FXCollections.observableArrayList(checking.getChildren());
                        FXCollections.reverse(rowBoxes);
                        int index = row - maxRange + checking.getChildren().size();
                        return (HBox) rowBoxes.get(index);
                    }
                }
                return null;
            }
        };

        for (String position : schedule.getReservedPositionSeat()) {
            String type;
            AnchorPane reserveSeat;
            RowSeatFinder finder;
            int row = position.charAt(0) - 65;
            int column = Integer.parseInt(position.substring(1));
            if (currentTheatre == 0 || currentTheatre == 1) { type = "Normal"; finder = normalPattern;}
            else if (currentTheatre == 2 || currentTheatre == 3) { type = "Hybrid"; finder = hybridPattern;}
            else {type = "Couple"; finder = hybridPattern;}
            reserveSeat = SeatManager.getGroupImageByPosition(finder, showSeat, row, column - 1, type);
            reservedSeatList.add(reserveSeat);
            reserveSeat.setDisable(true);
            reserveSeat.setEffect(new ColorAdjust(0,0,-0.5,0));
        }
    }

    //reset unavailable seat before call showSeatStatus().
    private void resetSeatStatus() {
        for (AnchorPane anchorPane : reservedSeatList) {
            anchorPane.setDisable(false);
            anchorPane.setEffect(null);
        }
        for (AnchorPane pane : currentSelectedSeat) pane.getChildren().remove(2);
        currentSelectedSeat.clear();
        reservedSeatList.clear();
    }

    //check if user have selected more seat and the continue button will be available.
    private void checkAvailableContinueButton() {
        if (!currentSelectedSeat.isEmpty()) {
            continueButton.setDisable(false);
            EffectController.createFadeTransition(continueButton, 0.5, 1).play();
            PageController.stackWaringMessages.pop();
            PageController.stackWaringMessages.push(warningMessage);
            return;
        }
        continueButton.setDisable(true);
        EffectController.createFadeTransition(continueButton, 0.5, 0).play();
        PageController.stackWaringMessages.pop();
        PageController.stackWaringMessages.push(null);
    }

    // *** set objects, were passed from previous page ***//
    public void setSelectedMovie(Movies selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public void setSceneSize(ReadOnlyDoubleProperty sceneSize) {
        this.mainShowDetail.prefWidthProperty().bind(sceneSize);
    }

    public void setShowingSystems(ShowingSystem[] showingSystems) {
        this.showingSystems = showingSystems;
    }
    // *** set objects, were passed from previous page ***//
}