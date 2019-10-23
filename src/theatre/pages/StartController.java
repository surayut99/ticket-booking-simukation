package theatre.pages;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import theatre.tools.AccountController;
import theatre.tools.DataLoader;
import theatre.tools.EffectController;
import theatre.tools.PageController;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        TranslateTransition transWelcome = EffectController.createTransition(welcomeLabel, 0.5, welcomeLabel.getLayoutX(), 20 - welcomeLabel.getLayoutY());
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
    private void ActionOnLogInButton(ActionEvent event) {
        if (username.getText().equals("") || password.getText().equals(""))  {
            warningLogInMessage.setText("Please fill in your information before logging in.");
            showWarningLogInMessage();
            return;
        }

        String inputUsername = username.getText(), inputPassword = password.getText();
        String line;

        try {
            FileReader reader = new FileReader(new File("data/account_data/accounts.csv"));
            BufferedReader buffer = new BufferedReader(reader);
            line = buffer.readLine();
            while (line != null) {
                ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split(",")));
                if (inputUsername.equals(data.get(0))) {
                    if (inputPassword.equals(data.get(1))) {
                        AccountController.setUsername(inputUsername);
                        if (mainPane.getChildren().size() == 2) {
                            mainPane.getChildren().remove(1);
                        }
                        PageController.stackPages.clear();
                        PageController.stackWaringMessages.clear();
                        loadUserData();
                        mouseClickToContinue(null);
                    }
                    else {
                        warningLogInMessage.setText("Your password is invalid, check and try again.");
                        showWarningLogInMessage();

                    }
                    break;
                }
                line = buffer.readLine();
            }
            if (AccountController.getUsername() == null && line == null) {
                warningLogInMessage.setText("Your username is invalid, check and try again.");
                showWarningLogInMessage();
            }
            buffer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionOnCreateAccount(ActionEvent event) {
        filename = "createAccountScene.fxml";
        loadNextPage(filename);
        root.translateYProperty().set(mainPane.getHeight());
        translateScene(root.translateYProperty());
    }

    private void loadNextPage(String filename) {
        root = null;

        if (PageController.stackPages.isEmpty() || filename.equals("createAccountScene.fxml")) {
            try {
                root = FXMLLoader.load(getClass().getResource(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }

            root.prefWidthProperty().bind(mainPane.widthProperty());
            root.prefHeightProperty().bind(mainPane.heightProperty());
            mainPane.getChildren().add(root);
            PageController.stackPages.push(root);
            PageController.stackWaringMessages.push(null);
        }

        else {
            root = (AnchorPane) PageController.stackPages.firstElement();
            translateScene(root.translateXProperty());
        }
    }

    private void translateScene(DoubleProperty value) {
        Timeline t = EffectController.createTranslateTimeLine(value, 0, 0.5);
        t.setOnFinished(e -> {
            username.setText(null);
            password.setText(null);
        });
        t.play();
    }

    private void loadUserData() {
        try {
            FileReader reader = new FileReader(new File("data/account_data/userdata.csv"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while(line != null) {
                int firstCommaIndex = line.indexOf(',');
                if (line.substring(0, firstCommaIndex).equals(username.getText())) {
                    String[] spittedString = line.split(",");
                    AccountController.setUsername(spittedString[0]);
                    AccountController.setFirstName(spittedString[1]);
                    AccountController.setLastName(spittedString[2]);
                    AccountController.setQuestion(spittedString[3]);
                    AccountController.setAnswer(spittedString[4]);
                    AccountController.setBalance(Double.parseDouble(spittedString[5]));
                    break;
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(AccountController.printData());
    }

    private void showWarningLogInMessage() {
        Transition fadeIn = EffectController.createFadeTransition(warningLogInMessage, 0.5, 1);
        Transition fadeOut = EffectController.createFadeTransition(warningLogInMessage, 0.5, 0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeIn.play();
        fadeOut.play();
    }
}