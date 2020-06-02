package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    StorageReference storageReference;
    FirebaseDatabase database;
    private boolean flag = false;



    CircleImageView tempProfileImage;
    public User user;
    public final static int REQ_WALLET_CODE = 2001;
    public final static  int REQ_PROFILEPIC_CODE = 1001;
    private static final String TAG = "HomeActivity";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        storageReference = FirebaseStorage.getInstance().getReference();
        //initiate default user to prevent null exception in case InitUser fail
        user = new User();


        //switch to different fragment with respective of selected icon in bottom Nav bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment frag = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        frag = new DashboardFragment();
                        break;
                    case R.id.wallet:
                        frag = new WalletFragment();
                        break;
                    case R.id.add:
                        frag = new AddFragment();
                        break;
                    case R.id.transactions:
                        frag = new TransactionsFragment();
                        break;
                    case R.id.account:
                        frag = new AccountFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        InitUser();
    }

    public User getUser(){
        return user;
    }

    //open wallet creation form
    public void openWalletForm(View view) {
        Intent createAccForm = new Intent(HomeActivity.this, WalletFormActivity.class);
        createAccForm.putExtra("User", user);
        startActivityForResult(createAccForm, REQ_WALLET_CODE);
    }


    //redirect to login page and sign out
    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intToMain);
    }

    //get picture from gallery
    public void ProfileImageHandler(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQ_PROFILEPIC_CODE);
        tempProfileImage = findViewById(view.getId());
        tempProfileImage.setImageURI(null);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PROFILEPIC_CODE){
            if(resultCode == Activity.RESULT_OK){
                //update storage and profile pic view with image uploaded from gallery
                Uri imageUri = data.getData();
                tempProfileImage.setImageURI(imageUri);

                UploadProfileImage(imageUri);
            }
        }
        else if(requestCode == REQ_WALLET_CODE){
            if(resultCode == 1){
                //get updated user data from wallet form when new wallet created
                Log.v(TAG, "user with updated wallet received from wallet form");
                user = (User) data.getSerializableExtra("User");
            }
        }
    }


    private void UploadProfileImage(Uri imageUri) {
        //Upload profile image to firebase storage
        final StorageReference imageRef =
                storageReference.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() +"/profile.jpg");

        //Uploading image to declared storage reference
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uploadedUri) {
                        Toast.makeText(HomeActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void InitUser(){
        //obtain database and user ID
        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentId);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //initiate username
                String userName = dataSnapshot.child("username").getValue().toString();
                user = new User(currentId, userName);
                //initiate wallets & transactions
                for(DataSnapshot walletId : dataSnapshot.child("wallets").getChildren())
                {
                    Wallet newWallet = new Wallet(walletId.child("name").getValue().toString(), Double.valueOf(walletId.child("balance").getValue().toString()));
                    Log.v(TAG, "Wallet Name: "+ newWallet.toString());
                    //get transactions from database and populate transactions list into respective wallets
                    for(DataSnapshot transactionId : walletId.child("transactions").getChildren()){
                        Transaction t = new Transaction(transactionId.child("name").getValue().toString(),
                                Double.valueOf(transactionId.child("amount").getValue().toString()),
                        transactionId.child("type").getValue().toString(), transactionId.child("time").getValue().toString());
                        newWallet.addTransactions(t);
                        Log.v(TAG, "Transactions: " + t.getName().toString() + " loaded!");
                    }
                    user.addWallet(newWallet);
                }
                Log.v(TAG, userName);
                //change to dashboard view after initialising user data for the first time
                if(!flag){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new DashboardFragment()).commit();
                    flag = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG, "Error getting database data");
            }
        });
    }

}
