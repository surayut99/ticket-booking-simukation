package theatre;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import theatre.seat.SeatPatternGenerator;
import theatre.seat.TestingSeat;
import theatre.showingSystem.TestingSchedule;
import theatre.showingSystem.TestingShowingSystem;

import java.util.ArrayList;
import java.util.Iterator;

public class MovieDetail {
    private Movies selectedMovie;
    private TestingShowingSystem[] showingSystems;
    private Label preScheduleLabel, currentScheduleLabel;
    private ArrayList<AnchorPane> reservedSeatList;
//    private ArrayList<AnchorPane> currentReservedSeatList;
    private ArrayList<AnchorPane> currentSeletedSeat;
    private int preTheatre, currentTheatre, currentSchedule;
//    private ArrayList<ArrayList<ShowingSystem>> scheduleShowTimes;
//    private ArrayList<Seat> selectedSeat;
//    private ArrayList<Integer> collectCurrentRatePrice;

//    private ShowingSystem preSelectedShowingSystem, selectedShowingSystem;
//    private EffectOnObject effectOnObject;

    @FXML Label title, date, movieStatus, length;//, showShowingSystem;
    @FXML ImageView poster;
    @FXML AnchorPane seatPart, detailField, mainAnchorPane;
    @FXML VBox mainShowDetail, detailSeat ,showSeat;
//    @FXML HBox showPriceRate;
    @FXML GridPane tableSchedule;

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
//                effectOnObject = new EffectOnObject();
//                selectedSeat = new ArrayList<>();
//                collectCurrentRatePrice = new ArrayList<>();
                preTheatre = -1;
                currentTheatre = -1;
                currentSchedule = -1;
                preScheduleLabel = null;
                reservedSeatList = new ArrayList<>();
                currentSeletedSeat = new ArrayList<>();
//                preReservedPositionSeat = null;
                showSchedule();
            }
        });
    }

