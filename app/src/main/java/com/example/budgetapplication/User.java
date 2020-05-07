package com.example.budgetapplication;

import android.net.Uri;

public class User {
    String userName;
    String userId;


    public User(){}
    public User(String id, String name){
        userName = name;
        userId = id;
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
}
