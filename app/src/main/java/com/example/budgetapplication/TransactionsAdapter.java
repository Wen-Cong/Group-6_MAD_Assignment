package com.example.budgetapplication;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsViewHolder> {
    ArrayList<Transaction> transactionArrayList;
    Activity activityMain;
    User userData;
    String walletKey;
    String transactionKey;
    DatabaseReference databaseReference;
    String uid;
    Wallet wallet;
    private final String TAG = "Transaction Adapter";
    public TransactionsAdapter(ArrayList<Transaction> input, Activity parentActivity){
        transactionArrayList = input;
        activityMain = parentActivity;
        userData = ((HomeActivity) parentActivity).getUser();
    }


    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tranasaction_viewholder, parent, false);
        TransactionsViewHolder viewHolder = new TransactionsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        final Transaction t = transactionArrayList.get(position);
        holder.name.setText(t.getName());
        holder.amount.setText("$" + t.getAmount().toString());
        holder.type.setText(t.getType());
        holder.date.setText(t.getTime());
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference();
                loadKeys(t);
                showDeleteDialogBox(t);
                Log.v(TAG, "Out of showDeleteDialogBox");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    private void getKey(Wallet w, final Transaction t) {
        String walletname = w.getName().toString();
        databaseReference.child("Users").child(uid).child("wallets").orderByChild("name")
                .equalTo(walletname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    walletKey = childSnapshot.getKey();
                    Log.v(TAG,"Wallet: " + walletKey);
                    getTransactionKey(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getTransactionKey(Transaction t) {
        String tTime = t.getTime().toString();
        databaseReference.child("Users").child(uid).child("wallets").child(walletKey)
                .child("transactions").orderByChild("time")
                .equalTo(t.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    transactionKey = childSnapshot.getKey();
                    Log.v(TAG, "Transactions: " + transactionKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadKeys(final Transaction t){
        for(Wallet w : userData.getWallets()){
            for(Transaction transaction: w.getTransactions()){
                if(transaction == t){
                    getKey(w, t);
                    wallet = w;
                }
            }
        }
    }

    private void removeFromDatabase(final Transaction t){
        databaseReference.child("Users").child(uid).child("wallets").child(walletKey).child("transactions")
                .child(transactionKey).removeValue();
        int wpos = userData.getWallets().indexOf(wallet);
        userData.getWallets().get(wpos).getTransactions().remove(t);
    }

    private void showDeleteDialogBox(final Transaction t){
        AlertDialog.Builder builder = new AlertDialog.Builder(activityMain);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this transaction?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int pos = transactionArrayList.indexOf(t);
                transactionArrayList.remove(pos);
                notifyItemRemoved(pos);
                removeFromDatabase(t);
                ((HomeActivity) activityMain).user = userData;
                Toast.makeText(activityMain, "Transaction Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
