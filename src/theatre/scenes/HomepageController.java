package theatre.scenes;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import theatre.movies.ComingSoonMovies;
import theatre.movies.MovieCollector;
import theatre.movies.Movies;
import theatre.movies.ShowingMovies;
import theatre.showingSystem.*;
import theatre.tools.*;

import java.io.IOException;
import java.util.ArrayList;

public class HomepageController {
    private Label homepageLabel;
    private Movies selectedMovie;
    private String styleLabel;
    private VBox vBoxShow;
    private ArrayList<AnchorPane> showingMockUp, soonMockUp;

    @FXML
    Label showingOption, accountOption, registerOption;
    @FXML
    AnchorPane contentPart, mainPane;
    @FXML
    Button backButton;
    @FXML
    ScrollPane contentCollector;

    @FXML
    public void initialize() {
        //Bind nodes'size with windows's size
        contentPart.prefHeightProperty().bind(mainPane.heightProperty());
        contentCollector.prefWidthProperty().bind(contentPart.widthProperty());
        contentCollector.prefHeightProperty().bind(contentPart.heightProperty());

        homepageLabel = showingOption;
        if (AccountCollector.getCurrentAccount() == null) {
            accountOption.setVisible(false);
            accountOption.setDisable(true);
            registerOption.setText("LOG IN");
        } else {
            accountOption.setVisible(true);
            accountOption.setDisable(false);
            registerOption.setText("LOG OUT");
        }

        styleLabel = showingOption.getStyle();
        FadeTransition fadeBackground = EffectController.createFadeTransition(contentCollector, 0.75, 1);
        fadeBackground.play();
        fadeBackground.setOnFinished(e -> {
            vBoxShow = new VBox(10);
            vBoxShow.setPadding(new Insets(10, 10, 10, 10));
            vBoxShow.setOpacity(0);
            NodeCreator.setAlignmentNodeOnAnchorPane(vBoxShow, 0., 0., 0., 0.);
            PageController.setMainShowContent(contentCollector);
            PageController.setBackButton(backButton);
            PageController.setLastHomePage(homepageLabel);
            addMockUp();
            showMockUp();
//            printSchedule();
        });
    }

    public void printSchedule() {
        ShowingSystem[] systems = ShowingSystemCollector.getShowingSystems();
        for (int i = 0; i < systems.length; i++) {
            System.out.println("Theatre: " + i);
            ArrayList<Schedule> schedules = systems[i].getSchedules();
            for (Schedule s : schedules) {
                System.out.println("\t" + s.getStartTime());
                System.out.println("\t" + s.getReservedPositionSeat());
            }
        }
    }

    @FXML
    public void actionOnBackButton(ActionEvent event) {
        if (PageController.getStackPages().size() > 1) {
            if (PageController.getStackWaringMessages().peek() == null) PageController.back();
            else {
                PageController.back();
                AnchorPane message = PageController.getStackWaringMessages().peek();
                ((Button) message.getChildren().get(3)).setOnAction(this::actionOnChoiceBackPage);
                ((Button) message.getChildren().get(4)).setOnAction(this::actionOnChoiceBackPage);
                return;
            }
        }
        if (PageController.getStackPages().size() == 2) {
            homepageLabel.setStyle(styleLabel);
        }
    }

    @FXML
    private void actionOnChoiceBackToHome(ActionEvent event) {
        Button button = (Button) event.getSource();
        WarningController.hideWarning();
        if (button.getText().equals("Yes")) {
            PageController.goHomepage();
            homepageLabel.setStyle("-fx-background-color: null;");
            PageController.getLastHomePage().setStyle(styleLabel);
            showMockUp();
        }
    }

