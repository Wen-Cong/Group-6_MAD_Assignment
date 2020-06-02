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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String uid;
    private String walletKey;
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
        //get user data from home activity
        User user = ((HomeActivity)this.getActivity()).getUser();
        Log.d(TAG, "onViewCreated: " + user);
        ArrayList<Wallet> walletArrayList = user.getWallets();
        final ArrayAdapter walletAdapter = new ArrayAdapter(getActivity(), R.layout.spinner, walletArrayList);
        submit = view.findViewById(R.id.addTransaction_submit);
        type = view.findViewById(R.id.transactionType_spinner);
        transactionName = view.findViewById(R.id.transactionName_edittext);
        transactionAmt = view.findViewById(R.id.transactionAmount_edittext);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fromWallet = view.findViewById(R.id.wallet_spinner);

        //populate wallets to spinner from wallet list in user object
        fromWallet.setAdapter(walletAdapter);
        fromWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get wallet data from selected wallet
                wallet = (Wallet) parent.getSelectedItem();
                getWalletKey(wallet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //create new transaction & add to firebase database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = transactionName.getText().toString().trim();
                String string_amt = transactionAmt.getText().toString().trim();
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
                        transactionAmt.setError("Please enter an amount");
                        transactionAmt.requestFocus();
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
                            Transaction t = new Transaction(name, finalAmount, TransactionType);
                            wallet.addTransactions(t);
                            //update data to firebase database
                            databaseReference.child("Users").child(uid).child("wallets").child(walletKey).setValue(wallet);
                            Toast.makeText(getActivity(), "Transaction Create Successfully", Toast.LENGTH_SHORT).show();
                            transactionAmt.getText().clear();
                            transactionName.getText().clear();
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
    //get wallet primary key in database with wallet name
    private void getWalletKey(Wallet w) {
        String walletname = w.getName().toString();
        databaseReference.child("Users").child(uid).child("wallets").orderByChild("name")
                .equalTo(walletname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    walletKey = childSnapshot.getKey();
                    Log.v(TAG, walletKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error retrieving walletId from database", Toast.LENGTH_SHORT);
            }
        });
    }
}
