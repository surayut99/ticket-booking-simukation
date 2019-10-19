package theatre;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import theatre.movies.ComingSoonMovies;
import theatre.movies.MovieCollector;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.seat.TestingPremiumSeat;
import theatre.seat.TestingSeat;
import theatre.seat.TestingVIPSeat;
import theatre.showingSystem.Testing3DSystem;
import theatre.showingSystem.Testing4KSystem;
import theatre.showingSystem.TestingSchedule;
import theatre.showingSystem.TestingShowingSystem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Homepage {
    private Label preOption;
    private Movies selectedMovie;
    private String styleLabel;
    private VBox vBoxShow;
//    private ArrayList<Movies> collectShowingMovies, collectComingSoonMovies;
//    private ArrayList<HBox> collectRowShowingMockUp, collectRowSoonMockUp;
    private ArrayList<AnchorPane> showingMockUp, soonMockUp;
//    private ArrayList<ArrayList<ShowingSystem>> scheduleShowTimes;
//    private ArrayList<ArrayList<Movies>> sequenceMovieList;
    private TestingShowingSystem[] showingSystems;

    @FXML Label showingOption, comingSoonOption, accountOption, logInOption;
    @FXML AnchorPane showPart, mainPane, showContent;

    @FXML public void initialize() {
        //Bind nodes'size with windows's size
        showPart.prefHeightProperty().bind(mainPane.heightProperty());
        showContent.prefWidthProperty().bind(showPart.widthProperty());
        showContent.prefHeightProperty().bind(showPart.heightProperty());

        preOption = showingOption;
        styleLabel = showingOption.getStyle();
        EffectOnObject.fadeInNode(showContent.getChildren().get(0));
        FadeTransition fadeBackground = new FadeTransition(Duration.seconds(1), showContent.getChildren().get(0));
        fadeBackground.setToValue(0);
        fadeBackground.play();
        fadeBackground.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showContent.getChildren().remove(0);
                vBoxShow = new VBox(10);
                vBoxShow.setPadding(new Insets(10,10,10,10));
                NodeCreator.setAlignmentNodeOnAnchorPane(vBoxShow, 0., 0., 0., 0.);
                utilsMockUp();
//                addMovies();
//                addShowingMovie();
//                addComingSoonMovie();
//                addShowingSystem();
//                addScheduleShowTime();
//                addSequenceMovies();
//                addScheduleShowTimes();
//                addMockUp();
//                showMockUp();
            }
        });
