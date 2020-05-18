package com.example.budgetapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletViewHolder> {
    ArrayList<Wallet> data;
    public WalletAdapter(ArrayList<Wallet> input){
        data = input;
    }
    //This method is called by adapter when a new ViewHolder is created
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        //This line create the UI from the XML
        View item = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
//return the ViewHolder
        return new WalletViewHolder(item);
    }
    /*This method is called by Adapter to bind data to the ViewHolder. Adapter pass in the ViewHolder, and the row id (index starts from 0)*/

    public void onBindViewHolder(WalletViewHolder holder, int position){
//Using the row id to retrieve data from list
        String s = data.get(position).getName();
//Display the information on to the UI
        holder.txt.setText(s);
    }
    public int getItemCount(){
        return data.size();
    }

}
