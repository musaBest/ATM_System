import java.io.IOException;
import java.util.*;

public class ATMSystem {
    private Map<String, UserAccount> accounts;
    private FileHandler fileHandler;
    private Map<String, Integer> loginAttempts;

    public ATMSystem(FileHandler fileHandler) throws IOException {
        this.fileHandler = fileHandler;
        this.accounts = fileHandler.loadAccounts();
        this.loginAttempts = new HashMap<>();
    }

    public String createNewAccount(String fullName, String phoneNumber, String password, double initialBalance) throws IOException {
        String accountNumber = generateAccountNumber();
        UserAccount newAccount = new UserAccount(accountNumber, fullName, phoneNumber, password, initialBalance);
        accounts.put(accountNumber, newAccount);
        fileHandler.saveAccount(newAccount);
        return accountNumber;
    }

    public UserAccount login(String accountNumber, String password) {
        if (!accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account not found.");
        }

        UserAccount account = accounts.get(accountNumber);
        loginAttempts.putIfAbsent(accountNumber, 0);

        if (loginAttempts.get(accountNumber) >= 3) {
            throw new IllegalArgumentException("Account locked due to too many failed attempts.");
        }

        if (!account.verifyPassword(password)) {
            loginAttempts.put(accountNumber, loginAttempts.get(accountNumber) + 1);
            throw new IllegalArgumentException("Incorrect password.");
        }

        return account;
    }

    public void performTransaction(UserAccount account, String transactionType, double amount) throws IOException {
        switch (transactionType.toLowerCase()) {
            case "deposit":
                account.deposit(amount);
                break;
            case "withdraw":
                account.withdraw(amount);
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type.");
        }
        fileHandler.saveTransaction(account.getAccountNumber(), transactionType + " " + amount);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.valueOf(1000000 + random.nextInt(9000000));
        } while (accounts.containsKey(accountNumber));
        return accountNumber;
    }
}