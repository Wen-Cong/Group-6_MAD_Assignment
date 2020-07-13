package com.mad.budgetapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSharedWalletNameFragment extends Fragment {
    private EditText newWalletName;
    private DatabaseReference databaseReference;
    private String sharedWalletID;
    private Button submit;
    private Button cancel;

    public EditSharedWalletNameFragment(String walletid) {
        this.sharedWalletID = walletid;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_shared_wallet_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newWalletName = view.findViewById(R.id.newWalletname);
        cancel = view.findViewById(R.id.cancel_changeWalletname);
        submit = view.findViewById(R.id.walletname_submit_button);

        //Update database and redirect back to shared wallet details home page
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String walletName = newWalletName.getText().toString().trim();
                ((SharedWalletDetailsActivity) getActivity()).sharedWallet.setName(walletName);
                databaseReference.child("SharedWallets").child(sharedWalletID).child("name").setValue(walletName)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getActivity().
                                        getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.sharedWalletContainer,new SharedWalletHomeFragment()).commit();
                            }
                        });
            }
        });

        //Go back to shared wallet details home page
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().
                        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sharedWalletContainer,new SharedWalletHomeFragment()).commit();
            }
        });
    }
}