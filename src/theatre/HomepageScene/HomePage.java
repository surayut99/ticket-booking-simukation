package theatre.HomepageScene;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import theatre.*;
import theatre.MovieDetailScene.MovieDetail;
import theatre.Schedule.Schedule;
import theatre.ShowingSystem.ShowingSystem;
import theatre.movies.ComingSoonMovies;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomePage {
    private Label preLabel;
    private EffectOnObject effectOnObject;
    private Movies selectedMovie;
    private Schedule scheduler;
    private ArrayList<ArrayList<ShowingSystem>> scheduleShowTimes;
    private ArrayList<ArrayList<Movies>> sequenceMovieList;

    private ObservableList<Movies>collectShowingMovies = FXCollections.observableArrayList();
    private ObservableList<Movies>collectComingSoonMovies = FXCollections.observableArrayList();

    private ObservableList<AnchorPane>collectShowingAnchorPane = FXCollections.observableArrayList();
    private ObservableList<ImageView>collectShowingPoster= FXCollections.observableArrayList();
    private ObservableList<Label>collectShowingTitle = FXCollections.observableArrayList();

    private ObservableList<AnchorPane>collectSoonAnchorPane = FXCollections.observableArrayList();
    private ObservableList<ImageView>collectSoonPoster= FXCollections.observableArrayList();
    private ObservableList<Label>collectSoonTitle = FXCollections.observableArrayList();

    @FXML Label showingOption, comingSoonOption, accountOption, logInOption;
    @FXML VBox vBoxShow;
    @FXML AnchorPane fadeStartBackground, showPart, mainPane;

    @FXML public void initialize() {
        vBoxShow.prefWidthProperty().bind(showPart.widthProperty());
        showPart.prefHeightProperty().bind(mainPane.heightProperty());
        vBoxShow.prefHeightProperty().bind(showPart.heightProperty());
        fadeStartBackground.prefHeightProperty().bind(showPart.heightProperty());

        preLabel = showingOption;
        effectOnObject = new EffectOnObject();

        addShowingMovie();
        addComingSoonMovie();
        addShowingMoviesInRow();
        addSoonMoviesInRow();

        addSequenceMovies();
        addScheduleShowTimes();

        FadeTransition fadeBackground = new FadeTransition(Duration.seconds(1), fadeStartBackground);
        fadeBackground.setToValue(0);
        fadeBackground.setDelay(Duration.seconds(0.5));
        fadeBackground.play();
        fadeBackground.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showMoviesOnTheatre();

            }
        });
    }


    @FXML public void mouseEnterOnLabel(MouseEvent event) {
        Label effectedLabel = (Label) event.getSource();

        if(effectedLabel.getStyle().contains("null")) {
            effectOnObject.changeColorOnSelectedLabel(effectedLabel, 0);
        }
    }

    @FXML public void mouseExitOnLabel(MouseEvent event) {
        Label effectedLabel = (Label) event.getSource();
        effectOnObject.changeColorOnSelectedLabel(effectedLabel, 2);
    }

    @FXML public void mouseClickOnLabel(MouseEvent event) {
        Label effectedLabel = (Label) event.getSource();
        if (effectedLabel == preLabel) {return;}

        if (preLabel == null) {preLabel = effectedLabel;}

        preLabel.setStyle("-fx-background-color: null");
        preLabel = effectedLabel;
        effectedLabel.setEffect(effectOnObject.clickAndchangLabelColor());
        effectedLabel.setStyle("-fx-background-color: #1F618D;");

        if (effectedLabel == showingOption || effectedLabel == comingSoonOption) {
            showMoviesOnTheatre();
        }

        else if (effectedLabel == accountOption) {
            showAccount(); //Wait to update
        }

        else {
            showLogin(); //Wait to update
        }
    }

    @FXML public void mouseEnterOnObject(MouseEvent event) {
        Node effectedNode = (Node) event.getSource();
        int index = -1;

        ImageView effectedImage = null;
        Label effectedLabel = null;

        if (effectedNode.getClass() == ImageView.class) {
            if (!showingOption.getStyle().contains("null")) {
                index = collectShowingPoster.indexOf(effectedNode);
                effectedImage = collectShowingPoster.get(index);
                effectedLabel = collectShowingTitle.get(index);
            }

            else {
                index = collectSoonPoster.indexOf(effectedNode);
                effectedImage = collectSoonPoster.get(index);
                effectedLabel = collectSoonTitle.get(index);
            }
        }

        else if (effectedNode.getClass() == Label.class) {
            if (!showingOption.getStyle().contains("null")) {
                index = collectShowingTitle.indexOf(effectedNode);
                effectedImage = collectShowingPoster.get(index);
                effectedLabel = collectShowingTitle.get(index);
            }

            else {
                index = collectSoonTitle.indexOf(effectedNode);
                effectedImage = collectSoonPoster.get(index);
                effectedLabel = collectSoonTitle.get(index);
            }
        }

        effectOnObject.changeScaleOnSelectedNode(effectedLabel, 1.1);
        effectOnObject.changeScaleOnSelectedNode(effectedImage, 1.1);
    }

    @FXML public void mouseExitOffObject(MouseEvent event) {
        Node effectedNode = (Node) event.getSource();
        int index = -1;

        ImageView effectedImage = null;
        Label effectedLabel = null;

        if (effectedNode.getClass() == ImageView.class) {
            if (!showingOption.getStyle().contains("null")) {
                index = collectShowingPoster.indexOf(effectedNode);
                effectedImage = collectShowingPoster.get(index);
                effectedLabel = collectShowingTitle.get(index);
            }

            else {
                index = collectSoonPoster.indexOf(effectedNode);
                effectedImage = collectSoonPoster.get(index);
                effectedLabel = collectSoonTitle.get(index);
            }
        }

        else if (effectedNode.getClass() == Label.class) {
            if (!showingOption.getStyle().contains("null")) {
                index = collectShowingTitle.indexOf(effectedNode);
                effectedImage = collectShowingPoster.get(index);
                effectedLabel = collectShowingTitle.get(index);
            }

            else {
                index = collectSoonTitle.indexOf(effectedNode);
                effectedImage = collectSoonPoster.get(index);
                effectedLabel = collectSoonTitle.get(index);
            }
        }

        effectOnObject.changeScaleOnSelectedNode(effectedLabel, 1);
        effectOnObject.changeScaleOnSelectedNode(effectedImage, 1);
    }


    public void addShowingMovie() {
        LocalDate date = LocalDate.now();

        collectShowingMovies.add(new ShowingMovies("Spider-Man: Homeless", "02:02:02", date, "picture/poster/spider-man-homeless.jpg"));
        collectShowingMovies.add(new ShowingMovies("Boosty and the Beast","01:56:49", date, "picture/poster/booty_and_the_beast.jpg"));
    }

    public void addComingSoonMovie() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("09/09/2020", dateTimeFormatter);

        collectComingSoonMovies.add(new ComingSoonMovies("INCEPTION", "--:--:--", date, "picture/poster/inception.jpg"));
    }

    public void addShowingMoviesInRow() {
        int numMoviesInRow = 0;
        AnchorPane anchorPane = null;

        for (Movies movie : collectShowingMovies) {
            if (numMoviesInRow == 0) {
                anchorPane = createTheatreAnchorPaneToShow();
                collectShowingAnchorPane.add(anchorPane);
            }

            ImageView poster = createTheatrePosterTosShow(numMoviesInRow + 1, movie);
            Label title = createTheatreTitleToShow(movie);
            title.setLayoutX(poster.getLayoutX());

            collectShowingPoster.add(poster);
            collectShowingTitle.add(title);

            anchorPane.getChildren().addAll(poster, title);
            numMoviesInRow++;
            if (numMoviesInRow == 3) {
                numMoviesInRow = 0;
            }
        }
    }

    public void addSoonMoviesInRow() {
        int numMoviesInRow = 0;
        AnchorPane anchorPane = null;

        for (Movies movie : collectComingSoonMovies) {
            if (numMoviesInRow == 0) {
                anchorPane = createTheatreAnchorPaneToShow();
                collectSoonAnchorPane.add(anchorPane);
            }

            ImageView poster = createTheatrePosterTosShow(numMoviesInRow + 1, movie);
            Label title = createTheatreTitleToShow(movie);
            title.setLayoutX(poster.getLayoutX());

            collectSoonPoster.add(poster);
            collectSoonTitle.add(title);

            anchorPane.getChildren().addAll(poster, title);
            numMoviesInRow++;
            if (numMoviesInRow == 3) {
                numMoviesInRow = 0;
            }
        }
    }

    public void addSequenceMovies() {
        sequenceMovieList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            sequenceMovieList.add(new ArrayList<>());
            sequenceMovieList.get(i).addAll(collectShowingMovies);
            sequenceMovieList.get(i).addAll(collectShowingMovies);
            sequenceMovieList.get(i).addAll(collectShowingMovies);
        }
    }

    public void addScheduleShowTimes() {
        scheduler = new Schedule();
        scheduleShowTimes = new ArrayList<>();

        String[] typeSystem = {"Normal", "Normal/4K", "Hybrid/4K", "Hybrid/3D", "Couple/4K"};

        for (int i = 0; i < 5; i++) {
            ArrayList<ShowingSystem> schedule = new ArrayList<>();

            for (Movies movies : sequenceMovieList.get(i)) {
                ShowingSystem system = scheduler.createSchedule(movies, typeSystem[i]);
                system.setLabel(createScheduleLabel(system));

                addSeat(system, i);
                schedule.add(system);
            }
            scheduler.resetTime();
            scheduleShowTimes.add(schedule);
        }
    }

    public void addSeat(ShowingSystem showingSystem, int index) {
        int[] startPrice = {220, 260, 260, 290, 300};
        int[][] numSeat = {{10, 10}, {10, 15}, {8, 15}, {8, 10}, {8, 8}};

        showingSystem.generateSeat(numSeat[index][0], numSeat[index][1], startPrice[index]);
    }


    public Label createScheduleLabel(ShowingSystem showingSystem) {
        Label label = new Label(showingSystem.getTimeStart());
        label.setPrefWidth(100);
        label.setPrefHeight(40);

        label.setFont(Font.font("System", FontWeight.BOLD, 24));
        label.setTextFill(Color.BLACK);
        label.setAlignment(Pos.CENTER);
        label.setCursor(Cursor.HAND);

        label.setStyle("-fx-background-color: linear-gradient(#FDC830,#F37335);\n" +
                "    -fx-background-radius: 25;\n" +
                "    -fx-background-insets: 0;");

        return label;
    }

    public AnchorPane createTheatreAnchorPaneToShow() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(889);
        anchorPane.setPrefHeight(390);
        anchorPane.prefWidthProperty().bind(showPart.widthProperty());
        return anchorPane;
    }

    public ImageView createTheatrePosterTosShow(int numMoviesInRow, Movies movies) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(21);
        dropShadow.setHeight(21);
        dropShadow.setRadius(10);

        ImageView imageView = new ImageView(movies.getLocationPoster());
        imageView.setFitWidth(214);
        imageView.setFitHeight(284);
        imageView.setLayoutY(30);
        imageView.setEffect(dropShadow);

        if (numMoviesInRow == 1) {
            imageView.setLayoutX(50);
        }
        else if (numMoviesInRow == 2) {
            imageView.setLayoutX(342);
        }
        else {
            imageView.setLayoutX(634);
        }
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseEntered(this::mouseEnterOnObject);
        imageView.setOnMouseExited(this::mouseExitOffObject);
        imageView.setOnMouseClicked(this::showMovieDetail);

        return imageView;
    }

    public Label createTheatreTitleToShow(Movies movies) { // create title label to show movies title and set option of this node.
        Label titleLabel = new Label(movies.getTitle());
        titleLabel.setCursor(Cursor.HAND);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setLayoutY(322);
        titleLabel.setPrefWidth(204);
        titleLabel.setPrefHeight(40);

        titleLabel.setOnMouseEntered(this::mouseEnterOnObject);
        titleLabel.setOnMouseExited(this::mouseExitOffObject);
        titleLabel.setOnMouseClicked(this::showMovieDetail);

        return titleLabel;
    }


    public void showMoviesOnTheatre() {

        ObservableList<AnchorPane> list = null;

        if (!showingOption.getStyle().contains("null")) { // check clicked label is showing or coming-soon.
            list = collectShowingAnchorPane;
        }
        else {
            list = collectSoonAnchorPane;
        }

        vBoxShow.getChildren().clear(); // clear every node in vBox before show movie list.

        for (AnchorPane anchorPane : list) {
            anchorPane.setOpacity(0);
            vBoxShow.getChildren().add(anchorPane); // add movies on movies list.
            effectOnObject.fadeMoviesInRow(anchorPane); // fade row of list in.
        }
    }

    public void showMovieDetail(MouseEvent event) {
        AnchorPane newPane = createTheatreAnchorPaneToShow(); // Create new anchorpane to show detail movie scene.
        vBoxShow.getChildren().clear(); // Clear every node in vBox .
        vBoxShow.getChildren().add(newPane); // Add new anchorpane to vBox.

        Node selectedNode = (Node) event.getSource(); // Get node was clicked.
        checkSelectedObject(selectedNode); // check node is from which object movie.

        // Load detail movies scene after click on node.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../MovieDetailScene/movieDetail.fxml"));
            Parent root = loader.load();

            MovieDetail detailController = loader.getController();
            detailController.setSelectedMovie(this.selectedMovie);
            detailController.setScheduleShowtimes(scheduleShowTimes);
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


    public void checkSelectedObject(Node node){
        Movies selectedMovie = null;

        if (node.getClass() == ImageView.class) { // check if node is instance of ImageView or Label
            ImageView selectedPoster = (ImageView) node;
            if (!showingOption.getStyle().contains("null")) { // find index in arrayList and get index of movie object in arrayList.
                int index = collectShowingPoster.indexOf(selectedPoster);
                selectedMovie = collectShowingMovies.get(index);
            }
            else {
                int index = collectSoonPoster.indexOf(selectedPoster);
                selectedMovie = collectComingSoonMovies.get(index);
            }
        }
        else {
            Label selectedTitle = (Label) node;
            {
                if (!showingOption.getStyle().contains("null")) {
                    int index = collectShowingTitle.indexOf(selectedTitle);
                    selectedMovie = collectShowingMovies.get(index);
                }
                else {
                    int index = collectSoonTitle.indexOf(selectedTitle);
                    selectedMovie = collectComingSoonMovies.get(index);
                }
            }
        }

        // reset color on label after click on movies node.

        preLabel.setStyle("-fx-background-color: null;");
        preLabel = null;
        this.selectedMovie = selectedMovie;
    }
}