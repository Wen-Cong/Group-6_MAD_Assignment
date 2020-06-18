package com.example.budgetapplication;

import java.util.ArrayList;
import java.util.List;

public class SharedWallet extends Wallet {
    private String adminId;
    private ArrayList<String> participants;

    public SharedWallet(){}

    public SharedWallet(String name, Double bal, String uid){
        super(name, bal);
        adminId = uid;
        participants = new ArrayList<String>() {};
        participants.add(uid);
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String id) {
        this.participants.add(id);
    }
}
