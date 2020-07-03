package com.mad.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SharedWalletViewHolder extends RecyclerView.ViewHolder {
    TextView walletName;
    TextView walletBal;
    ConstraintLayout layout;

    public SharedWalletViewHolder(@NonNull View itemView) {
        super(itemView);
        walletBal = itemView.findViewById(R.id.sharedWalletBal);
        walletName = itemView.findViewById(R.id.sharedWalletName);
        layout = itemView.findViewById(R.id.sharedwalletLayout);
    }
}
