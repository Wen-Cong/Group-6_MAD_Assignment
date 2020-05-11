package com.example.budgetapplication;

public class Transaction {
    String Name;
    Double Amount;
    Wallet Wallet;

    public Transaction(){}
    public  Transaction(String name, Double amt, Wallet wallet){
        Name = name;
        Amount = amt;
        Wallet = wallet;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public com.example.budgetapplication.Wallet getWallet() {
        return Wallet;
    }

    public void setWallet(com.example.budgetapplication.Wallet wallet) {
        Wallet = wallet;
    }
}
