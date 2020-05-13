package com.example.budgetapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    private Spinner type;
    private Wallet wallet;
    private Button submit;
    private  Double finalAmount;
    private final String TAG = "Add Transaction";

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
        //Issues with getting user from home activity
        User user = ((HomeActivity)this.getActivity()).getUser();
        Log.d(TAG, "onViewCreated: " + user);
        ArrayList<Wallet> walletArrayList = user.getWallets();
        final ArrayAdapter walletAdapter = new ArrayAdapter(getActivity(), R.layout.spinner, walletArrayList);
        submit = view.findViewById(R.id.addTransaction_submit);
        type = view.findViewById(R.id.transactionType_spinner);
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
                String string_amt = transactionAmt.getText().toString().trim();
                String TransactionType = type.getSelectedItem().toString();
                if(name.isEmpty()){
                    transactionName.setError("Please enter a name");
                    transactionName.requestFocus();
                }else if(!name.isEmpty()){
                    if(string_amt.isEmpty()){
                        transactionAmt.setError("Please enter an amount");
                        transactionAmt.requestFocus();
                    }else if(!string_amt.isEmpty()){
                        Double amt = Double.parseDouble(string_amt);
                        if(TransactionType.equals("Expenses")) {
                            finalAmount = -1 * amt;
                        }else if (TransactionType.equals("Income")){
                            finalAmount = amt;
                        }
                        Transaction t = new Transaction(name, finalAmount,wallet, TransactionType);
                        String id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Users").child(id).child("Transactions").push().setValue(t);
                        Toast.makeText(getActivity(), "Transaction Create Successfully", Toast.LENGTH_SHORT).show();
                        transactionAmt.getText().clear();
                        transactionName.getText().clear();
                    }
                    else{
                        Toast.makeText(getActivity(),"Error Occurred!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
