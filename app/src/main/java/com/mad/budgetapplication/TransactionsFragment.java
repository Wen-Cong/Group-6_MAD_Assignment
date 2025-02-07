package com.mad.budgetapplication;

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

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {
    User user;
    RecyclerView transactionView;
    RecyclerView assetView;
    RecyclerView RTransactionView;
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
        user = ((HomeActivity) getActivity()).user;
        transactionView = view.findViewById(R.id.transactionView);
        assetView = view.findViewById(R.id.assetView);
        RTransactionView = view.findViewById(R.id.RTransactionView);

        RTransactionAdapter rAdapter = new RTransactionAdapter(user, getActivity());
        LinearLayoutManager rLayoutManager = new LinearLayoutManager(getActivity());
        RTransactionView.setLayoutManager(rLayoutManager);
        RTransactionView.setItemAnimator(new DefaultItemAnimator());
        RTransactionView.setAdapter(rAdapter);

        //Create adapter and pass in the data for asset
        AssetAdapter mAdapter = new AssetAdapter(user, getActivity());
        //Layout manager tells recyclerview how to draw the list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //pass in layout, animation and adapter.
        assetView.setLayoutManager(mLayoutManager);
        assetView.setItemAnimator(new DefaultItemAnimator());
        assetView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ArrayList<Transaction> allTransactionsList = new ArrayList<Transaction>();
        //get all user created transaction into one single list
        for(Wallet w : user.getWallets()){
            for(Transaction t : w.getTransactions()){
                if(t.getType().equals("Income") || t.getType().equals("Expenses")){
                    allTransactionsList.add(t);
                    Log.v(TAG, t.getName() + " loaded to transaction history");
                }
            }
        }

        //sort the list in term of date, latest date at the front
        Collections.sort(allTransactionsList);
        Collections.reverse(allTransactionsList);

        //set view for recyclerview
        TransactionsAdapter adapter = new TransactionsAdapter(allTransactionsList, getActivity());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        transactionView.addItemDecoration(itemDecor);
        transactionView.setAdapter(adapter);
        transactionView.setLayoutManager(layoutManager);
        transactionView.setItemAnimator(new DefaultItemAnimator());


    }
}
