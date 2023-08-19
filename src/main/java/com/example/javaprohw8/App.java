package com.example.javaprohw8;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("Bank");
            em = emf.createEntityManager();

            try {
                createSampleUsers();
                while (true) {
                    System.out.println("0: Show all users");
                    System.out.println("1: Show balance in UAH");
                    System.out.println("2: Transfer of funds to another accounts");
                    System.out.println("3: Currency converting");
                    System.out.println("4: Refill the account in the desired currency");
                    System.out.println("5: View All Transactions");
                    System.out.println("6: View total money of all accounts");
                    System.out.println("7: Exit");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "0":
                            showAllUsers();
                            break;

                        case "1":
                            showBalance();
                            break;

                        case "2":
                            transferFunds();
                            break;

                        case "3":
                            convertCurrency();
                            break;

                        case "4":
                            refillAccount();
                            break;

                        case "5":
                            viewTransactions();
                            break;

                        case "6":
                            viewTotal();
                            break;

                        case "7":
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }
    private static void createSampleUsers() {
        em.getTransaction().begin();
        try {
            User user1 = new User();
            user1.setUsername("Bill Gates");
            user1.setEmail("gates@example.com");
            em.persist(user1);

            User user2 = new User();
            user2.setUsername("ELon Musk");
            user2.setEmail("musk@example.com");
            em.persist(user2);

            User user3 = new User();
            user3.setUsername("Angelina Jolie");
            user3.setEmail("jolie@example.com");
            em.persist(user3);

            Account account1 = new Account();
            account1.setOwner("Bill Gates");
            account1.setBalanceUAH(1800000);
            account1.setBalanceUSD(50000);
            account1.setBalanceEUR(45000);
            em.persist(account1);

            Account account2 = new Account();
            account2.setOwner("Elon Musk");
            account2.setBalanceUAH(1800000);
            account2.setBalanceUSD(50000);
            account2.setBalanceEUR(45000);
            em.persist(account2);

            Account account3 = new Account();
            account3.setOwner("Angelina Jolie");
            account3.setBalanceUAH(1800000);
            account3.setBalanceUSD(50000);
            account3.setBalanceEUR(45000);
            em.persist(account3);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
    private static void showAllUsers() {
        em.getTransaction().begin();
        try {
            List<User> users = em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();

            for (User user : users) {
                System.out.println("Username: " + user.getUsername() + ", Email: " + user.getEmail());

                // Retrieve the account for this user based on the owner field
                Account account = em.createQuery("SELECT a FROM Account a WHERE a.owner = :owner", Account.class)
                        .setParameter("owner", user.getUsername())
                        .getSingleResult();

                System.out.println("Balance for " + user.getUsername() + " in UAH: " + account.getBalanceUAH());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.getTransaction().commit();
        }
    }

    private static void showBalance() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        em.getTransaction().begin();
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            Account account = em.createQuery("SELECT a FROM Account a WHERE a.owner = :owner", Account.class)
                    .setParameter("owner", user.getUsername())
                    .getSingleResult();

            System.out.println("Balance for " + user.getUsername() + " in UAH: " + account.getBalanceUAH());
        } catch (NoResultException e) {
            System.out.println("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.getTransaction().commit();
        }
    }
    private static void transferFunds() {
        System.out.print("Enter account username FROM: ");
        String sourceUsername = sc.nextLine();

        System.out.print("Enter account username transfer TO: ");
        String targetUsername = sc.nextLine();

        System.out.print("Enter amount to transfer: ");
        double amount = Double.parseDouble(sc.nextLine());

        em.getTransaction().begin();
        try {
            // Retrieve source account
            Account sourceAccount = em.createQuery("SELECT a FROM Account a WHERE a.owner = :owner", Account.class)
                    .setParameter("owner", sourceUsername)
                    .getSingleResult();

            // Retrieve target account
            Account targetAccount = em.createQuery("SELECT a FROM Account a WHERE a.owner = :owner", Account.class)
                    .setParameter("owner", targetUsername)
                    .getSingleResult();

            if (sourceAccount.getBalanceUAH() >= amount) {
                sourceAccount.setBalanceUAH(sourceAccount.getBalanceUAH() - amount);
                targetAccount.setBalanceUAH(targetAccount.getBalanceUAH() + amount);

                em.getTransaction().commit();
                System.out.println("Funds transferred successfully.");
            } else {
                System.out.println("Insufficient funds in the source account.");
                em.getTransaction().rollback();
            }
        } catch (NoResultException e) {
            System.out.println("One or both users not found.");
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
    private static void convertCurrency() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter amount to convert: ");
        double amount = Double.parseDouble(sc.nextLine());

        System.out.print("Enter source currency (USD, EUR, UAH): ");
        String sourceCurrency = sc.nextLine();

        System.out.print("Enter target currency (USD, EUR, UAH): ");
        String targetCurrency = sc.nextLine();

        em.getTransaction().begin();
        try {
            // Retrieve user's account
            Account userAccount = em.createQuery("SELECT a FROM Account a WHERE a.owner = :owner", Account.class)
                    .setParameter("owner", username)
                    .getSingleResult();

            double sourceBalance = 0.0;
            double targetBalance = 0.0;

            switch (sourceCurrency) {
                case "USD":
                    sourceBalance = userAccount.getBalanceUSD();
                    break;
                case "EUR":
                    sourceBalance = userAccount.getBalanceEUR();
                    break;
                case "UAH":
                    sourceBalance = userAccount.getBalanceUAH();
                    break;
            }

            switch (targetCurrency) {
                case "USD":
                    targetBalance = userAccount.getBalanceUSD();
                    break;
                case "EUR":
                    targetBalance = userAccount.getBalanceEUR();
                    break;
                case "UAH":
                    targetBalance = userAccount.getBalanceUAH();
                    break;
            }

            if (sourceBalance >= amount) {
                double conversionRate = 1.0; // Default conversion rate (no conversion)

                if (sourceCurrency.equals("USD") && targetCurrency.equals("UAH")) {
                    conversionRate = 36.0;
                } else if (sourceCurrency.equals("EUR") && targetCurrency.equals("UAH")) {
                    conversionRate = 1.10 * 36.0;
                } else if (sourceCurrency.equals("UAH") && targetCurrency.equals("USD")) {
                    conversionRate = 1.0 / 36.0;
                } else if (sourceCurrency.equals("UAH") && targetCurrency.equals("EUR")) {
                    conversionRate = 1.0 / (1.10 * 36.0);
                } else if (sourceCurrency.equals("USD") && targetCurrency.equals("EUR")) {
                    conversionRate = 1.0 / 1.10;
                } else if (sourceCurrency.equals("EUR") && targetCurrency.equals("USD")) {
                    conversionRate = 1.10;
                }

                double convertedAmount = amount * conversionRate;

                switch (sourceCurrency) {
                    case "USD":
                        userAccount.setBalanceUSD(sourceBalance - amount);
                        break;
                    case "EUR":
                        userAccount.setBalanceEUR(sourceBalance - amount);
                        break;
                    case "UAH":
                        userAccount.setBalanceUAH(sourceBalance - amount);
                        break;
                }

                switch (targetCurrency) {
                    case "USD":
                        userAccount.setBalanceUSD(targetBalance + convertedAmount);
                        break;
                    case "EUR":
                        userAccount.setBalanceEUR(targetBalance + convertedAmount);
                        break;
                    case "UAH":
                        userAccount.setBalanceUAH(targetBalance + convertedAmount);
                        break;
                }

                em.getTransaction().commit();
                System.out.println("Currency conversion successful.");
            } else {
                System.out.println("Insufficient funds for conversion.");
                em.getTransaction().rollback();
            }
        } catch (NoResultException e) {
            System.out.println("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    private static void refillAccount() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter currency (USD, EUR, UAH): ");
        String currency = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(sc.nextLine());

        em.getTransaction().begin();
        try {
            // Retrieve user's account
            Account userAccount = em.createQuery("SELECT a FROM Account a WHERE a.owner = :owner", Account.class)
                    .setParameter("owner", username)
                    .getSingleResult();

            switch (currency) {
                case "USD":
                    userAccount.setBalanceUSD(userAccount.getBalanceUSD() + amount);
                    break;
                case "EUR":
                    userAccount.setBalanceEUR(userAccount.getBalanceEUR() + amount);
                    break;
                case "UAH":
                    userAccount.setBalanceUAH(userAccount.getBalanceUAH() + amount);
                    break;
                default:
                    System.out.println("Invalid currency.");
                    em.getTransaction().rollback();
                    return;
            }

            em.getTransaction().commit();
            System.out.println("Account refilled successfully.");
        } catch (NoResultException e) {
            System.out.println("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
    private static void viewTransactions() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        em.getTransaction().begin();
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            List<Transaction> transactions = user.getTransactions();

            System.out.println("Transactions for " + user.getUsername() + ":");
            for (Transaction transaction : transactions) {
                System.out.println("Date: " + transaction.getDate() + ", Amount: " + transaction.getAmount() +
                        ", Description: " + transaction.getDescription());
            }
        } catch (NoResultException e) {
            System.out.println("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.getTransaction().commit();
        }
    }
    private static void viewTotal() {
        em.getTransaction().begin();
        try {
            Double totalMoneyUAH = em.createQuery("SELECT SUM(a.balanceUAH) FROM Account a", Double.class)
                    .getSingleResult();

            Double totalMoneyUSD = em.createQuery("SELECT SUM(a.balanceUSD) FROM Account a", Double.class)
                    .getSingleResult();

            Double totalMoneyEUR = em.createQuery("SELECT SUM(a.balanceEUR) FROM Account a", Double.class)
                    .getSingleResult();

            System.out.println("Total Money in UAH: " + totalMoneyUAH);
            System.out.println("Total Money in USD: " + totalMoneyUSD);
            System.out.println("Total Money in EUR: " + totalMoneyEUR);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.getTransaction().commit();
        }
    }

}


