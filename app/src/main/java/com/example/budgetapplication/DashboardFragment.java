package com.example.budgetapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private TextView transaName;
    private TextView transaAmt;
    private TextView transaWallet;
    private TextView transaDate;
    private TextView walletName;
    private TextView walletAmt;
    private TextView walletNoTrans;
    private final String TAG = "Dashboard";

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transaName = view.findViewById(R.id.dTransName);
        transaAmt = view.findViewById(R.id.dAmt);
        transaWallet = view.findViewById(R.id.dTWalletName);
        transaDate = view.findViewById(R.id.dDate);
        walletName = view.findViewById(R.id.fWallet);
        walletAmt = view.findViewById(R.id.fWalletAmt);
        walletNoTrans = view.findViewById(R.id.fWalletTrans);
        User user = ((HomeActivity) getActivity()).user;
        int walletTrans = 0;
        int favWalletTrans = 0;
        Date t_date;
        Date lastest_date;
        Wallet lastestUsedWallet = new Wallet();
        Transaction latestTransaction = user.getWallets().get(0).getTransactions().get(0);
        Wallet favWallet = user.getWallets().get(0);


        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            for(Wallet w : user.getWallets()){
                for(Transaction t : w.getTransactions()){
                    lastest_date = df.parse(latestTransaction.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    try {
                        t_date = sdf.parse(t.getTime());
                        if(t_date.after(lastest_date)){
                            latestTransaction = t;
                            lastestUsedWallet = w;
                        }
                    } catch (ParseException ex) {
                        Log.v("Exception", ex.getLocalizedMessage());
                    }

                }
            }
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage() + " initial exception");
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date d = format.parse(latestTransaction.getTime());
            SimpleDateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
            String formatedDate = dformat.format(d);
            transaName.setText(latestTransaction.getName());
            transaAmt.setText("Amount: " + latestTransaction.getAmount());
            if(latestTransaction.getType().equals("Expenses")){
                transaAmt.setTextColor(Color.RED);
            }else if(latestTransaction.getType().equals("Income")){
                transaAmt.setTextColor(Color.GREEN);
            }
            transaWallet.setText("Wallet: " + lastestUsedWallet.getName());
            transaDate.setText(formatedDate);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
            transaName.setText(latestTransaction.getName());
            transaAmt.setText("Amount: " + latestTransaction.getAmount());
            transaWallet.setText("Wallet: " + lastestUsedWallet.getName());
            transaDate.setText(latestTransaction.getTime());
        }


        for(Wallet w : user.getWallets()){
            walletTrans = 0;
            for(Transaction t : w.getTransactions()){
                SimpleDateFormat monthlyformat = new SimpleDateFormat("MM");
                Date date = new Date();
                String currentMonth = monthlyformat.format(date);
                SimpleDateFormat favWdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    Date favWalletTransactionDate = favWdf.parse(t.getTime());
                    String transactionMonth = monthlyformat.format(favWalletTransactionDate);
                    if(transactionMonth.equals(currentMonth)){
                        walletTrans++;
                        if(walletTrans > favWalletTrans){
                            favWalletTrans = walletTrans;
                            favWallet = w;
                        }
                    }
                }
                catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }
            }
            walletNoTrans.setText("No. of Transaction (Monthly): " + walletTrans);
        }
        walletName.setText(favWallet.getName());
        walletAmt.setText("Balance: $" + favWallet.getBalance());

    }

}
