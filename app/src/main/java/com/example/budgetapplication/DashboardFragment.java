package com.example.budgetapplication;

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
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private TextView transaName;
    private TextView transaAmt;
    private TextView transaWallet;
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
        User user = ((HomeActivity) getActivity()).user;
        Date t_date;
        Date lastest_date;
        Wallet lastestUsedWallet = new Wallet();
        Log.v("Dashboard","Username: " + user.getUserName());
        Transaction lastesttransaction = user.getWallets().get(0).getTransactions().get(0);


        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            lastest_date = df.parse(lastesttransaction.getTime());
            for(Wallet w : user.getWallets()){
                for(Transaction t : w.getTransactions()){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    try {
                        t_date = sdf.parse(t.getTime());
                        if(t_date.after(lastest_date)){
                            lastesttransaction = t;
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

        transaName.setText(lastesttransaction.getName());
        transaAmt.setText("Amount: " + lastesttransaction.getAmount());
        transaWallet.setText("Wallet: " + lastestUsedWallet.getName());
    }

}
