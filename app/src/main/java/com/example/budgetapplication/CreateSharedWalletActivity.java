package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CreateSharedWalletActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final String TAG = "Shared WalletForm";
    EditText walletName;
    EditText walletBal;
    Button cancel;
    Button create;
    User user;
    SharedWallet w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shared_wallet);
        walletName = findViewById(R.id.accNameSharedWallet);
        walletBal = findViewById(R.id.accountBalSharedWallet);
        cancel = findViewById(R.id.cancelSharedWallet);
        create = findViewById(R.id.saveSharedWallet);
        user = (User) getIntent().getSerializableExtra("User");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtain data from field
                String accountName = walletName.getText().toString().trim();
                double walletBalance = Double.parseDouble(walletBal.getText().toString());
                //validate if wallet name is entered
                if(accountName.isEmpty())
                {
                    //display error message
                    walletName.setError("Please enter account name");
                    walletName.requestFocus();
                }

                else if(!accountName.isEmpty()){
                    //send data into database & update user object
                    String id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    w = new SharedWallet(accountName, walletBalance, id);
                    Transaction t = new Transaction("Initial Transactions", 0.00, "Initialisation");
                    w.addTransactions(t);

                    Query lastQuery = databaseReference.child("SharedWallets");
                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String previouswalletId = String.valueOf(dataSnapshot.getChildrenCount());
                            String walletId = ((Integer) (Integer.valueOf(previouswalletId) + 1)).toString();
                            Log.d(TAG, "onDataChange: " + walletId);
                            databaseReference.child("SharedWallets").child(walletId).setValue(w);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });
                    Toast.makeText(CreateSharedWalletActivity.this,"Shared Wallet Created Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }

                else{
                    Toast.makeText(CreateSharedWalletActivity.this,"Error creating shared wallet!",Toast.LENGTH_SHORT).show();
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
}