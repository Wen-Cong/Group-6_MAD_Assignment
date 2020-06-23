package com.example.budgetapplication;

import java.util.ArrayList;
import java.util.List;

public class SharedWallet extends Wallet {
    private String adminId;
    private ArrayList<String> participants;
    private ArrayList<SharedTransaction> sharedTransactions;

    public SharedWallet(){
        sharedTransactions = new ArrayList<>();
        participants = new ArrayList<>();
    }

    public SharedWallet(String name, Double bal, String uid){
        super(name, bal);
        adminId = uid;
        sharedTransactions = new ArrayList<>();
        participants = new ArrayList<>();
        participants.add(uid);
    }

    public SharedWallet(String name, Double bal, String aid, ArrayList<String> participant, ArrayList<SharedTransaction> transactions){
        super(name, bal);
        adminId = aid;
        participants = participant;
        sharedTransactions = transactions;
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

    public void addSharedTransaction(SharedTransaction st){
        sharedTransactions.add(st);
    }

    public ArrayList<SharedTransaction> getSharedTransaction(){
        return sharedTransactions;
    }
}
