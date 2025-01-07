import java.io.*;
import java.util.*;

public class FileHandler {
    private String userFilePath;
    private String historyFilePath;

    public FileHandler(String userFilePath, String historyFilePath) {
        this.userFilePath = userFilePath;
        this.historyFilePath = historyFilePath;
    }

    public Map<String, UserAccount> loadAccounts() {
        Map<String, UserAccount> accounts = new HashMap<>();
        File userFile = new File(userFilePath);

        if (!userFile.exists()) {
            return accounts;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length < 5) {
                        continue;
                    }
                    UserAccount account = new UserAccount(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Double.parseDouble(data[4])
                    );
                    accounts.put(data[0], account);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing balance for line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
        }

        return accounts;
    }

    public void saveAccount(UserAccount account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath, true))) {
            writer.write(account.getAccountNumber() + "," +
                    account.getFullName() + "," +
                    account.getPhoneNumber() + "," +
                    account.getPassword() + "," +
                    account.getBalance() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to user file: " + e.getMessage());
        }
    }

    public void saveTransaction(String accountNumber, String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFilePath, true))) {
            writer.write(accountNumber + ": " + transaction + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to transaction file: " + e.getMessage());
        }
    }
}