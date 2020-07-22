package com.mad.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class RTransactionViewHolder extends RecyclerView.ViewHolder {
    TextView txt;
    TextView value;
    public RTransactionViewHolder(View itemView){
        super(itemView);
        txt = itemView.findViewById(R.id.textView);
        value = itemView.findViewById(R.id.textView3);
    }
}
