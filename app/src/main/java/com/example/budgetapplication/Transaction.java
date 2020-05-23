package com.example.budgetapplication;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        this.time = getCurrentDateTime();
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

    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        TimeZone tz = TimeZone.getDefault();
        Date date = Calendar.getInstance(tz).getTime();
        return dateFormat.format(date);
    }

}
