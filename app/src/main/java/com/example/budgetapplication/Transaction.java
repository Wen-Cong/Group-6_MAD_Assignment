package com.example.budgetapplication;

import java.io.Serializable;

public class Transaction implements Serializable {
    String Name;
    Double Amount;
    Wallet Wallet;
    String Type;

    public Transaction(){}
    public  Transaction(String name, Double amt, Wallet wallet, String type){
        this.Name = name;
        this.Amount = amt;
        this.Wallet = wallet;
        this.Type = type;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
