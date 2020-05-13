package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public EditText emailId, password;
    Button btnSignUp;
    TextView signIn;
    //create firebase object
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create firebase instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //find email from text input
        emailId = findViewById(R.id.email);
        //find password from text input
        password = findViewById((R.id.password));
        //button
        btnSignUp = findViewById(R.id.button);
        //link to sign in
        signIn = findViewById(R.id.signinlink);


        //when button is clicked
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            //create onclick for sign up button
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pass = password.getText().toString();
                //check whether email is empty
                if (email.isEmpty()) {
                    //set empty password error
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                }
                //check whether password is empty
                //it is then
                else if (pass.isEmpty()) {
                    //set empty password error
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                //if both not empty then go ahead with signup
                else if (!(email.isEmpty() && pass.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if sign up not successful display message
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Sign Up Unsuccessful... Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                            //else go to HomeActivity!
                            else {
                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference.child("Users").child(id).child("username").setValue("username");
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                        }
                    });
                }
                //if some other error occurred then display this msg
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //when sign in link is clicked instead

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }


    public void OnBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