//        addShowingMovie();
//        addComingSoonMovie();
//        addShowingSystem();
//        addScheduleShowTime();
    }

    @FXML public void mouseEnterOnNode(MouseEvent event) {
        Node enterOnObject = (Node) event.getSource();

        if (enterOnObject.getClass() == Label.class) {
            if(enterOnObject.getStyle().contains("null")) {
                EffectOnObject.changeColorOnSelectedLabel((Label) enterOnObject, 0);
            }
        }

        else if (enterOnObject.getClass() == AnchorPane.class) {
            AnchorPane enterAnchor = (AnchorPane) enterOnObject;
            for (int i = 0; i < enterAnchor.getChildren().size(); i++) {
                EffectOnObject.changeScaleOnSelectedNode(enterAnchor.getChildren().get(i), 1.1);
            }
        }
    }

    @FXML public void mouseExitOffNode(MouseEvent event) {
        Node enterOnObject = (Node) event.getSource();

        if (enterOnObject.getClass() == Label.class) {
            if(enterOnObject.getStyle().contains("null")) {
                EffectOnObject.changeColorOnSelectedLabel((Label) enterOnObject, 2);
            }
        }

        else if (enterOnObject.getClass() == AnchorPane.class) {
            AnchorPane enterAnchor = (AnchorPane) enterOnObject;
            for (int i = 0; i < enterAnchor.getChildren().size(); i++) {
                EffectOnObject.changeScaleOnSelectedNode(enterAnchor.getChildren().get(i), 1.0);
            }
        }
    }

    @FXML public void mouseClickOnNode(MouseEvent event) {
        Node clickedNode = (Node) event.getSource();

        // if option below is clicked
        if (clickedNode.getClass() == Label.class) {
            Label selectedOption = (Label) clickedNode;

            if (selectedOption == preOption) {return;}
            if (preOption == null) {preOption = selectedOption;}


            preOption.setStyle("-fx-background-color: null;");
            preOption = selectedOption;
            selectedOption.setEffect(EffectOnObject.clickAndChangLabelColor());
            selectedOption.setStyle(styleLabel);

            if (selectedOption == showingOption || selectedOption == comingSoonOption) {showMockUp();}

            else if (selectedOption == accountOption) {
                showAccount(); //Wait to update
            }

            else {
                showLogin(); //Wait to update
            }
        }

        //if poster is clicked
        else if (clickedNode.getClass() == AnchorPane.class) {
            AnchorPane clickMockUp = (AnchorPane) clickedNode;
            Label movieTitle = (Label) clickMockUp.getChildren().get(1);
//            selectedMovie = getSelectedMovie(movieTitle.getText());
            selectedMovie = MovieCollector.findMovie(movieTitle.getText());
            preOption.setStyle("-fx-background-color: null;");
            preOption = null;
            showMovieDetail();
        }
    }

    public void utilsMockUp() {
        addMovies();
        addMockUp();
        showMockUp();
        addShowingSystem();
        addScheduleShowTime();
    }

    public void addMovies() {
        LocalDate date = LocalDate.now();
        MovieCollector.moviesList.add(new ShowingMovies(
                "Spider-Man: Homeless", "02:02:02", date, "picture/poster/spider-man-homeless.jpg"
        ));
        MovieCollector.moviesList.add(new ShowingMovies(
                "Boosty And the Beast","01:56:49", date, "picture/poster/booty_and_the_beast.jpg"
        ));
        MovieCollector.moviesList.add(new ShowingMovies(
                "Once Upon Deadpool", "02:34:08", date, "picture/poster/once_upon_deadpool.jpg"
        ));


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateSoon = LocalDate.parse("09/09/2020", dateTimeFormatter);
        MovieCollector.moviesList.add(new ComingSoonMovies("INCEPTION", "--:--:--", dateSoon, "picture/poster/inception.jpg"));
    }

    public void addMockUp() {
        showingMockUp = MovieCollector.generateMockUp(ShowingMovies.class);
        soonMockUp = MovieCollector.generateMockUp(ComingSoonMovies.class);
    }

    public void showMockUp() {
        ArrayList<AnchorPane> mockUpList = showingOption.getStyle().contains("null") ?
                soonMockUp : showingMockUp;

        showContent.getChildren().clear();
        showContent.getChildren().add(vBoxShow);
        vBoxShow.getChildren().clear();
        EffectOnObject.fadeOutNode(vBoxShow);
        int numMockUp = 0;
        HBox row = new HBox(10);
        vBoxShow.getChildren().add(row);
        for (AnchorPane mockUp : mockUpList) {
            if (numMockUp == 5) {
                row = new HBox(10);
                vBoxShow.getChildren().add(row);
                numMockUp = 0;
            }
            row.getChildren().add(mockUp);
            mockUp.setOnMouseClicked(this::mouseClickOnNode);
            mockUp.setOnMouseEntered(this::mouseEnterOnNode);
            mockUp.setOnMouseExited(this::mouseExitOffNode);
            numMockUp++;

        }

        EffectOnObject.fadeInNode(vBoxShow);


//        ArrayList<HBox> rowMockUpList = showingOption.getStyle().contains("null")?
//                collectRowSoonMockUp : collectRowShowingMockUp;
//        vBoxShow.getChildren().clear();
//        EffectOnObject.fadeInNode(vBoxShow);
//
//        for (HBox rowMockUp : rowMockUpList) {
//            rowMockUp.setOpacity(0);
//            vBoxShow.getChildren().add(rowMockUp);
//            EffectOnObject.fadeInNode(rowMockUp);
//        }
    }

    public void addShowingSystem() {
        showingSystems = new TestingShowingSystem[5];
        int[][] numSeat = {{5, 8}, {8, 10}, {10, 12}, {8, 10}, {8, 8}};
        String[] systemTypes = {"Normal", "4K", "4K", "3D", "3D"};

        for (int i = 0; i < 5; i++) {
            TestingSeat[][] seats = new TestingSeat[numSeat[i][0]][numSeat[i][1]];
            ArrayList<String> seatTypes = new ArrayList<>();
            ArrayList<Double> seatPrices = new ArrayList<>();
            for (int j = 0; j < numSeat[i][0]; j++) {
                char letter = (char) ('A' + j);
                if (i == 0 || i == 1) {
                    seats[j] = createSeat(numSeat[i][1], letter, "Normal");
                    if (!seatTypes.contains("Normal")) {seatTypes.add("Normal");}
                    if (!seatPrices.contains(seats[j][0].getPrice())) {seatPrices.add(seats[j][0].getPrice());}
                    continue;
                }
                if (j == 0) {
                    seats[j] = createSeat(numSeat[i][1], letter, "VIP");
                    seatTypes.add("VIP");
                    seatPrices.add(seats[j][0].getPrice());
                }
                else if (j == 1 || j == 2) {
                    seats[j] = createSeat(numSeat[i][1], letter, "Premium");
                    if (!seatTypes.contains("Premium")) {seatTypes.add("Premium");}
                    if (!seatPrices.contains(seats[j][0].getPrice())) {seatPrices.add(seats[j][0].getPrice());}
                }
                else {
                    seats[j] = createSeat(numSeat[i][1], letter, "Normal");
                    if (!seatTypes.contains("Normal")) {seatTypes.add("Normal");}
                    if (!seatPrices.contains(seats[j][0].getPrice())) {seatPrices.add(seats[j][0].getPrice());}
                }
            }

            switch (systemTypes[i]) {
                case "4K":
                    Testing4KSystem system4K = new Testing4KSystem(systemTypes[i], 0, seats);
                    system4K.increasePrice();
                    showingSystems[i] = system4K;
                    showingSystems[i].setSeatTypes(seatTypes);
                    showingSystems[i].setSeatPrices(seatPrices);
                    break;

                case "3D":
                    Testing3DSystem system3D = new Testing3DSystem(systemTypes[i], 0, seats);
                    system3D.increasePrice();
                    showingSystems[i] = system3D;
                    showingSystems[i].setSeatTypes(seatTypes);
                    showingSystems[i].setSeatPrices(seatPrices);
                    break;

                default:
                    showingSystems[i] = new TestingShowingSystem(systemTypes[i], 0, seats);
                    showingSystems[i].setSeatTypes(seatTypes);
                    showingSystems[i].setSeatPrices(seatPrices);
                    break;

            }
        }

//        printTheatreDetail();
    }

    public void addScheduleShowTime() {
        Movies spiderman = MovieCollector.findMovie("spider");
        Movies boosty = MovieCollector.findMovie("boosty");
        Movies deadpool = MovieCollector.findMovie("deadpool");

        showingSystems[0].addSequenceMovies(spiderman, spiderman, boosty, deadpool, boosty, deadpool, deadpool);
        showingSystems[1].addSequenceMovies(deadpool, boosty, boosty, spiderman, deadpool, spiderman);
        showingSystems[2].addSequenceMovies(boosty, spiderman, deadpool, deadpool, boosty, boosty);
        showingSystems[3].addSequenceMovies(deadpool, spiderman, deadpool, spiderman, boosty, boosty);
        showingSystems[4].addSequenceMovies(spiderman, boosty, spiderman, boosty, deadpool, deadpool);

//        for (TestingShowingSystem showingSystem : showingSystems) {
//            System.out.println("Showing System: " + showingSystem.getSystemType());
//            for (int i = 0; i < showingSystem.getSeatTypes().size(); i++) {
//                System.out.print(showingSystem.getSeatTypes().get(i) + ": " + showingSystem.getSeatPrices().get(i) + " ");
//            }
//            System.out.println();
//            for (TestingSchedule schedule : showingSystem.getSchedules()) {
//                System.out.println(schedule.getMovies().getTitle() + ": " + schedule.getStartTime() +
//                        " Total: " + schedule.getTotalSeat() + " Available: " + schedule.getNumAvailableSeat());
//            }
//            System.out.println();
//        }
    }

    public TestingSeat[] createSeat(int column, char letter, String type) {
        TestingSeat[] seats = new TestingSeat[column];
        for (int i = 0; i < column; i++) {
            String position = Character.toString(letter) + (i + 1);
            switch (type) {
                case "Premium":
                    TestingPremiumSeat premiumSeat = new TestingPremiumSeat(type, 200, "picture/seat/premium.png", position, false);
                    premiumSeat.increasePrice();
                    seats[i] = premiumSeat;
                    break;

                case "VIP":
                    TestingVIPSeat vipSeat = new TestingVIPSeat(type, 200, "picture/seat/vip.png", position, false);
                    vipSeat.increasePrice();
                    seats[i] = vipSeat;
                    break;

                default:
                    seats[i] = new TestingSeat(type, 200, "picture/seat/normal.png", position, false);
                    break;
            }
        }

        return seats;
    }

