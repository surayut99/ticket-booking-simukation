package theatre.pages;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import theatre.movies.ComingSoonMovies;
import theatre.movies.MovieCollector;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.seat.SeatManager;
import theatre.seat.Seat;
import theatre.showingSystem.*;
import theatre.tools.DataLoader;
import theatre.tools.EffectController;
import theatre.tools.NodeCreator;
import theatre.tools.PageController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomepageController {
    private Label homepageLabel;
    private Movies selectedMovie;
    private String styleLabel;
    private VBox vBoxShow;
    private ArrayList<AnchorPane> showingMockUp, soonMockUp;

    @FXML Label showingOption, comingSoonOption, accountOption, logInOption;
    @FXML AnchorPane contentPart, mainPane, showContent;
    @FXML Button backButton;

    @FXML public void initialize() {
        //Bind nodes'size with windows's size
        contentPart.prefHeightProperty().bind(mainPane.heightProperty());
        showContent.prefWidthProperty().bind(contentPart.widthProperty());
        showContent.prefHeightProperty().bind(contentPart.heightProperty());

        homepageLabel = showingOption;
//        preOption = showingOption;
        styleLabel = showingOption.getStyle();
        FadeTransition fadeBackground = EffectController.createFadeTransition(showContent.getChildren().get(0), 0.5, 0);
        fadeBackground.play();
        fadeBackground.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showContent.getChildren().remove(0);
                vBoxShow = new VBox(10);
                vBoxShow.setPadding(new Insets(10,10,10,10));
                vBoxShow.setOpacity(0);
                NodeCreator.setAlignmentNodeOnAnchorPane(vBoxShow, 0., 0., 0., 0.);
                PageController.mainShowContent = showContent;
                PageController.backButton = backButton;
                PageController.backButton.setOpacity(0);
                PageController.lastHomePage = showingOption;
                addMovies();
                addMockUp();
                showMockUp();
                addShowingSystem();
                addScheduleShowTime();
                DataLoader.loadReservingData();
            }
        });
    }

    @FXML public void mouseEnterOnNode(MouseEvent event) {
        Node enterOnObject = (Node) event.getSource();

        if (enterOnObject.getClass() == Label.class) {
            if(enterOnObject.getStyle().contains("null")) {
                EffectController.changeColorOnSelectedLabel((Label) enterOnObject, 0);
            }
        }

        else if (enterOnObject.getClass() == AnchorPane.class) {
            AnchorPane enterAnchor = (AnchorPane) enterOnObject;
            for (int i = 0; i < enterAnchor.getChildren().size(); i++) {
                EffectController.changeScaleOnSelectedNode(enterAnchor.getChildren().get(i), 1.1);
            }
        }
    }

    @FXML public void mouseExitOffNode(MouseEvent event) {
        Node enterOnObject = (Node) event.getSource();

        if (enterOnObject.getClass() == Label.class) {
            if(enterOnObject.getStyle().contains("null")) {
                EffectController.changeColorOnSelectedLabel((Label) enterOnObject, 2);
            }
        }

        else if (enterOnObject.getClass() == AnchorPane.class) {
            AnchorPane enterAnchor = (AnchorPane) enterOnObject;
            for (int i = 0; i < enterAnchor.getChildren().size(); i++) {
                EffectController.changeScaleOnSelectedNode(enterAnchor.getChildren().get(i), 1.0);
            }
        }
    }

    @FXML public void mouseClickOnHomepage(MouseEvent event) {
        if (PageController.stackWaringMessages.peek() != null) {
            homepageLabel = (Label) event.getSource();
            AnchorPane message  = PageController.stackWaringMessages.peek();
            ((Button)message.getChildren().get(3)).setOnAction(this::ActionOnChoiceBackToHome);
            ((Button)message.getChildren().get(4)).setOnAction(this::ActionOnChoiceBackToHome);
            ((AnchorPane) PageController.stackPages.firstElement()).getChildren().add(PageController.stackWaringMessages.peek());
            showContent.getChildren().get(0).setEffect(new ColorAdjust(0,0,-0.5,0));
            PageController.lastHomePage = (Label) event.getSource();
            return;
        }


        //if selected option is the same, do nothing.
        if (((Label) event.getSource()).getStyle().equals(styleLabel)) return;
        PageController.goHomepage();
        //else: set previous option style to be null.
        homepageLabel.setStyle("-fx-background-color: null;");
        homepageLabel = (Label) event.getSource();
        homepageLabel.setStyle(styleLabel);
        homepageLabel.setEffect(EffectController.clickAndChangLabelColor());
        showMockUp();
    }

    @FXML public void mouseClickOnPoster(MouseEvent event) {
        AnchorPane clickMockUp = (AnchorPane) event.getSource();
        Label movieTitle = (Label) clickMockUp.getChildren().get(1);
        selectedMovie = MovieCollector.findMovie(movieTitle.getText());
        homepageLabel.setStyle("-fx-background-color: null;");
        showMovieDetail();
    }

    @FXML public void ActionOnBackButton(ActionEvent event) {
        if (PageController.stackPages.size() > 1) {
            if (PageController.stackWaringMessages.peek() == null) PageController.back();
            else {
                PageController.back();
                AnchorPane message = PageController.stackWaringMessages.peek();
                ((Button) message.getChildren().get(3)).setOnAction(this::ActionOnChoiceBackPage);
                ((Button) message.getChildren().get(4)).setOnAction(this::ActionOnChoiceBackPage);
                return;
            }
        }
        if (PageController.stackPages.size() == 2) { homepageLabel.setStyle(styleLabel); }
    }

    @FXML public void ActionOnChoiceBackToHome(ActionEvent event) {
        String choice = ((Button) event.getSource()).getText();
        switch (choice) {
            case "Yes":
                PageController.goHomepage();
                showContent.setEffect(null);
                homepageLabel.setStyle("-fx-background-color: null;");
                PageController.lastHomePage.setStyle(styleLabel);
                ((AnchorPane) PageController.stackPages.firstElement()).getChildren().remove(1);
                showMockUp();
                break;

            case "No":
                showContent.getChildren().get(0).setEffect(null);
                ((AnchorPane) PageController.stackPages.firstElement()).getChildren().remove(1);
                break;
        }
    }

    @FXML public void ActionOnChoiceBackPage(ActionEvent event) {
        String choice = ((Button) event.getSource()).getText();
        switch (choice) {
            case "Yes":
                PageController.stackWaringMessages.pop();
                PageController.stackWaringMessages.push(null);
                ((AnchorPane) PageController.stackPages.firstElement()).getChildren().remove(1);
                ActionOnBackButton(event);
                break;
            case "No":
                ((AnchorPane) PageController.stackPages.firstElement()).getChildren().remove(1);
                PageController.stackPages.peek().setEffect(null);
                break;
        }
    }

    //add movie object to MoviesCollector class
    private void addMovies() {
        if (!MovieCollector.moviesList.isEmpty()) return;

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

    //create Mock up as Anchorpane which contains image seat and position seat label and collect as ArrayList.
    private void addMockUp() {
        showingMockUp = MovieCollector.generateMockUp(ShowingMovies.class);
        soonMockUp = MovieCollector.generateMockUp(ComingSoonMovies.class);
    }

    //take each element in ArrayList, contains mock up, to show on screen.
    private void showMockUp() {
        FadeTransition fade = EffectController.createFadeTransition(vBoxShow, 0.125, 0);
        fade.play();
        fade.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<AnchorPane> mockUpList = showingOption.getStyle().contains("null") ?
                        soonMockUp : showingMockUp;

                showContent.getChildren().clear();
                showContent.getChildren().add(vBoxShow);
                vBoxShow.getChildren().clear();
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
                    mockUp.setOnMouseClicked(HomepageController.this::mouseClickOnPoster);
                    mockUp.setOnMouseEntered(HomepageController.this::mouseEnterOnNode);
                    mockUp.setOnMouseExited(HomepageController.this::mouseExitOffNode);
                    numMockUp++;
                }

                EffectController.createFadeTransition(vBoxShow, 0.125, 1).play();
                PageController.stackPages.push(showContent.getChildren().get(0));
                PageController.stackWaringMessages.push(null);
            }
        });
    }

    //create and add showing system object to ShowingSystemCollector.
    private void addShowingSystem() {
        String[] systemTypes = {"Normal", "4K", "4K", "3D", "3D"};
        String[] patternSeat = {"Normal", "Normal", "Hybrid", "Hybrid", "Hybrid"};
        int[][] numSeat = {{5, 8}, {8, 10}, {10, 12}, {8, 10}, {8, 8}};

        for (int i = 0; i < 5; i++) {
            Seat[][] seats = SeatManager.createSeatArray(patternSeat[i], numSeat[i][0], numSeat[0][1], 200);
            ShowingSystemCollector.addShowingSystem(i, 20, systemTypes[i], seats);
        }
    }

    //create and add schedule for each showing system.
    private void addScheduleShowTime() {
        Movies spiderman = MovieCollector.findMovie("spider");
        Movies boosty = MovieCollector.findMovie("boosty");
        Movies deadpool = MovieCollector.findMovie("deadpool");

        ShowingSystemCollector.addScheduleMovies(0, spiderman, spiderman, boosty, deadpool, boosty, deadpool, deadpool);
        ShowingSystemCollector.addScheduleMovies(1, deadpool, boosty, boosty, spiderman, deadpool, spiderman);
        ShowingSystemCollector.addScheduleMovies(2, boosty, spiderman, deadpool, deadpool, boosty, boosty);
        ShowingSystemCollector.addScheduleMovies(3, deadpool, spiderman, deadpool, spiderman, boosty, boosty);
        ShowingSystemCollector.addScheduleMovies(4, spiderman, boosty, spiderman, boosty, deadpool, deadpool);

    }

    //resume after mouseClickOnNode() actives, create new pane and load data from .fxml file to show in next page.
    private void showMovieDetail() {
        EffectController.createFadeTransition(PageController.peakPage(), 0.5, 0).play();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("movieDetailScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MovieDetailController movieDetailController = loader.getController();
        movieDetailController.setSelectedMovie(selectedMovie);
        movieDetailController.setShowingSystems(ShowingSystemCollector.showingSystems);
        movieDetailController.setSceneSize(mainPane.widthProperty());
        showContent.getChildren().clear();
        showContent.getChildren().add(root);
        PageController.addPage(root);
        PageController.backButton.setDisable(false);
        EffectController.createFadeTransition(PageController.backButton, 0.5,1).play();

        root.setOpacity(0);
        Timeline showDetailScene = new Timeline();
        KeyValue keyValue = new KeyValue(root.opacityProperty(), 1, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        showDetailScene.getKeyFrames().add(keyFrame);
        showDetailScene.play();
    }

    //show show own user account detail.
    private void showAccount() {
        showContent.getChildren().clear();
        PageController.stackPages.clear();
    }

    //show login page if user have not logged in yet.
    @FXML private void showLogin(MouseEvent event) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(mainPane.translateXProperty(), 1280, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}