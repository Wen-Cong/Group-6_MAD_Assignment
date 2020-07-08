package com.mad.budgetapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class SharedWalletTransactionFragment extends Fragment {
    private static final String TAG = "sharedWalletTransaction";
    private RecyclerView recyclerView;
    private FloatingActionButton addSharedTransaction;
    private SharedWallet wallet;
    ArrayList<SharedTransaction> transactions;


    public SharedWalletTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get shared transactions and sort them in terms of newest at the front
        wallet = ((SharedWalletDetailsActivity) getActivity()).sharedWallet;
        transactions = wallet.getSharedTransaction();
        Collections.sort(transactions);
        Collections.reverse(transactions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared_wallet_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.sharedTransaction);
        addSharedTransaction = view.findViewById(R.id.addSharedTransaction);

        //Set recycler view to display transactions
        SharedWalletTransactionAdapter adapter = new SharedWalletTransactionAdapter(transactions, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Open add shared transaction page
        addSharedTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.sharedWalletContainer,new AddSharedTransactionFragment()).commit();
            }
        });
    }
}