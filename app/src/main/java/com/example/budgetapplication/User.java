package com.example.budgetapplication;

import android.net.Uri;

import java.util.ArrayList;

public class User {
    private String userName;
    private String userId;
    private ArrayList<Wallet> wallets;


    public User(){}
    public User(String id, String name){
        userName = name;
        userId = id;
        wallets = new ArrayList<>();
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
}
