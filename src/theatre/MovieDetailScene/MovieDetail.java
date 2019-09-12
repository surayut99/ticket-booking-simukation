package theatre.MovieDetailScene;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import theatre.EffectOnObject;
import theatre.Seat.Seat;
import theatre.ShowingSystem.ShowingSystem;
import theatre.movies.ComingSoonMovies;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.Schedule.Schedule;

import java.util.ArrayList;

public class MovieDetail {
    private Movies selectedMovie;
    private ArrayList<ArrayList<ShowingSystem>> scheduleShowtimes;
    private ArrayList<ImageView> selectedSeat;
    private ArrayList<Integer> collectCurrentRatePrice;

    private ShowingSystem selectedShowingSystem;
    private ShowingSystem preSelectedShowingSystem;
    private VBox showSeat;
    private EffectOnObject effectOnObject;
//    private ImageView check;

    @FXML Label title, date, movieStatus, length, warningShowTime, showShowingSystem;
    @FXML ImageView poster;
    @FXML AnchorPane seatPart;
    @FXML VBox mainShowDetail, detailSeat;
    @FXML HBox showPriceRate;
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
                }
                effectOnObject = new EffectOnObject();
                selectedSeat = new ArrayList<>();
                collectCurrentRatePrice = new ArrayList<>();

                checkVBoxToShowSeat();
                showSchedule();
