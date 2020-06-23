package com.example.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedWalletTransactionViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profile_pic;
    TextView transactionName;
    TextView username;
    TextView dateTime;
    TextView amt;

    public SharedWalletTransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        profile_pic = itemView.findViewById(R.id.st_profile_pic);
        transactionName = itemView.findViewById(R.id.stname);
        dateTime = itemView.findViewById(R.id.stdate);
        username = itemView.findViewById(R.id.stusername);
        amt = itemView.findViewById(R.id.stamt);
    }
}
