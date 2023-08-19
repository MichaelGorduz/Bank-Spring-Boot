package com.example.javaprohw8;

import javax.persistence.*;
import java.util.*;

@Entity // Marks the class as a JPA entity
@Table(name = "users") // Specifies the table name in the database
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generates auto-incremented primary key
    @Column(name = "user_id") // Specifies the column name in the database
    private Long userId;

    @Column(name = "username") // Specifies the column name in the database
    private String username;

    @Column(name = "email") // Specifies the column name in the database
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
