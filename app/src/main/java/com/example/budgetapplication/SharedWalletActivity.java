package com.example.budgetapplication;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_wallet);

        user = (User) getIntent().getSerializableExtra("User");
        addwallet = findViewById(R.id.addSharedWallet);
        joinWallet = findViewById(R.id.joinSharedWallet);
        recyclerView = findViewById(R.id.sharedWalletRecyclerView);
        reference = FirebaseDatabase.getInstance().getReference();
        wallets = new ArrayList<>();

        InitSharedWallets();

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

    private void loadRecyclerView() {
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
                for(String paticipatedWallet : user.getParticipatedSharedWallet()){
                    ArrayList<String> participants = new ArrayList<>();
                    ArrayList<Transaction> transactions = new ArrayList<>();
                    for(DataSnapshot participant : dataSnapshot.child(paticipatedWallet).child("participants").getChildren()){
                        participants.add(participant.getValue().toString());
                    }
                    for(DataSnapshot transaction : dataSnapshot.child(paticipatedWallet).child("transactions").getChildren()){
                        Transaction t = new Transaction(transaction.child("name").getValue().toString(),
                                Double.valueOf(transaction.child("amount").getValue().toString()),
                                transaction.child("type").getValue().toString(), transaction.child("time").getValue().toString());

                        transactions.add(t);
                    }
                    SharedWallet sw = new SharedWallet(dataSnapshot.child(paticipatedWallet).child("name").getValue().toString(),
                            Double.valueOf(dataSnapshot.child(paticipatedWallet).child("balance").getValue().toString()),
                            dataSnapshot.child(paticipatedWallet).child("adminId").getValue().toString(),
                            participants, transactions);
                    wallets.add(sw);
                    Log.d(TAG, "onDataChange: " + sw.getName() + " loaded!");
                }

                loadRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
            }
        };

        reference.child("SharedWallets").addValueEventListener(eventListener);

    }

    private void openJoinWallet() {
        Intent intent = new Intent(SharedWalletActivity.this, JoinWalletActivity.class);
        intent.putExtra("User", user);
        startActivityForResult(intent, REQ_JOINSHAREDWALLET_CODE);
    }

    private void openCreateWallet() {
        Intent intent = new Intent(SharedWalletActivity.this, CreateSharedWalletActivity.class);
        intent.putExtra("User", user);
        startActivityForResult(intent, REQ_CREATESHAREDWALLET_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CREATESHAREDWALLET_CODE || requestCode == REQ_JOINSHAREDWALLET_CODE){
            if(resultCode == 1){
                user = (User) data.getSerializableExtra("User");

                Intent userIntent = new Intent();
                userIntent.putExtra("User", data.getSerializableExtra("User"));
                setResult(1, userIntent);
                Log.v(TAG, "Send user data to HomeActivity from SharedWallet");
            }
        }
    }
}