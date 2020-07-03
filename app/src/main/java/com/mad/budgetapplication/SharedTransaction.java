package com.mad.budgetapplication;

public class SharedTransaction extends Transaction {
    private String uid;

    public SharedTransaction(String name, Double amt, String type, String user) {
        super(name, amt, type);
        this.uid = user;
    }

    public SharedTransaction(String name, Double amt, String type, String dateTime, String user) {
        super(name, amt, type, dateTime);
        this.uid = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String username) {
        this.uid = username;
    }
}
