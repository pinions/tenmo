package com.techelevator.tenmo.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class UserAccount {

    private String username;
    private double balance;

    public UserAccount(double balance, String username) {
        this.username = username;
        this.balance = balance;
    }
    public UserAccount() {

    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }
    public double getBalance() {
        return balance;
    }
}
