package com.mad.budgetapplication;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private String userId;
    private ArrayList<Wallet> wallets;
    private ArrayList<String> participatedSharedWallet;
    private ArrayList<Asset> AssetList;


    public User(){
        wallets = new ArrayList<>();
        participatedSharedWallet = new ArrayList<>();
        AssetList = new ArrayList<>();

    }
    public User(String id, String name){
        username = name;
        userId = id;
        wallets = new ArrayList<>();
        participatedSharedWallet = new ArrayList<>();
        AssetList = new ArrayList<>();

    }

    public String getUserName() {
        return username;
    }
    public void setUserName(String userName) {
        this.username = userName;
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


}
