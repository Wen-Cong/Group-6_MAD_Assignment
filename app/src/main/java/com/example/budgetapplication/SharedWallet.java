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

    public SharedWallet(String name, Double bal, String aid, ArrayList<String> participant, ArrayList<Transaction> transactions){
        super(name, bal, transactions);
        adminId = aid;
        participants = participant;
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
