package com.mad.budgetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.jar.Attributes;

public class AddAssetActivity extends AppCompatActivity {

    private User user;
    private Button submitButton;
    private Button cancelButton;
    private TextView desc;
    private TextView value;

    private String TAG = "AddAssetActivity";
    DatabaseReference databaseReference;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset);

        user = (User) getIntent().getSerializableExtra("User");
        submitButton = findViewById(R.id.submitAsset);
        cancelButton = findViewById(R.id.cancelAsset);
        desc = findViewById(R.id.assetName);
        value = findViewById(R.id.assetValue);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NameAsset = desc.getText().toString();
                double ValueAsset = Double.parseDouble(value.getText().toString());
                if (NameAsset.isEmpty()) {
                    desc.setError("Please Enter The Description/Name of Asset");
                    desc.requestFocus();
                }
                if(ValueAsset == 0){
                    value.setError("Value of Asset cannot be zero");
                    desc.requestFocus();
                }
                else {
                    Asset newAsset = new Asset(NameAsset, ValueAsset);
                    user.addAsset(newAsset);
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //update data to firebase database
                    String assetID = databaseReference.child("Users").child(uid).child("assets").push().getKey();
                    databaseReference.child("Users").child(uid).child("assets").child(assetID).setValue(newAsset);
                    Toast.makeText(AddAssetActivity.this,"Asset Added Successfully",Toast.LENGTH_SHORT).show();
                    Log.v(TAG, user.getAssetList().get(0).getName() + " is added successfully");

                    Intent userIntent = new Intent();
                    userIntent.putExtra("User", user);
                    setResult(1, userIntent);
                    Log.v(TAG, "Send result to HomeActivity");
                    finish();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}