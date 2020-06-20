package com.example.budgetapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SharedWalletAdapter extends RecyclerView.Adapter<SharedWalletViewHolder> {
    ArrayList<SharedWallet> data;
    Activity sharedWalletActivity;

    public SharedWalletAdapter(Activity activity, ArrayList<SharedWallet> sharedWallets){
        data = sharedWallets;
        sharedWalletActivity = activity;
    }

    @NonNull
    @Override
    public SharedWalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedwalletview, parent, false);
        SharedWalletViewHolder viewHolder = new SharedWalletViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SharedWalletViewHolder holder, int position) {
        SharedWallet sw = data.get(position);
        holder.walletName.setText(sw.getName());
        holder.walletBal.setText("Balance: $" + sw.getBalance().toString());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(sharedWalletActivity, "Shared Wallet Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
