package theatre.scenes;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import theatre.tools.*;
import theatre.tools.AccountData.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {
    @FXML
    private Button backButton;
    @FXML
    private GridPane gridInput;
    @FXML
    private AnchorPane showContent;
    @FXML
    private Text warningMessage;
    @FXML
    private ScrollPane mainScroll;

    @FXML
    private void initialize() {
        backButton.setOnAction(this::actionOnBackButton);
        PageController.setMainShowContent(mainScroll);
//        ScrollPane scrollPane = new ScrollPane();
//        Parent parent = showContent.getParent();
//        AnchorPane anchorPane = (AnchorPane) parent;
//        anchorPane.getChildren().add(scrollPane);
//        scrollPane.setContent(showContent);
//        scrollPane.prefWidthProperty().bind(anchorPane.widthProperty());
//        scrollPane.prefHeightProperty().bind(anchorPane.heightProperty());
//        showContent.prefWidthProperty().bind(scrollPane.widthProperty());
//        showContent.prefHeightProperty().bind(scrollPane.heightProperty());
//
//        PageController.setMainShowContent(scrollPane);
//        scrollPane.prefHeightProperty().bind(showContent.heightProperty());
//        scrollPane.prefWidthProperty().bind(showContent.widthProperty());
    }

    @FXML
    private void actionOnBackButton(ActionEvent event) {
        isFilledBlank();
//        AnchorPane thisPage = (AnchorPane) PageController.getStackPages().peek();
        AnchorPane peekMessage = PageController.getStackWaringMessages().peek();
        if (peekMessage == null) {
            backFunction();
        } else {
//            thisPage.getChildren().add(peekMessage);
            WarningController.showWarning(peekMessage);
            showContent.setDisable(true);
            showContent.setEffect(new ColorAdjust(0, 0, -0.5, 0));
            ((Button) peekMessage.getChildren().get(3)).setOnAction(this::actionOnChoiceWarning);
            ((Button) peekMessage.getChildren().get(4)).setOnAction(this::actionOnChoiceWarning);
        }
    }

    @FXML
    private void actionOnChoiceWarning(ActionEvent event) {
        String choice = ((Button) event.getSource()).getText();
        AnchorPane thisPage = (AnchorPane) PageController.getStackPages().peek();
//        thisPage.getChildren().remove(PageController.getStackWaringMessages().peek());
        WarningController.hideWarning();
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

    @FXML
    private void actionOnCreateAccount(ActionEvent event) {
        TextChecker emptyTextChecker = new TextChecker() {
            @Override
            public void check(String s) {
                if (s.equals("")) throw new IllegalArgumentException("Please fill all blank before creating account.");
            }
        };

        ArrayList<TextField> textFields = new ArrayList<>();
        for (Node node : gridInput.getChildren()) {
            if (node instanceof TextField) textFields.add((TextField) node);
        }

        try {
            RegisterChecker.check(emptyTextChecker, gridInput.getChildren());
            RegisterChecker.checkForm(textFields);
            RegisterChecker.checkMatchedPassword(textFields);

        } catch (IllegalArgumentException e) {
            warningMessage.setText(e.getMessage());
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
                AnchorPane parent = (AnchorPane) PageController.getStackPages().firstElement().getParent();
                PageController.getStackPages().pop();
                PageController.getStackWaringMessages().pop();
                parent.getChildren().remove(thisPage);
            }
        });
    }

    private void isFilledBlank() {
        TextChecker hasTextChecker = new TextChecker() {
            @Override
            public void check(String s) {
                if (!s.equals("")) throw new IllegalArgumentException();
            }
        };

        try {
            RegisterChecker.check(hasTextChecker, gridInput.getChildren());
        } catch (IllegalArgumentException e) {
            String topic = "You have just filled some your data in this blank.";
            String description = "If you go back, your data on this page will lose";
            AnchorPane warning = NodeCreator.createWarningAnchorPane(this::actionOnChoiceWarning, topic, description);
            PageController.getStackWaringMessages().pop();
            PageController.getStackWaringMessages().push(warning);
        }
    }

    private void initialAddAccount(ArrayList<TextField> textFields) {
        String inputUsername = textFields.remove(2).getText();
        String inputPassword = textFields.remove(2).getText();
        textFields.remove(2);
        List<String> dataList = new ArrayList<>();

        try {
            FileWriter writer = new FileWriter(new File("data/accountData/accounts.csv"), true);
            String account = inputUsername + "," + inputPassword + "\n";
            StringBuilder data = new StringBuilder();
            for (TextField t : textFields) {
                data.append(t.getText()).append(",");
                dataList.add(t.getText());
            }
            writer.write(account + data + "0.0\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(new File("data/accountData/BookingData.csv"), true);
            String data = "username," + inputUsername + "\n\n";
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Account newAccount = new Account(inputUsername, inputPassword, dataList.get(0),
                dataList.get(1), dataList.get(2), dataList.get(3), dataList.get(4), 0.0
        );
        AccountCollector.addAccount(inputUsername, newAccount);
        backFunction();
    }

    private void showWarningMessage() {
        Transition fadeIn = EffectController.createFadeTransition(warningMessage, 0.5, 1);
        Transition fadeOut = EffectController.createFadeTransition(warningMessage, 0.5, 0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeIn.play();
        fadeOut.play();
    }
}
