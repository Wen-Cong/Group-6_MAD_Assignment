package com.mad.budgetapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Wallet implements Serializable {
    private String name;
    private Double balance;
    private ArrayList<Transaction> transactionList;
    private ArrayList<RTransaction> RTransactionList;

    public Wallet() {
        this.transactionList = new ArrayList<>();
        RTransactionList = new ArrayList<>();
    }

    public Wallet(String walletname, Double bal) {
        this.name = walletname;
        this.balance = bal;
        this.transactionList = new ArrayList<>();
        RTransactionList = new ArrayList<>();
    }

    public Wallet(String walletname, Double bal, ArrayList<Transaction> tlist) {
        this.name = walletname;
        this.balance = bal;
        this.transactionList = tlist;
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
    public ArrayList<Transaction> getTransactions(){ return transactionList;}
    public String toString(){
        return this.name;
    }

    public ArrayList<RTransaction> getRTransactionList() {
        return RTransactionList;
    }
    public void addRTransaction(RTransaction rTransaction){
        RTransactionList.add(rTransaction);
    }
    public void removeRTransaction(RTransaction rTransaction){
        RTransactionList.remove(rTransaction);
    }
}
