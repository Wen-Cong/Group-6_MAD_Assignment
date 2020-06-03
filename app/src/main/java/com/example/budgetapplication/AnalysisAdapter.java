package com.example.budgetapplication;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisViewHolder> {
    ArrayList<Wallet> wallets;
    public AnalysisAdapter(ArrayList<Wallet> input){
        wallets = input;
    }

    @NonNull
    @Override
    public AnalysisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.analaysisview, parent, false);
        AnalysisViewHolder holder = new AnalysisViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisViewHolder holder, int position) {
        Wallet w = wallets.get(position);
        int walletTrans = 0;
        //calculate number of transaction in the current month for each wallet
        for(Transaction t : w.getTransactions()){
            //get current month
            SimpleDateFormat monthlyformat = new SimpleDateFormat("MM");
            Date date = new Date();
            String currentMonth = monthlyformat.format(date);
            SimpleDateFormat favWdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                //find wallet with most transaction in the same month as current month
                Date favWalletTransactionDate = favWdf.parse(t.getTime());
                String transactionMonth = monthlyformat.format(favWalletTransactionDate);
                if(transactionMonth.equals(currentMonth)){
                    walletTrans++;
                }
            }
            catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
        }
        holder.walletName.setText(w.getName());
        holder.NoOfTransaction.setText("" + walletTrans);
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }
}
