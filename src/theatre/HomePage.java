package theatre;

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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomePage {
    private Label preLabel;
    private EffectOnObject effectOnObject;
    private ObservableList<Label> labelList = FXCollections.observableArrayList();
    private Movies selectedMovie;

    private ObservableList<Movies>collectShowingMovies = FXCollections.observableArrayList();
    private ObservableList<Movies>collectComingSoonMovies = FXCollections.observableArrayList();

    private ObservableList<AnchorPane>collectShowingAnchorPane = FXCollections.observableArrayList();
    private ObservableList<ImageView>collectShowingPoster= FXCollections.observableArrayList();
    private ObservableList<Label>collectShowingTitle = FXCollections.observableArrayList();

    private ObservableList<AnchorPane>collectSoonAnchorPane = FXCollections.observableArrayList();
    private ObservableList<ImageView>collectSoonPoster= FXCollections.observableArrayList();
    private ObservableList<Label>collectSoonTitle = FXCollections.observableArrayList();

    @FXML Label showingOption, comingSoonOption, accountOption, logInOption;
    @FXML VBox vBoxshow;
    @FXML AnchorPane fadeStartBackground, showPart, mainPane;

    @FXML public void initialize() {
        vBoxshow.prefWidthProperty().bind(showPart.widthProperty());
        showPart.prefHeightProperty().bind(mainPane.heightProperty());
        vBoxshow.prefHeightProperty().bind(showPart.heightProperty());
        fadeStartBackground.prefHeightProperty().bind(showPart.heightProperty());

        labelList.addAll(showingOption, comingSoonOption, accountOption, logInOption);
        preLabel = showingOption;
        effectOnObject = new EffectOnObject();

        addShowingMovie();
        addComingSoonMovie();
        addShowingMoviesInRow();
        addSoonMoviesInRow();

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

        if (effectedLabel != preLabel && preLabel != null) {
            preLabel.setStyle("-fx-background-color: null");
            effectedLabel.setEffect(effectOnObject.clickAndchangLabelColor());
            effectedLabel.setStyle("-fx-background-color: #1F618D;");
            preLabel = effectedLabel;
        }

        else if (preLabel == null) {
            preLabel = effectedLabel;
            effectedLabel.setEffect(effectOnObject.clickAndchangLabelColor());
            effectedLabel.setStyle("-fx-background-color: #1F618D;");
        }

        if (effectedLabel == showingOption || effectedLabel == comingSoonOption) {
            showMoviesOnTheatre();
        }

        else if (effectedLabel == accountOption) {
            showAccount();
        }

        else {
            showLogin();
        }

        preLabel = effectedLabel;
    }

    @FXML public void mouseEnterOnPoster(MouseEvent event) {
        ImageView effectedPoster = (ImageView) event.getSource();
        int index = -1;
        ObservableList<Label> titleList = null;
        if (!showingOption.getStyle().contains("null")) {
            index = collectShowingPoster.indexOf(effectedPoster);
            titleList = collectShowingTitle;
        }
        else {
            index = collectSoonPoster.indexOf(effectedPoster);
            titleList = collectSoonTitle;
        }

        effectOnObject.changeScaleOnSelectedNode(titleList.get(index), 1.1);
        effectOnObject.changeScaleOnSelectedNode(effectedPoster, 1.1);
    }

    @FXML public void mouseExitOffPoster(MouseEvent event) {
        ImageView effectedPoster = (ImageView) event.getSource();
        int index = -1;
        ObservableList<Label> titleList = null;
        if (!showingOption.getStyle().contains("null")) {
            index = collectShowingPoster.indexOf(effectedPoster);
            titleList = collectShowingTitle;
        }
        else {
            index = collectSoonPoster.indexOf(effectedPoster);
            titleList = collectSoonTitle;
        }

        effectOnObject.changeScaleOnSelectedNode(titleList.get(index), 1);
        effectOnObject.changeScaleOnSelectedNode(effectedPoster, 1);
    }

    @FXML public void mouseEnterOnTitle(MouseEvent event) {
        Label effectedTitle = (Label) event.getSource();
        int index = -1;
        ObservableList<ImageView> posterList = null;
        if (!showingOption.getStyle().contains("null")) {
            index = collectShowingTitle.indexOf(effectedTitle);
            posterList = collectShowingPoster;
        }
        else {
            index = collectSoonTitle.indexOf(effectedTitle);
            posterList = collectSoonPoster;
        }

        effectOnObject.changeScaleOnSelectedNode(posterList.get(index), 1.1);
        effectOnObject.changeScaleOnSelectedNode(effectedTitle, 1.1);
    }

    @FXML public void mouseExitOffTitle(MouseEvent event) {
        Label effectedTitle = (Label) event.getSource();

        int index = -1;
        ObservableList<ImageView> posterList = null;
        if (!showingOption.getStyle().contains("null")) {
            index = collectShowingTitle.indexOf(effectedTitle);
            posterList = collectShowingPoster;
        }
        else {
            index = collectSoonTitle.indexOf(effectedTitle);
            posterList = collectSoonPoster;
        }

        effectOnObject.changeScaleOnSelectedNode(posterList.get(index), 1);
        effectOnObject.changeScaleOnSelectedNode(effectedTitle, 1);
    }


    public void addShowingMovie() {
        LocalDate date = LocalDate.now();
        String comingDate = "09/09/2019";

        collectShowingMovies.add(new ShowingMovies("Spider-Man: Homeless", "02:02:02", date, "picture/poster/spider-man-homeless.jpg"));
        collectShowingMovies.add(new ShowingMovies("Boosty and the Beast","01:56:49", date, "picture/poster/booty_and_the_beast.jpg"));
    }

    public void addComingSoonMovie() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("09/09/2019", dateTimeFormatter);

        collectComingSoonMovies.add(new ComingSoonMovies("INCEPTION", "01:55:55", date, "picture/poster/inception.jpg"));
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
        imageView.setOnMouseEntered(this::mouseEnterOnPoster);
        imageView.setOnMouseExited(this::mouseExitOffPoster);
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

        titleLabel.setOnMouseEntered(this::mouseEnterOnTitle);
        titleLabel.setOnMouseExited(this::mouseExitOffTitle);
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

        vBoxshow.getChildren().clear(); // clear every node in vBox before show movie list.
        //vBoxshow.setPrefHeight(0); // reset vBox pre-height.

        for (AnchorPane anchorPane : list) {
            anchorPane.setOpacity(0);
            vBoxshow.getChildren().add(anchorPane); // add movies on movies list.
            //vBoxshow.setPrefHeight(vBoxshow.getPrefHeight() + anchorPane.getPrefHeight()); // resize vBox pre-height after adding.
            effectOnObject.fadeMoviesInRow(anchorPane); // fade row of list in.
        }
    }

    public void showMovieDetail(MouseEvent event) {
        AnchorPane newPane = createTheatreAnchorPaneToShow(); // Create new anchorpane to show detail movie scene.
        vBoxshow.getChildren().clear(); // Clear every node in vBox .
        vBoxshow.getChildren().add(newPane); // Add new anchorpane to vBox.

        Node selectedNode = (Node) event.getSource(); // Get node was clicked.
        checkSelectedObject(selectedNode); // check node is from which object movie.

        // Load detail movies scene after click on node.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movieDetail.fxml"));
            Parent root = loader.load();

            MovieDetail detailController = loader.getController();
            detailController.setSelectedMovie(this.selectedMovie);
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
        vBoxshow.getChildren().clear();
    }

    public void showLogin() {
        vBoxshow.getChildren().clear();
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