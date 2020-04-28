package com.example.budgetapplication;

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

public class MainActivity extends AppCompatActivity {
    public EditText emailId,password;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                else  if (pass.isEmpty())
                {
                    //set empty password error
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                //if both not empty then go ahead with signup
                else if (!(email.isEmpty() && pass.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if signup not successful display message
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Sign Up Unsuccessful... Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            //else go to HomeActivity!
                            else {
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));


                            }
                        }
                    });
                }
                //if some other error occurred then display this msg
                else{
                    Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });



        //when sign in link is clicked instead

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
