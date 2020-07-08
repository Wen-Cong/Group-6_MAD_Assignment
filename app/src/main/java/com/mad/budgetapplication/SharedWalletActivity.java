package com.mad.budgetapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SharedWalletActivity extends AppCompatActivity {
    private static final String TAG = "SharedWallet Activity";
    User user;
    Button addwallet;
    Button joinWallet;
    RecyclerView recyclerView;
    DatabaseReference reference;
    ArrayList<SharedWallet> wallets;
    public final static int REQ_CREATESHAREDWALLET_CODE = 3001;
    public final static  int REQ_JOINSHAREDWALLET_CODE = 4001;
    public final static  int REQ_VIEWSHAREDWALLET_CODE = 5001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_wallet);

        user = (User) getIntent().getSerializableExtra("User");
        addwallet = findViewById(R.id.addSharedWallet);
        joinWallet = findViewById(R.id.joinSharedWallet);
        recyclerView = findViewById(R.id.sharedWalletRecyclerView);
        reference = FirebaseDatabase.getInstance().getReference();

        addwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateWallet();
            }
        });
        joinWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJoinWallet();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        wallets = new ArrayList<>();
        InitSharedWallets();
    }

    private void loadRecyclerView() {
        //Set recycler view to display wallets
        SharedWalletAdapter adapter = new SharedWalletAdapter(this, wallets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(itemDecor);
    }

    private void InitSharedWallets() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get all wallet details of user's participated wallet
                for(String participatedWallet : user.getParticipatedSharedWallet()){
                    ArrayList<String> participants = new ArrayList<>();
                    ArrayList<SharedTransaction> transactions = new ArrayList<>();
                    // Get all participants of the wallet
                    for(DataSnapshot participant : dataSnapshot.child(participatedWallet).child("participants").getChildren()){
                        participants.add(participant.getValue().toString());
                    }
                    // Get all transaction of the wallet
                    for(DataSnapshot transaction : dataSnapshot.child(participatedWallet).child("sharedTransaction").getChildren()){
                        SharedTransaction t = new SharedTransaction(transaction.child("name").getValue().toString(),
                                Double.valueOf(transaction.child("amount").getValue().toString()),
                                transaction.child("type").getValue().toString(), transaction.child("time").getValue().toString(),
                                transaction.child("uid").getValue().toString());

                        if(!t.getType().equals("Initialisation")){
                            transactions.add(t);
                        }
                    }
                    // Get details of the shared wallet
                    SharedWallet sw = new SharedWallet(dataSnapshot.child(participatedWallet).child("name").getValue().toString(),
                            Double.valueOf(dataSnapshot.child(participatedWallet).child("balance").getValue().toString()),
                            dataSnapshot.child(participatedWallet).child("adminId").getValue().toString(),
                            dataSnapshot.child(participatedWallet).child("password").getValue().toString(), participants, transactions);
                    //Populate into a list
                    wallets.add(sw);
                    Log.v(TAG, "onDataChange: " + sw.getName() + " loaded!");
                }

                //Display data initiated from database
                loadRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
            }
        };

        reference.child("SharedWallets").addListenerForSingleValueEvent(eventListener);

    }

    private void openJoinWallet() {
        // Go join wallet page
        Intent intent = new Intent(SharedWalletActivity.this, JoinWalletActivity.class);
        intent.putExtra("User", user);
        startActivityForResult(intent, REQ_JOINSHAREDWALLET_CODE);
    }

    private void openCreateWallet() {
        // Go to create wallet page
        Intent intent = new Intent(SharedWalletActivity.this, CreateSharedWalletActivity.class);
        intent.putExtra("User", user);
        startActivityForResult(intent, REQ_CREATESHAREDWALLET_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CREATESHAREDWALLET_CODE || requestCode == REQ_JOINSHAREDWALLET_CODE || requestCode == REQ_VIEWSHAREDWALLET_CODE){
            if(resultCode == 1){
                // Get user data from CreateWalletActivity and JoinWalletActivity
                user = (User) data.getSerializableExtra("User");

                //Send result back to HomeActivity
                Intent userIntent = new Intent();
                userIntent.putExtra("User", data.getSerializableExtra("User"));
                setResult(1, userIntent);
                Log.v(TAG, "Send user data to HomeActivity from SharedWallet");
            }
        }
    }
}