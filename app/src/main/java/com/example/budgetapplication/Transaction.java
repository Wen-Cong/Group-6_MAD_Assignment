package com.example.budgetapplication;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Transaction implements Serializable {
    private String Name;
    private Double Amount;
    private String Type;
    private String time;

    public Transaction(){}

    public Transaction(String name, Double amt, String type){
        this.Name = name;
        this.Amount = amt;
        this.Type = type;
        this.time = Calendar.getInstance().getTime().toString();
    }

    public Transaction(String name, Double amt, String type, String dateTime){
        this.Name = name;
        this.Amount = amt;
        this.Type = type;
        this.time = dateTime;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTime() {
        return time;
    }

}
