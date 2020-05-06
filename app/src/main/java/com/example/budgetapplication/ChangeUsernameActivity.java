package com.example.budgetapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUsernameActivity extends AppCompatActivity {
    Button submit;
    EditText newUsername;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        submit = findViewById(R.id.usernameSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newUsername.getText().toString();
                if(!username.isEmpty()){
                    UpdateUsername(username);
                    Fragment accountFragment = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, accountFragment);
                }

            }
        });
    }

    private void UpdateUsername(String username){
        if(!username.isEmpty()){
            String id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference.child("Users").child(id).child("username").setValue(username);
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