//    public Movies getSelectedMovie(String selectedTitle){
//        ArrayList<Movies> moviesList = showingOption.getStyle().contains("null") ?
//                collectComingSoonMovies : collectShowingMovies;
//
//        for (Movies movie : moviesList) {
//            if (movie.getTitle().equals(selectedTitle)){
//                preOption.setStyle("-fx-background-color: null;");
//                preOption = null;
//                return movie;
//            }
//        }
//        return null;
//    }


//    public ArrayList<HBox> createRowMockUp(ArrayList<Movies> collectionMovies) {
//        int numMoviesInRow = 0;
//        ArrayList<HBox> rowMockUpList = new ArrayList<>();
//        HBox rowMockUp = null;
//        for (Movies movies : collectionMovies) {
//            if (numMoviesInRow == 0) {
//                rowMockUp = new HBox(20);
//                rowMockUp.setPadding(new Insets(20,20,20,20));
//                rowMockUp.prefWidthProperty().bind(vBoxShow.widthProperty());
//                rowMockUp.setAlignment(Pos.CENTER_LEFT);
//                AnchorPane.setLeftAnchor(rowMockUp, 10.0);
//                AnchorPane.setRightAnchor(rowMockUp, 10.0);
//                rowMockUpList.add(rowMockUp);
//            }
//
//            ImageView poster = NodeCreator.createImageView(200,280, movies.getLocationPoster(), false);
//            Label title = NodeCreator.createLabel(movies.getTitle(), 18, "#ffffff");
//            NodeCreator.setAlignmentNodeOnAnchorPane(title, 290. ,0.,0.,0.);
//            NodeCreator.setAlignmentNodeOnAnchorPane(poster, 0., 0.,0., null);
//            AnchorPane blockMockUp = NodeCreator.createAnchorPane(200, 320, poster, title);
//            blockMockUp.setCursor(Cursor.HAND);
//            blockMockUp.setOnMouseEntered(this::mouseEnterOnNode);
//            blockMockUp.setOnMouseExited(this::mouseExitOffNode);
//            blockMockUp.setOnMouseClicked(this::mouseClickOnNode);
//            rowMockUp.getChildren().add(blockMockUp);
//            numMoviesInRow++;
//
//            if (numMoviesInRow == 5) {numMoviesInRow = 0;}
//        }
//
//        return rowMockUpList;
//    }

