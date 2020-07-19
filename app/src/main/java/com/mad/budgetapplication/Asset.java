package com.mad.budgetapplication;
import java.io.Serializable;
import java.util.ArrayList;

public class Asset implements Serializable{
    private String name;
    private Double value;

    public Asset(String AssetName, Double AssetValue){
        this.name = AssetName;
        this.value = AssetValue;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
