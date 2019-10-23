package theatre.tools;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterChecker {
    public static void check(TextChecker checker, ObservableList<Node> nodes) throws Exception {
        for (Node node : nodes) {
            if (node instanceof TextField) {
                TextField field = (TextField) node;
                checker.check(field.getText());
            }
        }
    }

    public static void checkMatchedPassword(ArrayList<TextField> textFields) throws Exception {
        if (!textFields.get(3).getText().equals(textFields.get(4).getText())) throw new Exception("Password is not matched, check and try again.");
    }

    public static void checkForm(ArrayList<TextField> fields) throws Exception {
        TextField username = fields.get(2);
        TextField password = fields.get(3);
        TextField confirmPassword = fields.get(4);
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matchUsername = pattern.matcher(username.getText());
        Matcher matchPassword = pattern.matcher(password.getText());
        Matcher matchConfirmPass = pattern.matcher(confirmPassword.getText());
        if (username.getText().length() < 6) throw new Exception("Username must has more than 6 characters.");
        if (matchUsername.find()) throw new Exception("Your username is invalid, contains special character.");
        if (password.getText().length() < 8) throw new Exception("Password must has more than 8 characters.");
        if (matchPassword.find() || matchConfirmPass.find()) throw new Exception("Your password is invalid, contains special character.");
    }
}
