package com.example.budgetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletViewHolder> {
    Activity activityMain;
    final String TAG = "walletAdapter";
    ArrayList<Wallet> data;

    public WalletAdapter(ArrayList<Wallet> input, Activity parentActivity){
        data = input;
        activityMain = parentActivity;
    }
    //This method is called by adapter when a new ViewHolder is created
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        //This line create the UI from the XML
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_view_item, parent, false);
    //return the ViewHolder
        return new WalletViewHolder(item);
    }
    /*This method is called by Adapter to bind data to the ViewHolder. Adapter pass in the ViewHolder, and the row id (index starts from 0)*/

    public void onBindViewHolder(WalletViewHolder holder, final int position){
    //Using the row id to retrieve data from list
        final String name = data.get(position).getName();
        String bal = String.valueOf(data.get(position).getBalance());
    //Display the information on to the UI
        holder.name.setText(name);
        holder.balance.setText(bal);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "delete clicked");
                DeleteDialog(name, position);
            }
        });
    }
    public int getItemCount(){
        return data.size();
    }

    public void deleteItemFromDB(String name){
        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentId);
        Query deleteQuery = databaseReference.child("wallets").orderByChild("name").equalTo(name);

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void DeleteDialog(final String Name, final int Position){
        AlertDialog.Builder builder = new AlertDialog.Builder(activityMain);
        builder.setTitle("Delete Wallet");
        builder.setMessage("Are you sure you want to delete "+Name+"?\n It will delete all transactions.");
        builder.setCancelable(false);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItemFromDB(Name);
                data.remove(Position);
                notifyDataSetChanged();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

