package com.mad.budgetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//  this java page is for activity_wallet_form
public class WalletFormActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final String TAG = "WalletForm";
    EditText accName;
    EditText accBal;
    Button Cancel;
    Button Save;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_form);
        accName = findViewById(R.id.accName);
        accBal = findViewById(R.id.accountBal);
        Cancel = findViewById(R.id.cancel);
        Save = findViewById(R.id.saveNow);
        user = (User) getIntent().getSerializableExtra("User");


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtain data from field
                String accountName = accName.getText().toString().trim();
                double walletBalance = Double.parseDouble(accBal.getText().toString());
                //validate if wallet name is entered
                if(accountName.isEmpty())
                {
                    //display error message
                    accName.setError("Please enter account name");
                    accName.requestFocus();
                }

                else if(!accountName.isEmpty()){
                    //send data into database & update user object
                    String id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Wallet w = new Wallet(accountName, walletBalance);
                    Transaction t = new Transaction("Initial Transactions", 0.00, "Initialisation");
                    w.addTransactions(t);
                    user.addWallet(w);
                    String walletId = databaseReference.child("Users").child(id).child("wallets").push().getKey();
                    databaseReference.child("Users").child(id).child("wallets").child(walletId).setValue(w);
                    Toast.makeText(WalletFormActivity.this,"Wallet Added Successfully",Toast.LENGTH_SHORT).show();
                    Log.v(TAG, user.getWallets().get(0).getName().toString() + " is added successfully");
                    Intent userIntent = new Intent();
                    userIntent.putExtra("User", user);
                    setResult(1, userIntent);
                    Log.v(TAG, "Send result to HomeActivity");
                    finish();
                }

                else{
                    Toast.makeText(WalletFormActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }

        });



    }
}
