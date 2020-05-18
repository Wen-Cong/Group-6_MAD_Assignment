package com.example.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsViewHolder extends RecyclerView.ViewHolder {
    TextView amount;
    TextView name;
    TextView wallet;
    public TransactionsViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
