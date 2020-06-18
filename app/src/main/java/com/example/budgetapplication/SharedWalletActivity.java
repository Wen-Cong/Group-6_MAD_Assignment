package com.example.budgetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SharedWalletActivity extends AppCompatActivity {
    User user;
    Button addwallet;
    Button joinWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_wallet);

        Intent intent = new Intent();
        user = (User) intent.getSerializableExtra("User");
        addwallet = findViewById(R.id.addSharedWallet);
        joinWallet = findViewById(R.id.joinSharedWallet);

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

    private void openJoinWallet() {
        Intent intent = new Intent(SharedWalletActivity.this, JoinWalletActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    private void openCreateWallet() {
        Intent intent = new Intent(SharedWalletActivity.this, CreateSharedWalletActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}