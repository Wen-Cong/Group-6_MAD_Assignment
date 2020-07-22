package com.mad.budgetapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AssetViewHolder extends RecyclerView.ViewHolder {
    TextView txt;
    TextView value;
    public AssetViewHolder(View itemView){
        super(itemView);
        txt = itemView.findViewById(R.id.textView);
        value = itemView.findViewById(R.id.textView3);
    }
}