//    @FXML public void mouseClickOnSeatImg(MouseEvent event) {
//        AnchorPane anchorPane = (AnchorPane) event.getSource();
//        String position = ((Label) anchorPane.getChildren().get(1)).getText();
//        for (Seat[] seats : selectedShowingSystem.getSeats()) {
//            for (Seat eachSeat : seats) {
//                if (eachSeat.getSeatPosition().equals(position)) {
//                    if (eachSeat.getSeatImg().getEffect() == null) {
//                        effectOnObject.changeColorSelectedSeat(eachSeat.getSeatImg());
//                        selectedSeat.add(eachSeat);
//                        break;
//                    }
//                    eachSeat.getSeatImg().setEffect(null);
//                    selectedSeat.remove(eachSeat);
//                    break;
//                }
//            }
//        }
//        for (Seat seat : selectedSeat) {
//            System.out.print(seat.getSeatPosition() + " ");
//        }
//        System.out.println();
//    }
//
//    @FXML public void mouseClickOnScheduleLabel(MouseEvent event) {
//        Label selectedLabel = (Label) event.getSource();
//        selectedSeat.clear();
//        if (selectedShowingSystem == null) {detailSeat.getChildren().remove(1);}
//        selectedShowingSystem = getSelectedShowTimes(selectedLabel);
//        if (selectedShowingSystem != preSelectedShowingSystem) {
//            showSeat.getChildren().clear();
//            collectCurrentRatePrice.clear();
//            showPriceRate.getChildren().clear();
//            showSeat.setPrefHeight(30);
//
//            Seat[][] currentSeat = selectedShowingSystem.getSeats();
//            String preImgPath = currentSeat[currentSeat.length - 1][0].getSeatImgPath();
//            String system = selectedShowingSystem.getTypeSystem();
//            if (system.contains("/")) {
//                String[] splitString = system.split("/");
//                system = splitString[1];
//            }
//
//            showShowingSystem.setText("Showing System: " + system);
//            int imgSize = 60;
//
//            int couple = 0;
//            for (int i = currentSeat.length - 1; i >= 0; i--) {
//                HBox hBox = new HBox();
//                hBox.setAlignment(Pos.TOP_CENTER);
//
//                if (!currentSeat[i][0].getSeatImgPath().equals(preImgPath)) {
//                    preImgPath = currentSeat[i][0].getSeatImgPath();
//                    i++;
//                    hBox.setPrefHeight(imgSize);
//                    showSeat.getChildren().add(hBox);
//                    showSeat.setPrefHeight(showSeat.getPrefHeight() + 10);
//                    continue;
//                }
//
//                for (int j = 0; j < currentSeat[i].length; j++) {
//                    if (currentSeat[i][j].getSeatImg() == null) {
//                        ImageView imageView = new ImageView(currentSeat[i][j].getSeatImgPath());
//                        imageView.setFitHeight(imgSize);
//                        imageView.setFitWidth(imgSize);
//
//                        AnchorPane.setTopAnchor(imageView, 0.0);
//                        AnchorPane.setLeftAnchor(imageView, 0.0);
//                        AnchorPane.setBottomAnchor(imageView, 0.0);
//                        AnchorPane.setRightAnchor(imageView, 0.0);
//
//                        currentSeat[i][j].setSeatImg(imageView);
//                    }
//                    AnchorPane group = createGroup(currentSeat[i][j], imgSize);
//                    group.setCursor(Cursor.HAND);
//                    group.setOnMouseClicked(this::mouseClickOnSeatImg);
//                    hBox.getChildren().add(group);
//
//                    if (!collectCurrentRatePrice.contains(currentSeat[i][j].getPrice())) {
//                        collectCurrentRatePrice.add(currentSeat[i][j].getPrice());
//                        showPriceRate(currentSeat[i][j]);
//                    }
//
//                    if (selectedShowingSystem.getTypeSystem().contains("Couple")) {
//                        couple++;
//                        if (couple == 2) {
//                            Label seperator = new Label();
//                            seperator.setPrefHeight(imgSize);
//                            seperator.setPrefWidth(imgSize);
//                            hBox.getChildren().add(seperator);
//
//                            couple = 0;
//                        }
//                    }
//                }
//
//                showSeat.getChildren().add(hBox);
//                showSeat.setPrefHeight(showSeat.getPrefHeight() + imgSize);
//            }
//
//            seatPart.setPrefHeight(seatPart.getPrefHeight() + showSeat.getPrefHeight());
//            preSelectedShowingSystem = selectedShowingSystem;
//        }
//    }
//
//    public Label createScheduleLabel(ShowingSystem showingSystem) {
//        Label label = new Label(showingSystem.getTimeStart());
//        label.setPrefWidth(100);
//        label.setPrefHeight(40);
//
//        label.setFont(Font.font("System", FontWeight.BOLD, 24));
//        label.setTextFill(Color.BLACK);
//        label.setAlignment(Pos.CENTER);
//        label.setCursor(Cursor.HAND);
//
//        label.setStyle("-fx-background-color: linear-gradient(#FDC830,#F37335);\n" +
//                "    -fx-background-radius: 25;\n" +
//                "    -fx-background-insets: 0;");
//
//        return label;
//    }
//
//    public AnchorPane createGroup(Seat seat, int imgSize) {
//        AnchorPane groupImg = new AnchorPane();
//        ImageView imageView = seat.getSeatImg();
//        if (!seat.isReserve()) imageView.setEffect(null);
//        groupImg.setPrefHeight(imgSize);
//        groupImg.setPrefWidth(imgSize);
//        groupImg.getChildren().add(imageView);
//
//        Label position = new Label(seat.getSeatPosition());
//        position.setFont(Font.font("System", FontWeight.BOLD, 18));
//        position.setTextFill(Color.WHITE);
//        position.setAlignment(Pos.CENTER);
//        AnchorPane.setTopAnchor(position, 29.0);
//        AnchorPane.setLeftAnchor(position, 0.0);
//        AnchorPane.setBottomAnchor(position, 4.0);
//        AnchorPane.setRightAnchor(position, 0.0);
//        Glow glow = new Glow();
//        glow.setLevel(0.5);
//        position.setEffect(glow);
//        groupImg.getChildren().add(position);
//
//        return groupImg;
//    }
//
//    public ShowingSystem getSelectedShowTimes(Label selectedLabel) {
//        ShowingSystem currentSystem = null;
//        int theatre = GridPane.getRowIndex(selectedLabel) == null? 0 : GridPane.getRowIndex(selectedLabel);
//        int showtimes = GridPane.getColumnIndex(selectedLabel) == null? 0 : GridPane.getColumnIndex(selectedLabel);
//        return scheduleShowTimes.get(theatre).get(showtimes);
//    }
//
//    public void showPriceRate(Seat seat) {
//        ImageView showSeat = new ImageView(seat.getSeatImgPath());
//        showSeat.setFitWidth(70);
//        showSeat.setFitHeight(70);
//        showSeat.setEffect(effectOnObject.createDropShadow());
//
//        Label showPrice = new Label(seat.getType() + ": " + seat.getPrice() + " Baht");
//        showPrice.setFont(Font.font("System", FontWeight.BOLD, 24));
//        showPrice.setTextFill(Color.WHITE);
//        showPrice.setEffect(effectOnObject.createDropShadow());
//        showPrice.setPrefHeight(showPriceRate.getPrefHeight());
//
//        showPriceRate.getChildren().addAll(showSeat, showPrice);
//    }
//
//    public void showSchedule() {
//        int columnIndex = 0;
//        int rowIndex = 0;
//
//        for (ArrayList system : scheduleShowTimes) {
//            for (Object schedule : system) {
//                if (((ShowingSystem) schedule).getMovies() == selectedMovie) {
//                    Label showStartTime = createScheduleLabel((ShowingSystem) schedule);
//                    showStartTime.setOnMouseClicked(this::mouseClickOnScheduleLabel);
//                    tableSchedule.add(showStartTime, columnIndex, rowIndex);
//                    columnIndex++;
//                }
//            }
//            rowIndex++;
//            columnIndex = 0;
//        }
//    }
//
//    public void setScheduleShowTimes(ArrayList<ArrayList<ShowingSystem>> scheduleShowTimes) {
//        this.scheduleShowTimes = scheduleShowTimes;
//    }

    @FXML public void mouseClickOnScheduleLabel(MouseEvent event) {
        currentScheduleLabel = (Label) event.getSource();
        currentScheduleLabel.setDisable(true);
        if (!currentSeletedSeat.isEmpty()) {
            String topic = "You have just selected seat in this showtime.";
            String description = "If you change showtime or the theatre, the seat you selected will be unchecked.";
            AnchorPane warning = NodeCreator.createWarningAnchorPane(topic, description);
            ((Button) warning.getChildren().get(3)).setOnAction(this::ActionOnWarningOption);
            ((Button) warning.getChildren().get(4)).setOnAction(this::ActionOnWarningOption);
            mainShowDetail.setDisable(true);
            mainAnchorPane.getChildren().add(warning);
        }
        else {initialShowSeat();}
    }

    @FXML public void mouseClickOnSeat(MouseEvent event) {
        AnchorPane selectedSeat = (AnchorPane) event.getSource();
        switch (selectedSeat.getChildren().size()) {
            case 2:
                ImageView checkImg = NodeCreator.createImageView(30,30, "/picture/check.png", true);
                AnchorPane.setLeftAnchor(checkImg, 15.0);
                AnchorPane.setBottomAnchor(checkImg, 30.);
                selectedSeat.getChildren().add(checkImg);
                currentSeletedSeat.add(selectedSeat);
                break;

            case 3:
                selectedSeat.getChildren().remove(2);
                currentSeletedSeat.remove(selectedSeat);
                break;
        }
    }

    @FXML public void mouseClickOnMovieDetail(MouseEvent event) {
        Label label = (Label) event.getSource();
        switch (label.getText()) {
            case "See more detail":
                for (Node node : detailField.getChildren()) node.setOpacity(0);

                Label moreDetail = NodeCreator.createLabel("TestDetail", 16, "#ffffff");
                NodeCreator.setAlignmentNodeOnAnchorPane(moreDetail,0.,0.,0.,0.);
                moreDetail.setPadding(new Insets(10,10,10,10));
                moreDetail.setAlignment(Pos.TOP_LEFT);
                moreDetail.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
                detailField.getChildren().add(moreDetail);
                label.setText("Hide detail");
                break;

            case "Hide detail":
                detailField.getChildren().remove(2);
                for (Node node : detailField.getChildren()) node.setOpacity(1);
                label.setText("See more detail");
                break;
        }
    }

    @FXML public void ActionOnWarningOption(ActionEvent event) {
        String option = ((Button) event.getSource()).getText();
        switch (option) {
            case "Yes":
                initialShowSeat();
                break;
            case "No":
                currentScheduleLabel.setDisable(false);
                preScheduleLabel.setDisable(true);
                break;
        }
        mainAnchorPane.getChildren().remove(1);
        mainShowDetail.setDisable(false);
    }


    public void initialShowSeat() {
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

    private void showSeatDetail(String imgPath) {
        if (imgPath.equals("")) {
            if (showSeat.getChildren().get(showSeat.getChildren().size() - 1) instanceof Label) {showSeat.getChildren().remove(showSeat.getChildren().size() - 1);}
            TestingSchedule schedules = showingSystems[currentTheatre].getSchedules().get(currentSchedule);
            int available = schedules.getTotalSeat() - schedules.getReservedPositionSeat().size();
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

    private void showSeat() {
        showSeat.getChildren().clear();
        TestingSeat[][] seats = showingSystems[currentTheatre].getSeats();
        String currentSeatType = "";
        VBox typeBox = null;
        double imgSize = 60;

        SeatPatternGenerator normalPattern = new SeatPatternGenerator() {
            @Override
            public HBox generateRowSeat(TestingSeat[] seats, double imgSize) {
                HBox rowSeats = new HBox(5);
                rowSeats.setPrefHeight(imgSize);
                rowSeats.setAlignment(Pos.CENTER);
                for (int i = 0; i < seats.length; i++) {
                    AnchorPane groupImg = NodeCreator.createSeatIcon(imgSize, seats[i].getSeatPosition(), seats[i].getSeatImgPath());
                    if (seats[i].isReserve()) groupImg.setDisable(true);
                    groupImg.setOnMouseClicked(MovieDetail.this::mouseClickOnSeat);
                    rowSeats.getChildren().add(groupImg);
                }
                return rowSeats;
            }
        };

        SeatPatternGenerator couplePattern = new SeatPatternGenerator() {
            @Override
            public HBox generateRowSeat(TestingSeat[] seats, double imgSize) {
                HBox rowSeats = new HBox(25);
                HBox coupleBox = new HBox(5);
                coupleBox.setPrefHeight(imgSize);
                coupleBox.setAlignment(Pos.CENTER);
                for (int i = 0; i < seats.length; i++) {
                    AnchorPane groupImg = NodeCreator.createSeatIcon(imgSize, seats[i].getSeatPosition(), seats[i].getSeatImgPath());
                    if (seats[i].isReserve()) groupImg.setDisable(true);
                    groupImg.setOnMouseClicked(MovieDetail.this::mouseClickOnSeat);
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

    private void showSchedule() {
        for (int i = 0; i < showingSystems.length; i++) {
            ArrayList<TestingSchedule> schedules = showingSystems[i].getSchedules();
            Iterator<TestingSchedule> iterator = schedules.iterator();
            int column = 0;
            while (iterator.hasNext()) {
                TestingSchedule schedule = iterator.next();
                if (schedule.getMovies() == selectedMovie) {
                    Label scheduleLabel = NodeCreator.createTimeLabel(schedule.getStartTime());
                    scheduleLabel.setOnMouseClicked(this::mouseClickOnScheduleLabel);
                    tableSchedule.add(scheduleLabel, column++, i);
                }
            }
        }
    }

    private void showSeatStatus(TestingSchedule schedule) {
        for (String position : schedule.getReservedPositionSeat()) {
            String type;
            int row = position.charAt(0) - 65;
            int column = Integer.parseInt(position.substring(1));
            if (currentTheatre == 0 || currentTheatre == 1) {type = "Normal";}
            else if (currentTheatre == 2 || currentTheatre == 3) {type = "Hybrid";}
            else {type = "Couple";}
            AnchorPane anchorPane = getGroupImageByPosition(row, column - 1, type);
            reservedSeatList.add(anchorPane);
            anchorPane.setDisable(true);
            anchorPane.setEffect(new ColorAdjust(0,0,-0.6,0));
        }
    }

    private void resetSeatStatus() {
        for (AnchorPane anchorPane : reservedSeatList) {
            anchorPane.setDisable(false);
            anchorPane.setEffect(null);
        }
        for (AnchorPane pane : currentSeletedSeat) pane.getChildren().remove(2);
        currentSeletedSeat.clear();
        reservedSeatList.clear();
    }

    private AnchorPane getGroupImageByPosition(int row, int column, String patternSeat) {
        if (patternSeat.equals("Normal")) {
            RowSeatFinder normalPattern = new RowSeatFinder() {
                @Override
                public HBox findPosition(int row, VBox box) {
                    VBox typeBox = (VBox) box.getChildren().get(0);
                    ObservableList<Node> nodes = typeBox.getChildren();
                    int size = nodes.size() - 1;
                    return (HBox) nodes.get(size - row);
                }
            };

            normalPattern.findPosition(row, showSeat).getChildren().get(column);
            return (AnchorPane) (normalPattern.findPosition(row, showSeat)).getChildren().get(column);
        }

        RowSeatFinder hybridPattern = new RowSeatFinder() {
            @Override
            public HBox findPosition(int row, VBox box) {
                ObservableList<Node> typeBoxes = FXCollections.observableArrayList(box.getChildren());
                FXCollections.reverse(typeBoxes);
                int maxRange = 0;
                for (Node node : typeBoxes) {
                    VBox checking = (VBox) node;
                    System.out.println(checking.getChildren().size());
                    maxRange += checking.getChildren().size();
                    if (row < maxRange) {
                        ObservableList<Node> rowBoxes = FXCollections.observableArrayList(checking.getChildren());
                        FXCollections.reverse(rowBoxes);
                        int index = row - maxRange + checking.getChildren().size();
                        System.out.println(index);
                        return (HBox) rowBoxes.get(index);
                    }
                }

                return null;
            }
        };

        if (patternSeat.equals("Hybrid")) {
            return (AnchorPane) hybridPattern.findPosition(row, showSeat).getChildren().get(column);
        }

        HBox rowBox = hybridPattern.findPosition(row, showSeat);
        rowBox = (HBox) rowBox.getChildren().get(column / 2);
        return (AnchorPane) rowBox.getChildren().get(column % 2);
    }

    public void setSelectedMovie(Movies selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public void setSceneSize(ReadOnlyDoubleProperty sceneSize) {
        this.mainShowDetail.prefWidthProperty().bind(sceneSize);
    }

    public void setShowingSystems(TestingShowingSystem[] showingSystems) {
        this.showingSystems = showingSystems;
    }
}