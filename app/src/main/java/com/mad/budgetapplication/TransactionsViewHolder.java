package com.mad.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsViewHolder extends RecyclerView.ViewHolder {
    TextView amount;
    TextView name;
    TextView date;
    View view;
    public TransactionsViewHolder(@NonNull View itemView) {
        super(itemView);
        amount = itemView.findViewById(R.id.tamt);
        name = itemView.findViewById(R.id.tname);
        date = itemView.findViewById(R.id.tdate);
        view = (View) itemView.findViewById(R.id.tview);
    }
}
