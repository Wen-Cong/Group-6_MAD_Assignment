package com.mad.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Date;

public class RecurringTransactionActivity extends AppCompatActivity {
    private Button submit;
    private Button cancel;
    DatabaseReference databaseReference;
    private EditText transactionName;
    private EditText transactionAmt;
    private Spinner fromWallet;
    private Spinner type;
    private Wallet wallet;
    private Double finalAmount;
    private String uid;
    private String walletKey;
    private final String TAG = "Add Recurring Transaction";
    private EditText rtDate;
    private EditText rtInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final User user = (User) getIntent().getSerializableExtra("User");
        ArrayList<Wallet> walletArrayList = user.getWallets();
        final ArrayAdapter walletAdapter = new ArrayAdapter(RecurringTransactionActivity.this, R.layout.spinner, walletArrayList);
        setContentView(R.layout.activity_recurring_transaction);
        submit = findViewById(R.id.addTransaction_submit3);
        cancel = findViewById(R.id.addTransaction_submit2);
        transactionName = findViewById(R.id.transactionName_edittext);
        transactionAmt = findViewById(R.id.transactionAmount_edittext);
        fromWallet = findViewById(R.id.wallet_spinner);
        type = findViewById(R.id.transactionType_spinner);
        rtDate = findViewById(R.id.RTDate);
        rtInterval = findViewById(R.id.RTDate2);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        //Clear edit text when set focus
        transactionAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    transactionAmt.setText("");
                }
            }
        });
        //create new transaction & add to firebase database
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                String name = transactionName.getText().toString().trim();
                String string_amt = transactionAmt.getText().toString().trim();
                String TransactionType = type.getSelectedItem().toString();
                String StartDate = rtDate.getText().toString();
                String StringInterval = rtInterval.getText().toString();


                //check if transaction name is empty
                if (name.isEmpty()) {
                    //display error and bring focus to empty field
                    transactionName.setError("Please enter a name");
                    transactionName.requestFocus();
                }
                else if (!name.isEmpty()) {
                    //validate if amount entered is empty
                    if (string_amt.isEmpty()) {
                        //display error and bring focus to empty field
                        transactionAmt.setError("Please enter an amount");
                        transactionAmt.requestFocus();
                    }
                    else if (!string_amt.isEmpty()) {

                        Double amt = Double.parseDouble(string_amt);
                        if (TransactionType.equals("Expenses")) {
                            finalAmount = -1 * amt;
                        } else if (TransactionType.equals("Income")) {
                            finalAmount = amt;
                        } else {
                            finalAmount = 0.00;
                        }
                        Double newWalletBal = ((Double) wallet.getBalance()) + finalAmount;
                        if (newWalletBal < 0.00) {
                            Toast.makeText(RecurringTransactionActivity.this, "Invalid Amount!, Insufficient wallet balance", Toast.LENGTH_SHORT).show();
                        }
                        if(StartDate.isEmpty()){
                            //display error and bring focus to empty field
                            transactionAmt.setError("Please enter date");
                            transactionAmt.requestFocus();
                        }
                        if(StringInterval.isEmpty()){
                            transactionAmt.setError("Please enter a number larger than zero");
                            transactionAmt.requestFocus();
                        }

                        else {
                            int Interval = Integer.parseInt(rtInterval.getText().toString());
                            if(Interval == 0){
                                //display error and bring focus to empty field
                                transactionAmt.setError("Please enter a number larger than zero");
                                transactionAmt.requestFocus();
                            }
                            //create new transaction and update wallet
                            RTransaction t = new RTransaction(name, Interval, StartDate, finalAmount, TransactionType);
                            wallet.addRTransaction(t);
                            //update data to firebase database
                            databaseReference.child("Users").child(uid).child("wallets").child(walletKey).setValue(wallet);
                            Toast.makeText(RecurringTransactionActivity.this, "Recurring Transaction Create Successfully", Toast.LENGTH_SHORT).show();
                            Intent userIntent = new Intent();
                            userIntent.putExtra("User", user);
                            setResult(1, userIntent);
                            Log.v(TAG, "Send result to HomeActivity");
                            finish();
                        }
                    } else {
                        Toast.makeText(RecurringTransactionActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RecurringTransactionActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //get wallet primary key in database with wallet name
    private void getWalletKey(Wallet w) {
        String walletname = w.getName().toString();
        databaseReference.child("Users").child(uid).child("wallets").orderByChild("name")
                .equalTo(walletname).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    walletKey = childSnapshot.getKey();
                    Log.v(TAG, walletKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecurringTransactionActivity.this, "Error retrieving walletId from database", Toast.LENGTH_SHORT);
            }
        });

    }
}
