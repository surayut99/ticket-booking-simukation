package theatre.scenes;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import theatre.tools.*;

import java.io.*;

public class StartController {
    private String filename;
    private AnchorPane root;

    @FXML
    Label clickToContinue, welcomeLabel, sampleText;
    @FXML
    AnchorPane mainPane, showPane;
    @FXML
    GridPane gridLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Text warningLogInMessage;

    @FXML
    public void initialize() {
        showPane.prefWidthProperty().bind(mainPane.widthProperty());
        showPane.prefHeightProperty().bind(mainPane.heightProperty());

        FadeTransition fadeAnchor = EffectController.createFadeTransition(showPane, 0.5, 1);
        fadeAnchor.setDelay(Duration.seconds(1));
        FadeTransition fadeWelcome = EffectController.createFadeTransition(welcomeLabel, 0.5, 1);
        FadeTransition fadeSample = EffectController.createFadeTransition(sampleText, 0.5, 1);
        FadeTransition fadeClick = EffectController.createFadeTransition(clickToContinue, 1, 1);
        FadeTransition fadeGrid = EffectController.createFadeTransition(gridLogin, 0.5, 1);
        fadeGrid.setDelay(Duration.seconds(1));

        TranslateTransition transWelcome = EffectController.createTransition(welcomeLabel, 0.5, welcomeLabel.getLayoutX(), 25 - welcomeLabel.getLayoutY());
        TranslateTransition transName = EffectController.createTransition(sampleText, 0.5, sampleText.getLayoutX(), 100 - sampleText.getLayoutY());

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(transName, transWelcome, fadeGrid);

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(fadeAnchor, fadeWelcome, fadeSample, parallelTransition);
        sequentialTransition.play();
        sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clickToContinue.setDisable(false);
                gridLogin.setDisable(false);
                fadeClick.play();
                DataController.loadUserData();
                DataController.loadShowingSystem();
                DataController.loadReservingData();
            }
        });
    }

    @FXML
    public void mouseClickToContinue(MouseEvent event) {
        filename = "homepageScene.fxml";
        loadNextPage(filename);
        root.translateXProperty().set(mainPane.getWidth());
        translateScene(root.translateXProperty());
    }

    @FXML
    private void actionOnLogInButton(ActionEvent event) {
        String inputUsername = username.getText(), inputPassword = password.getText();

        if (inputUsername.equals("") || inputPassword.equals("")) {
            warningLogInMessage.setText("Please fill in your information before logging in.");
            showWarningLogInMessage();
            return;
        }

        try {
            AccountCollector.login(inputUsername, inputPassword);
            mouseClickToContinue(null);
        } catch (IllegalArgumentException e) {
            warningLogInMessage.setText(e.getMessage());
            showWarningLogInMessage();
        }
    }

    @FXML
    private void actionOnCreateAccount(ActionEvent event) {
        filename = "createAccountScene.fxml";
        loadNextPage(filename);
        root.translateYProperty().set(mainPane.getHeight());
        translateScene(root.translateYProperty());
    }

    private void loadNextPage(String filename) {
        root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.getChildren().add(root);
        root.prefWidthProperty().bind(mainPane.widthProperty());
        root.prefHeightProperty().bind(mainPane.heightProperty());
        PageController.getStackPages().push(root);
        PageController.getStackWaringMessages().push(null);
    }

    private void translateScene(DoubleProperty value) {
        Timeline t = EffectController.createTranslateTimeLine(value, 0, 0.5);
        t.setOnFinished(e -> {
            username.setText(null);
            password.setText(null);
        });
        t.play();
    }

    private void showWarningLogInMessage() {
        Transition fadeIn = EffectController.createFadeTransition(warningLogInMessage, 0.5, 1);
        Transition fadeOut = EffectController.createFadeTransition(warningLogInMessage, 0.5, 0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeIn.play();
        fadeOut.play();
    }
}