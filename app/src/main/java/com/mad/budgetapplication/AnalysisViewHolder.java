package com.mad.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnalysisViewHolder extends RecyclerView.ViewHolder {
    TextView walletName;
    TextView NoOfTransaction;
    public AnalysisViewHolder(@NonNull View itemView) {
        super(itemView);
        walletName = itemView.findViewById(R.id.walletNameAnalysis);
        NoOfTransaction = itemView.findViewById(R.id.walletTransAnalysis);
    }
}
