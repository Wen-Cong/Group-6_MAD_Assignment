package com.example.budgetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

//  this java page is for activity_wallet_form
public class WalletFormActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    EditText accName;
    EditText accBal;
    Button Cancel;
    Button Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_form);
        accName = findViewById(R.id.accName);
        accBal = findViewById(R.id.accountBal);
        Cancel = findViewById(R.id.cancel);
        Save = findViewById(R.id.saveNow);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = accName.getText().toString().trim();
                double walletBalance = Double.parseDouble(accBal.getText().toString());
                if(accountName.isEmpty())
                {
                    accName.setError("Please enter account name");
                    accName.requestFocus();
                }

                else if(!accountName.isEmpty()){
                    //send data into database
                    String id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Wallet w = new Wallet(accountName, walletBalance);
                    databaseReference.child("Users").child(id).child("wallets").push().setValue(w);
                    Toast.makeText(WalletFormActivity.this,"Wallet Added Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }

                else{
                    Toast.makeText(WalletFormActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }

        });



    }
}
