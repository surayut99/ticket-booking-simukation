package theatre.scenes;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import theatre.tools.AccountCollector;
import theatre.tools.AccountData.Account;
import theatre.tools.ApplicationDisplay;
import theatre.tools.EffectController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopUpController {

    @FXML
    TextField amountField;

    @FXML
    Text warningMessage;

    @FXML
    VBox dataBox;

    @FXML public void initialize() {
        Account account = AccountCollector.getCurrentAccount();
        String[] data = {account.getUsername(), account.getFirstName(), account.getLastName(), Double.toString(account.getBalance())};

        for (int i = 0; i < data.length; i++) {
            Text text = (Text) dataBox.getChildren().get(i);
            text.setText(text.getText() + data[i]);
            if (i == 3) {
                text.setText(text.getText() + "0 THB");
                ApplicationDisplay applicationDisplay = new ApplicationDisplay(text);
                account.addObserver(applicationDisplay);
            }
        }
    }

    @FXML
    public void actionOnSubmit(ActionEvent event) {
        if (amountField.getText().isEmpty()) {
            warningMessage.setText("Please fill in this blank before topping-up.");
            showWarningMessage();
            return;
        }

        String input = amountField.getText();
        Pattern pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        for (Character c : input.toCharArray()) {
            matcher = pattern.matcher(c.toString());
            if (!matcher.find()) {
                warningMessage.setText("Input must contain with integer only.");
                showWarningMessage();
                return;
            }
        }

        int amount = Integer.parseInt(input);
        if (amount > 5000) {
            warningMessage.setText("Amount must not exceed 5000 THB.");
            showWarningMessage();
            return;
        }

        Account account = AccountCollector.getCurrentAccount();
        account.topUp(amount);
        amountField.setText("");
        warningMessage.setText("Successfully top-up, you can top-up again if you want.");
        showWarningMessage();
    }

    private void showWarningMessage() {
        FadeTransition fadeIn = EffectController.createFadeTransition(warningMessage, 0.5, 1);
        fadeIn.play();
        fadeIn.setOnFinished(e -> {
            FadeTransition fadeout = EffectController.createFadeTransition(warningMessage, 0.5, 0);
            fadeout.setDelay(Duration.seconds(2));
            fadeout.play();
        });
    }
}
