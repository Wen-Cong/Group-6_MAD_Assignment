package com.mad.budgetapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SharedWalletParticipantFragment extends Fragment {
    private static final String TAG = "sharedWalletParticipant";
    SharedWallet wallet;
    RecyclerView recyclerView;
    Button leave;
    String uid;
    ArrayList<String> walletParticipantList;
    ArrayList<String> userParticipatedWallet;
    DatabaseReference databaseReference;
    String shareWalletId;


    public SharedWalletParticipantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        wallet = ((SharedWalletDetailsActivity) getActivity()).sharedWallet;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        shareWalletId = ((SharedWalletDetailsActivity) getActivity()).shareWalletId;
        return inflater.inflate(R.layout.fragment_shared_wallet_participant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.participantRecyclerView);
        leave = view.findViewById(R.id.leaveWallet);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        SharedWalletParticipantAdapter adapter = new SharedWalletParticipantAdapter(wallet.getParticipants(),
                wallet, getActivity());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(itemDecoration);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.equals(wallet.getAdminId())){
                    Toast.makeText(getActivity(), "Please assign another admin before leaving this wallet", Toast.LENGTH_LONG).show();
                }
                else{
                    showLeaveDialogBox();
                }
            }
        });
    }

    private void showLeaveDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Leave Wallet");
        builder.setMessage("Are you sure you want to leave this shared wallet?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete user from shared wallet's participant list
                walletParticipantList = new ArrayList<>();
                userParticipatedWallet = new ArrayList<>();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get all current participant
                        for(DataSnapshot participant : dataSnapshot.child("SharedWallets").child(shareWalletId).
                                child("participants").getChildren()){

                            String user = participant.getValue().toString();
                            walletParticipantList.add(user);
                        }
                        // Delete current user from the participant list
                        walletParticipantList.remove(uid);

                        // Get current user's participated wallet list
                        for(DataSnapshot walletId : dataSnapshot.child("Users").child(uid).child("participatedWallet").getChildren()){
                            String id = walletId.getValue().toString();
                            userParticipatedWallet.add(id);
                        }
                        // Delete participated shared wallet from current user data
                        userParticipatedWallet.remove(shareWalletId);

                        // Upload updated list to database
                        databaseReference.child("Users").child(uid).child("participatedWallet").setValue(userParticipatedWallet).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        databaseReference.child("SharedWallets").child(shareWalletId).child("participants").
                                                setValue(walletParticipantList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                getActivity().finish();
                                                Toast.makeText(getActivity(), "You are no longer a memeber of this wallet", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.v(TAG, "Error: " + databaseError);
                        Toast.makeText(getActivity(), "Error leaving wallet! Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}