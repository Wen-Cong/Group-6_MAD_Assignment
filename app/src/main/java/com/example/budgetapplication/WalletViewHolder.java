package com.example.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WalletViewHolder extends RecyclerView.ViewHolder {
    TextView txt;
    public WalletViewHolder(View itemView){
        super(itemView);
        txt = itemView.findViewById(android.R.id.text1);
    }
}
