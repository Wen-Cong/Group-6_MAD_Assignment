package com.example.budgetapplication;

import android.app.Activity;
import android.content.Intent;
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
    public void onBindViewHolder(@NonNull SharedWalletViewHolder holder, final int position) {
        final SharedWallet sw = data.get(position);
        holder.walletName.setText(sw.getName());
        holder.walletBal.setText("Balance: $" + sw.getBalance().toString());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSharedWalletdetails(sw, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void openSharedWalletdetails(SharedWallet sharedWallet, int position){
        Intent intent = new Intent(sharedWalletActivity, SharedWalletDetailsActivity.class);
        String walletId = ((SharedWalletActivity) sharedWalletActivity).user.getParticipatedSharedWallet().get(position);
        intent.putExtra("sharedWallet", sharedWallet);
        intent.putExtra("sharedWalletId", walletId);
        sharedWalletActivity.startActivityForResult(intent,
                ((SharedWalletActivity) sharedWalletActivity).REQ_SHAREDWALLETDETAILS_CODE);

    }
}
