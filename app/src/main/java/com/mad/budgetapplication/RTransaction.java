package com.mad.budgetapplication;
import java.io.Serializable;
import java.util.Date;

public class RTransaction implements Serializable{
    private String name;
    private int interval;
    private Date startDate;
    private int amount;

    public RTransaction(String Name, int Interval, Date StartDate, int Amount){
        this.name = Name;
        this.interval = Interval;
        this.startDate = StartDate;
        this.amount = Amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
