package com.example.budgetapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    DatabaseReference databaseReference;
    private EditText transactionName;
    private EditText transactionAmt;
    private Spinner fromWallet;
    private Wallet wallet;
    private Button submit;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent getUser = getActivity().getIntent();
        //Issues with getting user from home activity
        //User user = (User) getUser.getSerializableExtra("User");
        User user = new User();
        Wallet w = new Wallet("Cash", 20.0);
        user.addWallet(w);
        System.out.println(user);
        // ^^^ Above code is for testing purposes ^^^ Do not remove!
        ArrayList<Wallet> walletArrayList = user.getWallets();
        final ArrayAdapter walletAdapter = new ArrayAdapter(getActivity(), R.layout.spinner, walletArrayList);
        submit = view.findViewById(R.id.addTransaction_submit);
        transactionName = view.findViewById(R.id.transactionName_edittext);
        transactionAmt = view.findViewById(R.id.transactionAmount_edittext);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fromWallet = view.findViewById(R.id.wallet_spinner);
        fromWallet.setAdapter(walletAdapter);
        fromWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wallet = (Wallet) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = transactionName.getText().toString().trim();
                Double amt = Double.parseDouble(transactionAmt.getText().toString().trim());

            }
        });
    }
}
