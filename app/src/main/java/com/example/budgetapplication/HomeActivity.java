package com.example.budgetapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    CircleImageView profileImage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        storageReference = (StorageReference) FirebaseStorage.getInstance().getReference();

        StorageReference profileRef =
                storageReference.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        profileImage = (CircleImageView) findViewById(R.id.profile_pic);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            //@Override
            Fragment frag = null;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new DashboardFragment()).commit();

        //Add profile image for account fragment
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 1001);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                UploadProfileImage(imageUri);
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
                        //load the uploaded image from firebase as profileImage
                        Picasso.get().load(uploadedUri).into(profileImage);
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

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intToMain);
    }
}
