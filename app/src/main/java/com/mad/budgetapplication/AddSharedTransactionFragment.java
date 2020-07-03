package com.mad.budgetapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class AddSharedTransactionFragment extends Fragment {
    Button cancel;
    Button add;
    EditText transactionName;
    EditText transactionAmount;
    Spinner type;
    SharedWallet wallet;
    DatabaseReference databaseReference;
    String uid;
    String walletId;
    private Double finalAmount;

    public AddSharedTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shared_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancel = view.findViewById(R.id.addsharedTransaction_cancel);
        add = view.findViewById(R.id.addsharedTransaction_submit);
        transactionName = view.findViewById(R.id.sharedtransactionName_edittext);
        transactionAmount = view.findViewById(R.id.sharedtransactionAmount_edittext);
        type = view.findViewById(R.id.sharedtransactionType_spinner);
        wallet = ((SharedWalletDetailsActivity) getActivity()).sharedWallet;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        walletId = ((SharedWalletDetailsActivity) getActivity()).shareWalletId;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.sharedWalletContainer,new SharedWalletTransactionFragment()).commit();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = transactionName.getText().toString().trim();
                String string_amt = transactionAmount.getText().toString().trim();
                String TransactionType = type.getSelectedItem().toString();
                //check if transaction name is empty
                if(name.isEmpty()){
                    //display error and bring focus to empty field
                    transactionName.setError("Please enter a name");
                    transactionName.requestFocus();
                }else if(!name.isEmpty()){
                    //validate if amount entered is empty
                    if(string_amt.isEmpty()){
                        //display error and bring focus to empty field
                        transactionAmount.setError("Please enter an amount");
                        transactionAmount.requestFocus();
                    }else if(!string_amt.isEmpty()){
                        //calculate new wallet balance from transaction added
                        Double amt = Double.parseDouble(string_amt);
                        if(TransactionType.equals("Expenses")) {
                            finalAmount = -1 * amt;
                        }else if (TransactionType.equals("Income")){
                            finalAmount = amt;
                        }
                        else{
                            finalAmount = 0.00;
                        }
                        Double newWalletBal =((Double) wallet.getBalance()) + finalAmount;
                        if(newWalletBal < 0.00){
                            Toast.makeText(getActivity(), "Invalid Amount!, Insufficient wallet balance", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //create new transaction and update wallet
                            wallet.setBalance(newWalletBal);
                            SharedTransaction st = new SharedTransaction(name, finalAmount, TransactionType, uid);
                            wallet.addSharedTransaction(st);

                            //update data to firebase database
                            Log.d(TAG, "onClick: wallet ID: " + walletId);
                            databaseReference.child("SharedWallets").child(walletId).setValue(wallet);
                            Toast.makeText(getActivity(), "Transaction Create Successfully", Toast.LENGTH_SHORT).show();
                            transactionAmount.getText().clear();
                            transactionName.getText().clear();

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.sharedWalletContainer,new SharedWalletTransactionFragment()).commit();
                        }
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