//    public AnchorPane createTheatreAnchorPaneToShow() {
//        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setPrefWidth(200);
//        anchorPane.setPrefHeight(320);
////        anchorPane.prefWidthProperty().bind(showPart.widthProperty());
//        return anchorPane;
//    }

    //    public void printTheatreDetail() {
//        for (TestingShowingSystem system : showingSystems) {
//            System.out.println(system.getSystemType());
//            for (int i = 0; i < system.getSeatTypes().size(); i++) {
//                System.out.println(system.getSeatTypes().get(i));
//                System.out.println(system.getSeatPrices().get(i) + system.getPrice());
//            }
//            System.out.println();
//        }
//    }

//    public void addSequenceMovies() {
//        sequenceMovieList = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            sequenceMovieList.add(new ArrayList<>());
//            sequenceMovieList.get(i).addAll(collectShowingMovies);
//            sequenceMovieList.get(i).addAll(collectShowingMovies);
//            sequenceMovieList.get(i).addAll(collectShowingMovies);
//        }
//    }
//
//    public void addScheduleShowTimes() {
//        Schedule scheduler = new Schedule();
//        scheduleShowTimes = new ArrayList<>();
//        String[] typeSystem = {"Normal", "Normal/4K", "Hybrid/4K", "Hybrid/3D", "Couple/4K"};
//
//        for (int i = 0; i < 5; i++) {
//            ArrayList<ShowingSystem> schedule = new ArrayList<>();
//
//            for (Movies movies : sequenceMovieList.get(i)) {
//                if (scheduler.getStartHour() < 8) {continue;}
//
//                ShowingSystem system = scheduler.createSchedule(movies, typeSystem[i]);
//                addSeat(system, i);
//                schedule.add(system);
//            }
//
//            scheduler.resetTime();
//            scheduleShowTimes.add(schedule);
//        }
//    }

