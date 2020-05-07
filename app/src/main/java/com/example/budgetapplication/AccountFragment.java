package com.example.budgetapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {
    private static final String TAG = "Account Activity";

    StorageReference storageReference;
    DatabaseReference databaseReference;
    CircleImageView profileImage;
    String userId;
    private LinearLayout changeusername;
    private  LinearLayout supportbutton;
    TextView username;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storageReference = FirebaseStorage.getInstance().getReference();
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportbutton = view.findViewById(R.id.linearlayout_3);
        changeusername = view.findViewById(R.id.changeusername);
        username = view.findViewById(R.id.username);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        profileImage = getView().findViewById(R.id.profile_pic);
        StorageReference profileRef =
                storageReference.child("users/"+ userId +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
        readUsername(userId);


        changeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangeUsernamePage();
            }
        });
        supportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSupportPage();
            }
        });
    }

    private void openChangeUsernamePage(){
        Intent intent = new Intent(getActivity(), ChangeUsernameActivity.class);
        startActivity(intent);
    }

    private void openSupportPage(){
        Intent intent = new Intent(getActivity(), SupportActivity.class);
        startActivity(intent);
    }

    public void readUsername(String id){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+ id +"/username");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String database_username = dataSnapshot.getValue().toString();
                username.setText(database_username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
