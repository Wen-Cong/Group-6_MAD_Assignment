package com.mad.budgetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_viewholder, parent, false);
        TransactionsViewHolder viewHolder = new TransactionsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        final Transaction t = transactionArrayList.get(position);
        String formatedDate;
        //update view with transaction name and amount
        holder.name.setText(t.getName());
        holder.amount.setText("$" + t.getAmount().toString());

        //format date to dd/MM/yyyy for display
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date d = sdf.parse(t.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            formatedDate = format.format(d);
            holder.date.setText(formatedDate);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
            holder.date.setText(t.getTime());
        }

        //set color for respective transaction type
        if(t.getType().equals("Income")){
            holder.amount.setTextColor(Color.GREEN);
        }

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //delete transaction and update database
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
        //get primary key for wallet
        databaseReference.child("Users").child(uid).child("wallets").orderByChild("name")
                .equalTo(walletname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    walletKey = childSnapshot.getKey();
                    Log.v(TAG,"Wallet: " + walletKey);
                    //get transaction primary key upon finding wallet primary key
                    getTransactionKey(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getTransactionKey(Transaction t) {
        //find primary key for selecte transaction in database
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

    //load primary key obtain for respective wallet and transaction with selected transaction
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
        //update wallet balance after transaction deleted
        double currentBal = userData.getWallets().get(wpos).getBalance();
        double updatedBal = currentBal - t.getAmount();
        databaseReference.child("Users").child(uid).child("wallets").child(walletKey).child("balance").setValue(updatedBal);
        userData.getWallets().get(wpos).setBalance(updatedBal);
        //delete selected transaction from database
        userData.getWallets().get(wpos).getTransactions().remove(t);
    }

    private void showDeleteDialogBox(final Transaction t){
        //display dialog box for delete transaction confirmation
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
                //update recyclerview, database and local user object upon confirming a delete
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