//    public void addSeat(ShowingSystem showingSystem, int index) {
//        int[] startPrice = {220, 260, 260, 290, 300};
//        int[][] numSeat = {{5, 8}, {5, 10}, {10, 12}, {8, 10}, {8, 8}};
//
//        showingSystem.generateSeat(numSeat[index][0], numSeat[index][1], startPrice[index]);
//    }

    //    public AnchorPane createBlock() {
//        AnchorPane block = new AnchorPane();
//        block.setPrefHeight(320);
//        block.setPrefWidth(200);
//
//        return block;
//    }

//    public ImageView createPoster(String imgPath) {
//        ImageView poster = new ImageView(imgPath);
//        poster.setFitHeight(280);
//        poster.setFitWidth(200);
//        poster.setPreserveRatio(true);
//        AnchorPane.setRightAnchor(poster, 0.0);
//        AnchorPane.setLeftAnchor(poster, 0.0);
//        AnchorPane.setTopAnchor(poster, 0.0);
//
//        return poster;
//    }

//    public Label createTitle(String title) {
//        Label titleLabel = new Label(title);
//        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setAlignment(Pos.CENTER);
//        AnchorPane.setRightAnchor(titleLabel, 0.0);
//        AnchorPane.setLeftAnchor(titleLabel, 0.0);
//        AnchorPane.setBottomAnchor(titleLabel, 0.0);
//        AnchorPane.setTopAnchor(titleLabel, 290.0);
//
//        return titleLabel;
//    }


//    public void showMockUp() {
//        EffectOnObject.fadeOutNode(vBoxShow);
//
//        ArrayList<HBox> rowMockUpList = showingOption.getStyle().contains("null")?
//                collectRowSoonMockUp : collectRowShowingMockUp;
//        vBoxShow.getChildren().clear();
//        EffectOnObject.fadeInNode(vBoxShow);
//
//        for (HBox rowMockUp : rowMockUpList) {
//            rowMockUp.setOpacity(0);
//            vBoxShow.getChildren().add(rowMockUp);
//            EffectOnObject.fadeInNode(rowMockUp);
//        }
//    }
    //    public void addShowingMovie() {
//        collectShowingMovies = new ArrayList<>();
//        LocalDate date = LocalDate.now();
//        collectShowingMovies.add(new ShowingMovies(
//                "Spider-Man: Homeless", "02:02:02", date, "picture/poster/spider-man-homeless.jpg"
//        ));
//        collectShowingMovies.add(new ShowingMovies(
//                "Boosty And the Beast","01:56:49", date, "picture/poster/booty_and_the_beast.jpg"
//        ));
//        collectShowingMovies.add(new ShowingMovies(
//                "Once Upon Deadpool", "02:34:08", date, "picture/poster/once_upon_deadpool.jpg"
//        ));
//    }

//    public void addComingSoonMovie() {
//        collectComingSoonMovies = new ArrayList<>();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate date = LocalDate.parse("09/09/2020", dateTimeFormatter);
//        collectComingSoonMovies.add(new ComingSoonMovies("INCEPTION", "--:--:--", date, "picture/poster/inception.jpg"));
//    }

//    public void addMockUp() {
//        collectRowShowingMockUp = createRowMockUp(collectShowingMovies);
//        collectRowSoonMockUp = createRowMockUp(collectComingSoonMovies);
//    }

    public void showMovieDetail() {
        AnchorPane newPane = NodeCreator.createAnchorPane(200, 320);//createTheatreAnchorPaneToShow();
        vBoxShow.getChildren().clear();
        vBoxShow.getChildren().add(newPane);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movieDetail.fxml"));
            Parent root = loader.load();

            MovieDetail detailController = loader.getController();
            detailController.setSelectedMovie(selectedMovie);
//            detailController.setScheduleShowTimes(scheduleShowTimes);
            detailController.setShowingSystems(showingSystems);
            detailController.setSceneSize(mainPane.widthProperty());

            root.opacityProperty().setValue(0);
            newPane.getChildren().add(root);

            Timeline showDetailScene = new Timeline();
            KeyValue keyValue = new KeyValue(root.opacityProperty(), 1, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
            showDetailScene.getKeyFrames().add(keyFrame);
            showDetailScene.play();

        } catch (IOException e) {e.printStackTrace();}
    }

    public void showAccount() {
        vBoxShow.getChildren().clear();
    }

    public void showLogin() {
        vBoxShow.getChildren().clear();
    }
}