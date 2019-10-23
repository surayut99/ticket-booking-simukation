package theatre.tools;

import java.util.ArrayList;

public class AccountController {
    private static String username;
    private static String firstName;
    private static String lastName;
    private static String question;
    private static String answer;
    private static double balance;
    private static int[] theatres;
    private static String[][] startTimes;
    private static String[][] positions;


    public static String getUsername() {
        return username;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getQuestion() {
        return question;
    }

    public static String getAnswer() {
        return answer;
    }

    public static double getBalance() {
        return balance;
    }

    public static void setUsername(String username) {
        AccountController.username = username;
    }

    public static void setFirstName(String firstName) {
        AccountController.firstName = firstName;
    }

    public static void setLastName(String lastName) {
        AccountController.lastName = lastName;
    }

    public static void setQuestion(String question) {
        AccountController.question = question;
    }

    public static void setAnswer(String answer) {
        AccountController.answer = answer;
    }

    public static void setBalance(double balance) {
        AccountController.balance = balance;
    }

    public static void singOut() {
        username = "";
        firstName = "";
        lastName = "";
        question = "";
        answer = "";
        balance = 0;
    }

    public static void purchase(double amount) {
        if (balance - amount < 0) {
            throw new IllegalArgumentException("Your balance is not enough.");
        }
        balance -= amount;
    }

    public static String printData() {
        return  "Username: " + getUsername() + "\n" +
                "Firstname: " + getFirstName() + "\n" +
                "Lastname: " + getLastName() + "\n" +
                "Question: " + getQuestion() + "\n" +
                "Answer: " + getAnswer() + "\n" +
                "Balance: " + getBalance();
    }
}
