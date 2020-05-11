package com.example.budgetapplication;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private String name;
    private Double balance;
    private ArrayList<Transaction> transactionList;

    public Wallet() {
    }

    public Wallet(String walletname, Double bal) {
        name = walletname;
        balance = bal;
        transactionList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public void addTransactions(Transaction t){
        transactionList.add(t);
    }
}
