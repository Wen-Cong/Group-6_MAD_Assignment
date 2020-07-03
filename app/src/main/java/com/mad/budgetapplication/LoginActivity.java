package com.mad.budgetapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    final String TAG = "LoginActivity";
    public EditText emailId,password;
    Button btnSignIn;
    TextView signUp;
    //create firebase object
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //create firebase instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //find email from text input
        emailId = findViewById(R.id.email);
        //find password from text input
        password = findViewById((R.id.password));
        //button
        btnSignIn = findViewById(R.id.button);
        //link to sign in
        signUp = findViewById(R.id.signinlink);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                   Toast.makeText(LoginActivity.this,"You are logged in", Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                   startActivity(i);

                }
                else   {
                    Toast.makeText(LoginActivity.this,"Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
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
                else  if (pass.isEmpty())
                {
                    //set empty password error
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                //if both not empty then go ahead with signup
                else if (!(email.isEmpty() && pass.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Failed... Please Try Again", Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                //if some other error occurred then display this msg
                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChangeActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ChangeActivity);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
