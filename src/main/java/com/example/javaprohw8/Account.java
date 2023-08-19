package com.example.javaprohw8;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id") // Specifies the column name in the database for the primary key
    private long accountId;

    @Column(nullable = false) // Specifies that the column cannot be null
    private String owner; // Represents the owner of the account

    @Column(name = "balance_uan") // Specifies the column name in the database for UAN balance
    private double balanceUAH; // Represents the balance in Ukrainian Hryvnia

    @Column(name = "balance_usd")
    private double balanceUSD;

    @Column(name = "balance_eur")
    private double balanceEUR;

    public Account() {

    }

    public Account(String owner, double balanceUAH, double balanceUSD, double balanceEUR) {
        this.owner = owner;
        this.balanceUAH = balanceUAH;
        this.balanceUSD = balanceUSD;
        this.balanceEUR = balanceEUR;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalanceUAH() {
        return balanceUAH;
    }

    public void setBalanceUAH(double balanceUAN) {
        this.balanceUAH = balanceUAN;
    }

    public double getBalanceUSD() {
        return balanceUSD;
    }

    public void setBalanceUSD(double balanceUSD) {
        this.balanceUSD = balanceUSD;
    }

    public double getBalanceEUR() {
        return balanceEUR;
    }

    public void setBalanceEUR(double balanceEUR) {
        this.balanceEUR = balanceEUR;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", owner='" + owner + '\'' +
                ", balanceUAN=" + balanceUAH +
                ", balanceUSD=" + balanceUSD +
                ", balanceEUR=" + balanceEUR +
                '}';
    }
}
