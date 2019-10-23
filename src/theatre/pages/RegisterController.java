package theatre.pages;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import theatre.tools.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RegisterController {
    @FXML private Button backButton;
    @FXML private GridPane gridInput;
    @FXML private AnchorPane showContent;
    @FXML private Text warningMeassage;

    @FXML private void initialize() {
        backButton.setOnAction(this::ActionOnBackButton);
    }

    @FXML private void ActionOnBackButton(ActionEvent event) {
        isFilledBlank();
        AnchorPane thisPage = (AnchorPane) PageController.stackPages.peek();
        AnchorPane peekMessage = PageController.stackWaringMessages.peek();
        if (peekMessage == null) { backFunction();}
        else {
            thisPage.getChildren().add(peekMessage);
            showContent.setDisable(true);
            showContent.setEffect(new ColorAdjust(0, 0, -0.5, 0));
            ((Button) peekMessage.getChildren().get(3)).setOnAction(this::ActionOnChoiceWarning);
            ((Button) peekMessage.getChildren().get(4)).setOnAction(this::ActionOnChoiceWarning);
        }
    }

    @FXML private void ActionOnChoiceWarning(ActionEvent event) {
        String choice = ((Button) event.getSource()).getText();
        AnchorPane thisPage = (AnchorPane) PageController.stackPages.peek();
        thisPage.getChildren().remove(PageController.stackWaringMessages.peek());
        thisPage.setEffect(null);
        switch (choice) {
            case "Yes":
                backFunction();
                break;
            case "No":
                showContent.setDisable(false);
                showContent.setEffect(null);
        }
    }

    @FXML private void ActionOnCreateAccount(ActionEvent event) {
        TextChecker emptyTextChecker = new TextChecker() {
            @Override
            public void check(String s) throws Exception {
                if (s.equals("")) throw new Exception("Please fill all blank before creating account.");
            }
        };

        ArrayList<TextField> textFields = new ArrayList<>();
        for (Node node : gridInput.getChildren()) {
            if (node instanceof TextField) textFields.add((TextField) node);
        }

        try {
            RegisterChecker.checkForm(textFields);
            RegisterChecker.check(emptyTextChecker, gridInput.getChildren());
            RegisterChecker.checkMatchedPassword(textFields);
        }catch (Exception e) {
            warningMeassage.setText(e.getMessage());
            showWarningMessage();
            return;
        }

        initialAddAccount(textFields);
    }

    private void backFunction() {
        AnchorPane thisPage = (AnchorPane) PageController.peakPage();
        Timeline backTransition = new Timeline();
        KeyValue keyValue = new KeyValue(thisPage.translateYProperty(), thisPage.getWidth(), Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(.5), keyValue);
        backTransition.getKeyFrames().add(keyFrame);
        backTransition.play();
        backTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AnchorPane parent = (AnchorPane) PageController.stackPages.firstElement().getParent();
                PageController.stackPages.pop();
                PageController.stackWaringMessages.pop();
                parent.getChildren().remove(thisPage);
            }
        });
    }

    private void isFilledBlank() {
        TextChecker hasTextChecker = new TextChecker() {
            @Override
            public void check(String s) throws Exception {
                if (!s.equals("")) throw new Exception();
            }
        };

        try {
            RegisterChecker.check(hasTextChecker, gridInput.getChildren());
        } catch (Exception e) {
            String topic = "You have just filled some your data in this blank.";
            String description = "If you go back, your data on this page will lose";
            AnchorPane warning = NodeCreator.createWarningAnchorPane(topic, description);
            PageController.stackWaringMessages.pop();
            PageController.stackWaringMessages.push(warning);
        }
    }

    private void initialAddAccount(ArrayList<TextField> textFields) {
        TextField inputUsername = textFields.remove(2);
        TextField inputPassword = textFields.remove(2);
        textFields.remove(2);

        try {
            FileWriter writer = new FileWriter(new File("data/account_data/accounts.csv"), true);
            String account = inputUsername.getText() + "," + inputPassword.getText() + "\n";
            writer.write(account);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(new File("data/account_data/userdata.csv"), true);
            StringBuilder data = new StringBuilder(inputUsername.getText());
            for (TextField t : textFields) data.append(",").append(t.getText());
            data.append(",9999\n");
            writer.write(String.valueOf(data));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        backFunction();
    }

    private void showWarningMessage() {
        Transition fadeIn = EffectController.createFadeTransition(warningMeassage, 0.5, 1);
        Transition fadeOut = EffectController.createFadeTransition(warningMeassage, 0.5 , 0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeIn.play();
        fadeOut.play();
    }
}
