package com.example.budgetapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WalletViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView balance;
    ImageButton delete;
    public WalletViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.walletName);
        balance = itemView.findViewById(R.id.walletBalance);
        delete = itemView.findViewById(R.id.delete);
    }
}
