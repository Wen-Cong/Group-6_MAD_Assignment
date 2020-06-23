package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class SharedWalletDetailsActivity extends AppCompatActivity {
    private static final String TAG = "SharedWalletDetails";
    DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;
    String uid;
    StorageReference storageReference;
    SharedWallet sharedWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_wallet_details);
        bottomNavigationView = findViewById(R.id.sharedWalletbottomNavigation);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sharedWallet = (SharedWallet) getIntent().getSerializableExtra("sharedWallet");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment frag = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sharedWalletDetailsHome:
                        frag = new sharedWalletHomeFragment();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.sharedWalletContainer,new sharedWalletHomeFragment()).commit();

    }
}