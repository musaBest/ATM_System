import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("users.txt", "transactions.txt");
        ATMSystem atmSystem;

        try {
            atmSystem = new ATMSystem(fileHandler);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the ATM System!");
            while (true) {
                System.out.println("1. Create New Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter full name: ");
                        scanner.nextLine();
                        String name = scanner.nextLine();
                        System.out.print("Enter phone number: ");
                        String phone = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        System.out.print("Enter initial balance: ");
                        double balance = scanner.nextDouble();
                        String accountNumber = atmSystem.createNewAccount(name, phone, password, balance);
                        System.out.println("Account created! Your account number is: " + accountNumber);
                        break;

                    case 2:
                        System.out.print("Enter account number: ");
                        String accNumber = scanner.next();
                        System.out.print("Enter password: ");
                        String accPassword = scanner.next();
                        UserAccount account;
                        try {
                            account = atmSystem.login(accNumber, accPassword);
                            System.out.println("Login successful!");

                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. View Transaction History");
                            System.out.print("Choose an option: ");
                            int action = scanner.nextInt();

                            switch (action) {
                                case 1:
                                    System.out.print("Enter deposit amount: ");
                                    double depositAmount = scanner.nextDouble();
                                    atmSystem.performTransaction(account, "deposit", depositAmount);
                                    System.out.println("Deposit successful!");
                                    break;

                                case 2:
                                    System.out.print("Enter withdrawal amount: ");
                                    double withdrawAmount = scanner.nextDouble();
                                    atmSystem.performTransaction(account, "withdraw", withdrawAmount);
                                    System.out.println("Withdrawal successful!");
                                    break;

                                case 3:
                                    System.out.println("Current Balance: " + account.getBalance());
                                    break;

                                case 4:
                                    System.out.println("Transaction History:");
                                    for (String transaction : account.getTransactionHistory()) {
                                        System.out.println(transaction);
                                    }
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("Thank you for using the ATM System!");
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error initializing the system: " + e.getMessage());
        }
    }
}