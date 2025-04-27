package com.example.alpaspay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserRegistration {
    private String email;
    private String balance;

    public UserRegistration() {
    }
    public UserRegistration(String email, String balance) {
        this.email = email;
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
