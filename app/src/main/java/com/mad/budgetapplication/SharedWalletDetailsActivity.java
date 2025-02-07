package com.mad.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class SharedWalletDetailsActivity extends AppCompatActivity {
    private static final String TAG = "SharedWalletDetails";
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;
    private String uid;
    SharedWallet sharedWallet;
    String shareWalletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_wallet_details);
        bottomNavigationView = findViewById(R.id.sharedWalletbottomNavigation);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sharedWallet = (SharedWallet) getIntent().getSerializableExtra("sharedWallet");
        shareWalletId = getIntent().getStringExtra("sharedWalletId");

        //Change fragment upon clicking menu icons
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment frag = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sharedWalletDetailsHome:
                        frag = new SharedWalletHomeFragment();
                        break;
                    case R.id.sharedWallettransactions:
                        frag = new SharedWalletTransactionFragment();
                        break;
                    case R.id.sharedWalletDetailsParticipant:
                        frag = new SharedWalletParticipantFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.sharedWalletContainer,frag).commit();
                return true;
            }
        });

        //Set default fragment to Home page fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.sharedWalletContainer,new SharedWalletHomeFragment()).commit();

    }
}