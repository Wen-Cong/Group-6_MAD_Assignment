package com.example.budgetapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SharedWalletTransactionAdapter extends RecyclerView.Adapter<SharedWalletTransactionViewHolder> {
    private static final String TAG = "ShareTransactionAdapter";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<SharedTransaction> data;

    public SharedWalletTransactionAdapter(ArrayList<SharedTransaction> transactions){
        data = transactions;
    }

    @NonNull
    @Override
    public SharedWalletTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedtransaction_view, parent, false);
        SharedWalletTransactionViewHolder viewHolder = new SharedWalletTransactionViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SharedWalletTransactionViewHolder holder, int position) {
        final SharedTransaction sharedTransaction = data.get(position);
        String formatedDate;

        getUsername(sharedTransaction.getUid(), holder);
        holder.transactionName.setText(sharedTransaction.getName());
        holder.amt.setText("$" + sharedTransaction.getAmount().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date d = sdf.parse(sharedTransaction.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            formatedDate = format.format(d);
            holder.dateTime.setText(formatedDate);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
            holder.dateTime.setText(sharedTransaction.getTime());
        }

        loadProfilePic(sharedTransaction.getUid());
    }

    private void loadProfilePic(String uid) {
        //add code here
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void getUsername(String uid, final SharedWalletTransactionViewHolder holder){
        databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                holder.username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG, "onCancelled: " + databaseError);
            }
        });
    }
}
