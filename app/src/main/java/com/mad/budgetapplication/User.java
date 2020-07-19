package com.mad.budgetapplication;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class User implements Serializable {
    private String userName;
    private String userId;
    private ArrayList<Wallet> wallets;
    private ArrayList<String> participatedSharedWallet;
    private ArrayList<Asset> AssetList;
    private ArrayList<RTransaction> RTransactionList;

    public User(){
        wallets = new ArrayList<>();
        participatedSharedWallet = new ArrayList<>();
        AssetList = new ArrayList<>();
        RTransactionList = new ArrayList<>();
    }
    public User(String id, String name){
        userName = name;
        userId = id;
        wallets = new ArrayList<>();
        participatedSharedWallet = new ArrayList<>();
        AssetList = new ArrayList<>();
        RTransactionList = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void addWallet(Wallet w){
        wallets.add(w);
    }
    public ArrayList<Wallet> getWallets(){
        return wallets;
    }

    public  ArrayList<String> getParticipatedSharedWallet(){
        return participatedSharedWallet; }

    public void addParticipatedWallet(String walletId){
        participatedSharedWallet.add(walletId);
    }
    public void removeParticipatedWallet(String walletId){
        participatedSharedWallet.remove(walletId);
    }

    public ArrayList<Asset> getAssetList() {
        return AssetList;
    }
    public void addAsset(Asset asset){
        AssetList.add(asset);
    }
    public void removeAsset(Asset asset){
        AssetList.remove(asset);
    }

    public ArrayList<RTransaction> getRTransactionList() {
        return RTransactionList;
    }
    public void addRTransaction(RTransaction rTransaction){
        RTransactionList.add(rTransaction);
    }
    public void removeRTransaction(RTransaction rTransaction){
        RTransactionList.remove(rTransaction);
    }
}
