package com.mad.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class RTransactionViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView date;
    TextView amt;
    public RTransactionViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.RTName);
        date = itemView.findViewById(R.id.RTDate);
        amt = itemView.findViewById(R.id.RTAmt);
    }
}