//                check = new ImageView("/picture/check.png");
            }
        });
    }

    @FXML public void showSeatAfterClickOnTime(MouseEvent event) {
        Label selectedLabel = (Label) event.getSource();
        checkSelectedShowingSystem(selectedLabel);

        if (warningShowTime.getOpacity() == 1) {
            warningShowTime.setOpacity(0);
            seatPart.setOpacity(1);
            detailSeat.setPrefHeight(125);
            effectOnObject.fadeOutNode(warningShowTime);
        }

        if (selectedShowingSystem != preSelectedShowingSystem) {
            showSeat.getChildren().clear();
            showSeat.setPrefHeight(30);
            seatPart.setPrefHeight(70);

            collectCurrentRatePrice.clear();
            Seat[][] currentSeat = this.selectedShowingSystem.getSeats();
            String preImgPath = currentSeat[currentSeat.length - 1][0].getSeatImgPath();

            String system = getSystem(selectedShowingSystem.getTypeSystem());
            showShowingSystem.setText("Showing System: " + system);
            int imgSize = 60;
            showPriceRate.getChildren().clear();

            int couple = 0;
            int half = 0;
            for (int i = currentSeat.length - 1; i >= 0; i--) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_CENTER);

                if (!currentSeat[i][0].getSeatImgPath().equals(preImgPath)) {
                    preImgPath = currentSeat[i][0].getSeatImgPath();
                    i++;
                    hBox.setPrefHeight(imgSize);
                    showSeat.getChildren().add(hBox);
                    showSeat.setPrefHeight(showSeat.getPrefHeight() + 10);
                    continue;
                }

                for (int j = 0; j < currentSeat[i].length; j++) {
                    if (currentSeat[i][j].getSeatImg() == null) {
                        ImageView imageView = new ImageView(currentSeat[i][j].getSeatImgPath());
                        imageView.setFitHeight(imgSize);
                        imageView.setFitWidth(imgSize);

                        AnchorPane.setTopAnchor(imageView, 0.0);
                        AnchorPane.setLeftAnchor(imageView, 0.0);
                        AnchorPane.setBottomAnchor(imageView, 0.0);
                        AnchorPane.setRightAnchor(imageView, 0.0);

                        currentSeat[i][j].setSeatImg(imageView);
                    }
                    AnchorPane group = setGroupSeat(currentSeat[i][j], imgSize);
                    group.setCursor(Cursor.HAND);
                    group.setOnMouseClicked(this::check);
                    hBox.getChildren().add(group);


//                    if (currentSeat[i][j].getSeatImg() == null) {
//                        ImageView imageView = new ImageView(currentSeat[i][j].getSeatImgPath());
//                        imageView.setFitWidth(imgSize);
//                        imageView.setFitHeight(imgSize);
//                        imageView.setCursor(Cursor.HAND);
//                        imageView.setOnMouseClicked(this::check);
//
//                        currentSeat[i][j].setSeatImg(imageView);
//                        hBox.getChildren().add(imageView);
//                    }
//                    else {
//                        hBox.getChildren().add(currentSeat[i][j].getSeatImg());
//                    }

                    if (!collectCurrentRatePrice.contains(currentSeat[i][j].getPrice())) {
                        collectCurrentRatePrice.add(currentSeat[i][j].getPrice());
                        showPriceRate(currentSeat[i][j]);
                    }

                    if (selectedShowingSystem.getTypeSystem().contains("Couple")) {
                        couple++;
                        if (couple == 2) {
                            Label seperator = new Label();
                            seperator.setPrefHeight(imgSize);
                            seperator.setPrefWidth(imgSize);
                            hBox.getChildren().add(seperator);

                            couple = 0;
                        }
                    }
                }

                showSeat.getChildren().add(hBox);
                showSeat.setPrefHeight(showSeat.getPrefHeight() + imgSize);
            }

            seatPart.setPrefHeight(seatPart.getPrefHeight() + showSeat.getPrefHeight());
            preSelectedShowingSystem = selectedShowingSystem;
        }
    }

    @FXML public void check(MouseEvent event) {
        AnchorPane anchorPane = (AnchorPane) event.getSource();
        ImageView seat = (ImageView) anchorPane.getChildren().get(0);

//        ImageView seat = (ImageView) event.getSource();
        if (seat.getEffect() == null) {
            effectOnObject.changeColorSelectedSeat(seat);
            selectedSeat.add(seat);
        }

        else {
            seat.setEffect(null);
            selectedSeat.remove(seat);
        }

        for (Seat[] seats : selectedShowingSystem.getSeats()) {
            for (Seat eachSeat : seats) {
                if (eachSeat.getSeatImg() == seat) {
                    System.out.println(eachSeat.getSeatPosition());
                    break;
                }
            }
        }

        System.out.println("Number of selected seat: " + selectedSeat.size());
    }

    public String getSystem(String typeSystem) {
        if (typeSystem.contains("/")) {
            String[] splitString = typeSystem.split("/");
            return splitString[1];
        }

        return typeSystem;
    }

    public void showPriceRate(Seat seat) {
        ImageView showSeat = new ImageView(seat.getSeatImgPath());
        showSeat.setFitWidth(70);
        showSeat.setFitHeight(70);
        showSeat.setEffect(effectOnObject.createDropShadow());

        Label showPrice = new Label(seat.getType() + ": " + seat.getPrice() + " Baht");
        showPrice.setFont(Font.font("System", FontWeight.BOLD, 24));
        showPrice.setTextFill(Color.WHITE);
        showPrice.setEffect(effectOnObject.createDropShadow());
        showPrice.setPrefHeight(showPriceRate.getPrefHeight());

        showPriceRate.getChildren().addAll(showSeat, showPrice);
    }

    public void showSchedule() {
        int columnIndex = 0;
        int rowIndex = 0;

        for (ArrayList system : scheduleShowtimes) {
            for (Object schedule : system) {
                if (((ShowingSystem) schedule).getMovies() == selectedMovie) {
                    Label showStartTime = ((ShowingSystem) schedule).getTimeLabel();
                    showStartTime.setOnMouseClicked(this::showSeatAfterClickOnTime);
                    tableSchedule.add(showStartTime, columnIndex, rowIndex);
                    columnIndex++;
                }
            }
            rowIndex++;
            columnIndex = 0;
        }
    }

    public void setScheduleShowtimes(ArrayList<ArrayList<ShowingSystem>> scheduleShowtimes) {
        this.scheduleShowtimes = scheduleShowtimes;
    }

    public void setSelectedMovie(Movies selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public void setSceneSize(ReadOnlyDoubleProperty sceneSize) {
        this.mainShowDetail.prefWidthProperty().bind(sceneSize);
    }

    public AnchorPane setGroupSeat(Seat seat, int imgSize) {
        AnchorPane groupImag = new AnchorPane();
        groupImag.setPrefHeight(imgSize);
        groupImag.setPrefWidth(imgSize);
        groupImag.getChildren().add(seat.getSeatImg());

        Label position = new Label(seat.getSeatPosition());
        position.setFont(Font.font("System", FontWeight.BOLD, 18));
        position.setTextFill(Color.WHITE);
        position.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(position, 29.0);
        AnchorPane.setLeftAnchor(position, 0.0);
        AnchorPane.setBottomAnchor(position, 4.0);
        AnchorPane.setRightAnchor(position, 0.0);
        Glow glow = new Glow();
        glow.setLevel(0.5);
        position.setEffect(glow);
        groupImag.getChildren().add(position);

        return groupImag;
    }

    public void checkSelectedShowingSystem(Label selectedLabel) {
        for (ArrayList system : scheduleShowtimes) {
            for (Object schedule : system) {
                if (((ShowingSystem)schedule).getTimeLabel() == selectedLabel) {
                    this.selectedShowingSystem = (ShowingSystem) schedule;
                    break;
                }
            }
        }
    }

    public void checkVBoxToShowSeat() {
        if (showSeat == null) {
            showSeat = new VBox();
            showSeat.setPrefWidth(954);
            showSeat.setPrefHeight(0);
            showSeat.setLayoutX(9);
            showSeat.setLayoutY(100);
            showSeat.setAlignment(Pos.TOP_CENTER);

            AnchorPane.setLeftAnchor(this.showSeat, 9.0);
            AnchorPane.setRightAnchor(this.showSeat, 9.0);

            seatPart.getChildren().add(showSeat);
        }
    }
}