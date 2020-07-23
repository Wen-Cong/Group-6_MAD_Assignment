package com.mad.budgetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RTransactionAdapter extends RecyclerView.Adapter<RTransactionViewHolder> {
    User user;
    Activity activityMain;
    ArrayList<RTransaction> rTransactionArrayList;
    ArrayList<Wallet> wallets;
    final private String TAG = "RTransactionAdapter";

    RTransactionAdapter(User currentUser, Activity parentActivity) {
        rTransactionArrayList = new ArrayList<RTransaction>();
        user = currentUser;
        activityMain = parentActivity;
        wallets = user.getWallets();
        for (Wallet wallet : wallets
        ) {
            rTransactionArrayList.addAll(wallet.getRTransactionList());
        }
    }
        //This method is called by adapter when a new ViewHolder is created
        public RTransactionViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        //This line create the UI from the XML
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rtransaction_view_item, parent, false);
        //return the ViewHolder
            return new RTransactionViewHolder(item);
        }

    /*This method is called by Adapter to bind data to the ViewHolder. Adapter pass in the ViewHolder, and the row id (index starts from 0)*/

    public void onBindViewHolder(RTransactionViewHolder holder, final int position){
        //Using the row id to retrieve data from list
        String pattern = "dd/MM/yyyy";
        RTransaction a = rTransactionArrayList.get(position);
        //Display the information on to the UI
        final String s = a.getName();
        final Date date = a.getStartingDate();
        String v = "$"+String.valueOf(a.getAmount());
        String dateFormat = new SimpleDateFormat(pattern).format(date);
        holder.name.setText(s);
        holder.amt.setText(v);
        holder.date.setText(dateFormat);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeleteDialog(s, position);
                return true;
            }
        });
    }

    public int getItemCount(){
        return rTransactionArrayList.size();
    }

    public void deleteItemFromDB(String name){
        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentId);
        Query deleteQuery = databaseReference.child("rtransactionList").orderByChild("name").equalTo(name);

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
        builder.setTitle("Delete Transaction");
        builder.setMessage("Are you sure you want to delete "+Name+"?");
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
                rTransactionArrayList.remove(Position);
                notifyDataSetChanged();
                Toast.makeText(activityMain, "Recurring Transaction successfully deleted", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

