package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    StorageReference storageReference;
    CircleImageView tempProfileImage;
    public User user;
    public final static int REQ_USER_CODE = 2001;
    private static final String TAG = "HomeActivity";
    Button addButton;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = new User();
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        storageReference = FirebaseStorage.getInstance().getReference();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new DashboardFragment()).commit();
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
                return false;
            }
        });

        Intent walletForm = new Intent(getPackageName() , Uri.parse("com.example.budgetapplication.WalletFormActivity"));
        startActivityForResult(walletForm, REQ_USER_CODE);

    }

    public User getUser(){
        return user;
    }

//open wallet creation form
    public void openWalletForm(View view) {
        Intent createAccForm = new Intent(HomeActivity.this, WalletFormActivity.class);
        createAccForm.putExtra("User", user);
        startActivity(createAccForm);
    }


    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intToMain);
    }

    public void ProfileImageHandler(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1001);
        tempProfileImage = findViewById(view.getId());
        tempProfileImage.setImageURI(null);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                tempProfileImage.setImageURI(imageUri);

                UploadProfileImage(imageUri);
            }
        }
        else if(requestCode == REQ_USER_CODE){
            if(resultCode == 1){
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
}
