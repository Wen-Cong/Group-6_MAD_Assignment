package com.mad.budgetapplication;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RTransaction implements Serializable{
    private String name;
    private Integer interval;
    private String startDate;
    private Double amount;
    private String type;
    private Date startingDate;


    public RTransaction(String Name, int Interval, String StartDate, double Amount, String Type) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        this.name = Name;
        this.interval = Interval;
        this.startDate = StartDate;
        this.amount = Amount;
        this.type = Type;
        this.startingDate = format.parse(startDate);
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

    public String getStartDate() {
        return startDate;
    }
    public Date getStartingDate(){
        return startingDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
