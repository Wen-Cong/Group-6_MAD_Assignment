package com.example.budgetapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SharedWalletActivity extends AppCompatActivity {
    private static final String TAG = "SharedWallet Activity";
    User user;
    Button addwallet;
    Button joinWallet;
    public final static int REQ_CREATESHAREDWALLET_CODE = 3001;
    public final static  int REQ_JOINSHAREDWALLET_CODE = 4001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_wallet);

        user = (User) getIntent().getSerializableExtra("User");
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
            }
        }
    }
}