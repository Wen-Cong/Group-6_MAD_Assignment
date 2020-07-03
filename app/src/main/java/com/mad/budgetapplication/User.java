package com.mad.budgetapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String userName;
    private String userId;
    private ArrayList<Wallet> wallets;
    private ArrayList<String> participatedSharedWallet;

    public User(){
        wallets = new ArrayList<>();
        participatedSharedWallet = new ArrayList<>();
    }
    public User(String id, String name){
        userName = name;
        userId = id;
        wallets = new ArrayList<>();
        participatedSharedWallet = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void addWallet(Wallet w){
        wallets.add(w);
    }
    public ArrayList<Wallet> getWallets(){
        return wallets;
    }

    public  ArrayList<String> getParticipatedSharedWallet(){ return participatedSharedWallet; }
    public void addParticipatedWallet(String walletId){
        participatedSharedWallet.add(walletId);
    }
}
