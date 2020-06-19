package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinWalletActivity extends AppCompatActivity {
    private static final String TAG = "Join Wallet Activity";
    User user;
    Button join;
    Button cancel;
    EditText enter_walletId;
    DatabaseReference reference;
    String walletId;
    Boolean flag;
    ArrayList<String> walletParticipantList;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_wallet);
        join = findViewById(R.id.submit_joinwallet);
        cancel = findViewById(R.id.cancel_joinWallet);
        enter_walletId = findViewById(R.id.join_walletId);
        reference = FirebaseDatabase.getInstance().getReference();
        user = (User) getIntent().getSerializableExtra("User");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletId = enter_walletId.getText().toString().trim();
                flag = false;
                if(walletId.isEmpty()){
                    enter_walletId.requestFocus();
                    enter_walletId.setError("Please enter a Wallet ID");
                }
                else{
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot wId : dataSnapshot.child("User").child(uid).child("participatedWallet").getChildren()){
                                if(wId.getValue().toString().equals(walletId)){

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if(user.getParticipatedSharedWallet().contains(walletId)){
                        Toast.makeText(JoinWalletActivity.this, "You are already a member of this wallet", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        walletParticipantList = new ArrayList<>();
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot sharedwallet : dataSnapshot.child("SharedWallets").getChildren()){
                                    if(walletId.equals(sharedwallet.getKey())){
                                        flag = true;
                                        user.addParticipatedWallet(sharedwallet.getKey());
                                        reference.child("Users").child(uid).child("participatedWallet").setValue(user.getParticipatedSharedWallet());

                                        for (DataSnapshot participant : dataSnapshot.child("SharedWallets")
                                                .child(sharedwallet.getKey()).child("participants").getChildren()){

                                            walletParticipantList.add(participant.getValue().toString());
                                        }
                                        break;
                                    }
                                }

                                if(!flag) {
                                    Toast.makeText(JoinWalletActivity.this, "Wallet Id Not Found!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    walletParticipantList.add(uid);
                                    reference.child("SharedWallets").child(walletId)
                                            .child("participants").setValue(walletParticipantList);

                                    Log.v(TAG, "Participated wallet: " +
                                            user.getParticipatedSharedWallet().get(user.getParticipatedSharedWallet().size()-1));
                                    Toast.makeText(JoinWalletActivity.this, "Wallet Joined Successfully", Toast.LENGTH_SHORT).show();

                                    Intent userIntent = new Intent();
                                    userIntent.putExtra("User", user);
                                    setResult(1, userIntent);
                                    Log.v(TAG, "Send result to SharedWalletActivity");
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.v(TAG, "Error getting database data");
                            }
                        });
                    }
                }
            }
        });

    }
}