package theatre.tools;

import javafx.scene.text.Text;
import theatre.tools.AccountData.Account;

import java.util.Observable;
import java.util.Observer;

public class ApplicationDisplay implements Observer {
    private Text text;

    public ApplicationDisplay(Text text) {
        this.text = text;
    }

    @Override
    public void update(Observable o, Object arg) {
        Account account = (Account) o;
        text.setText("Balance: " + account.getBalance() + "0 THB");
    }
}
