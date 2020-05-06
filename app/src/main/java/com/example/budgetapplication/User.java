package com.example.budgetapplication;

import android.net.Uri;

public class User {
    String userName;



    public User(){}
    public User(String name){
        userName = name;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
