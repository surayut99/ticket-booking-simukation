package theatre.tools;

import theatre.tools.AccountData.Account;

import java.util.HashMap;
import java.util.Set;

public class AccountCollector {
    private static HashMap<String, Account> accounts;
    private static String currentAccount;

    public static void addAccount(String username, Account account) {
        if (accounts == null)
            accounts = new HashMap<>();

        accounts.put(username, account);
    }

    public static Account getCurrentAccount() {
        return accounts.get(currentAccount);
    }

    public static void login(String inputUsername, String inputPassword) {
        Set<String> usernameSet = accounts.keySet();
        if (!usernameSet.contains(inputUsername))
            throw new IllegalArgumentException("This account does not exist, check and try again.");
        Account account = accounts.get(inputUsername);
        if (!account.getPassword().equals(inputPassword))
            throw new IllegalArgumentException("Password is invalid, check and try again.");
        currentAccount = inputUsername;
    }

    public static void logout() {
        currentAccount = null;
    }

    public static Set<String> getKeySet() {
        return accounts.keySet();
    }

    public static Account getAccount(String username) {
        return accounts.get(username);
    }
}