    @FXML
    private void actionOnChoiceBackPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        WarningController.hideWarning();
        if (button.getText().equals("Yes")) {
            PageController.getStackWaringMessages().pop();
            PageController.getStackWaringMessages().push(null);
            actionOnBackButton(null);
        }
    }

    @FXML
    private void actionOnChoiceLogout(ActionEvent event) {
        String choice = ((Button) event.getSource()).getText();
        WarningController.hideWarning();
        if (choice.equals("Yes")) translateRegister();
    }

    @FXML
    public void mouseEnterOnNode(MouseEvent event) {
        Node enterOnObject = (Node) event.getSource();

        if (enterOnObject.getClass() == Label.class) {
            if (enterOnObject.getStyle().contains("null")) {
                EffectController.changeColorOnSelectedLabel((Label) enterOnObject, 0);
            }
        } else if (enterOnObject.getClass() == AnchorPane.class) {
            AnchorPane enterAnchor = (AnchorPane) enterOnObject;
            for (int i = 0; i < enterAnchor.getChildren().size(); i++) {
                EffectController.changeScaleOnSelectedNode(enterAnchor.getChildren().get(i), 1.1);
            }
        }
    }

    @FXML
    public void mouseExitOffNode(MouseEvent event) {
        Node enterOnObject = (Node) event.getSource();

        if (enterOnObject.getClass() == Label.class) {
            if (enterOnObject.getStyle().contains("null")) {
                EffectController.changeColorOnSelectedLabel((Label) enterOnObject, 2);
            }
        } else if (enterOnObject.getClass() == AnchorPane.class) {
            AnchorPane enterAnchor = (AnchorPane) enterOnObject;
            for (int i = 0; i < enterAnchor.getChildren().size(); i++) {
                EffectController.changeScaleOnSelectedNode(enterAnchor.getChildren().get(i), 1.0);
            }
        }
    }

    @FXML
    public void mouseClickOnHomepage(MouseEvent event) {
        if (PageController.hasWarning()) {
            String topic = "You are going to homepage.";
            String description = "If you go homepage, some data you did will lose.";
            AnchorPane warning = NodeCreator.createWarningAnchorPane(this::actionOnChoiceBackToHome, topic, description);
            WarningController.showWarning(warning);
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

    @FXML
    private void mouseClickOnPoster(MouseEvent event) {
        AnchorPane clickMockUp = (AnchorPane) event.getSource();
        Label movieTitle = (Label) clickMockUp.getChildren().get(1);
        selectedMovie = MovieCollector.findMovie(movieTitle.getText());
        homepageLabel.setStyle("-fx-background-color: null;\n" +
                "-fx-background-radius: 50;"
        );
        showMovieDetail();
    }

    //show show own user account detail.
    @FXML
    private void mouseClickOnAccount(MouseEvent event) {
        try {
            AnchorPane parent = (AnchorPane) PageController.getStackPages().firstElement();
            ScrollPane accountPane = FXMLLoader.load(getClass().getResource("/theatre/scenes/accountScene.fxml"));
            accountPane.prefHeightProperty().bind(parent.prefHeightProperty());
            accountPane.prefWidthProperty().bind(parent.prefWidthProperty());
            accountPane.translateYProperty().set(parent.getHeight());
            parent.getChildren().add(accountPane);
            Timeline timeline = EffectController.createTranslateTimeLine(accountPane.translateYProperty(), 0, 0.25);
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //show login page if user have not logged in yet.
    @FXML
    private void mouseClickOnRegisterOption(MouseEvent event) {
        if (PageController.hasWarning() || PageController.getStackPages().size() > 2) {
            String topic = "You are going to exit from this page";
            String description = "if you exit from this page, some data you did may lose.";
            AnchorPane warning = NodeCreator.createWarningAnchorPane(this::actionOnChoiceLogout, topic, description);
            WarningController.showWarning(warning);
            return;
        }
        translateRegister();
    }


    //create Mock up as Anchorpane which contains image seat and position seat label and collect as ArrayList.
    private void addMockUp() {
        showingMockUp = MovieCollector.generateMockUp(ShowingMovies.class);
        soonMockUp = MovieCollector.generateMockUp(ComingSoonMovies.class);
    }

    //take each element in ArrayList, contains mock up, to show on screen.
    private void showMockUp() {
        FadeTransition fade = EffectController.createFadeTransition(vBoxShow, 0.125, 0);
        if (PageController.getStackPages().size() == 2) {
            PageController.getStackPages().pop();
            PageController.getStackWaringMessages().pop();
        }
        fade.play();
        fade.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<AnchorPane> mockUpList = showingOption.getStyle().contains("null") ?
                        soonMockUp : showingMockUp;
                contentCollector.setContent(vBoxShow);
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
                PageController.getStackPages().push(contentCollector.getContent());
                PageController.getStackWaringMessages().push(null);
            }
        });
    }

    //resume after mouseClickOnNode() actives, create new pane and load data from .fxml file to show in next page.
    private void showMovieDetail() {
        EffectController.createFadeTransition(PageController.peakPage(), 0.5, 0).play();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("movieDetailScene.fxml"));
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MovieDetailController movieDetailController = loader.getController();
        movieDetailController.setSelectedMovie(selectedMovie);
        root.prefWidthProperty().bind(contentCollector.prefWidthProperty());
        contentCollector.setContent(root);
        PageController.getStackPages().push(root);
        PageController.getBackButton().setDisable(false);
        EffectController.createFadeTransition(PageController.getBackButton(), 0.5, 1).play();

        root.setOpacity(0);
        Timeline showDetailScene = new Timeline();
        KeyValue keyValue = new KeyValue(root.opacityProperty(), 1, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        showDetailScene.getKeyFrames().add(keyFrame);
        showDetailScene.play();
    }

    private void translateRegister() {
        if (AccountCollector.getCurrentAccount() != null) AccountCollector.logout();

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(mainPane.translateXProperty(), mainPane.getWidth(), Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(e -> {
            registerOption.setText("LOG IN");
            AnchorPane parent = (AnchorPane) PageController.getStackPages().firstElement().getParent();
            parent.getChildren().remove(PageController.getStackPages().firstElement());
            PageController.getStackPages().clear();
        });
        timeline.play();
    }
}
