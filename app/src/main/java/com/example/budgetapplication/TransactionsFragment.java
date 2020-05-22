package com.example.budgetapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {
    User user;
    RecyclerView transactionView;
    final String TAG = "Transaction History";
    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ((HomeActivity)this.getActivity()).getUser();
        transactionView = view.findViewById(R.id.transactionView);
        transactionView = view.findViewById(R.id.transactionView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ArrayList<Transaction> allTransactionsList = new ArrayList<Transaction>();

        for(Wallet w : user.getWallets()){
            for(Transaction t : w.getTransactions()){
                if(t.getType().equals("Income") || t.getType().equals("Expenses")){
                    allTransactionsList.add(t);
                    Log.v(TAG, t.getName() + " loaded to history");
                }
            }
        }

        TransactionsAdapter adapter = new TransactionsAdapter(allTransactionsList, getActivity());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        transactionView.addItemDecoration(itemDecor);
        transactionView.setAdapter(adapter);
        transactionView.setLayoutManager(layoutManager);
        transactionView.setItemAnimator(new DefaultItemAnimator());


    }
}
