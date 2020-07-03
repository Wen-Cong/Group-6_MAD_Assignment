package com.mad.budgetapplication;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SharedWalletParticipantAdapter extends RecyclerView.Adapter<SharedWalletParticipantViewHolder> {
    private static final String TAG = "ParticipantAdapter";
    ArrayList<String> participantUid;
    SharedWallet wallet;
    Activity detailsActivity;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public SharedWalletParticipantAdapter(ArrayList<String> participantsId, SharedWallet sw, Activity activity){
        participantUid = participantsId;
        wallet = sw;
        detailsActivity = activity;
    }
    @NonNull
    @Override
    public SharedWalletParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedwalletparticipant, parent, false);
        SharedWalletParticipantViewHolder viewHolder = new SharedWalletParticipantViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SharedWalletParticipantViewHolder holder, int position) {
        final String id = participantUid.get(position);
        loadProfilePic(id, holder);
        loadUserdata(id, holder);

    }

    @Override
    public int getItemCount() {
        return participantUid.size();
    }

    private void loadProfilePic(String uid, final SharedWalletParticipantViewHolder holder) {
        StorageReference profileRef =
                storageReference.child("users/"+ uid +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profile_pic);
            }
        });
    }

    private void loadUserdata(final String uid, final SharedWalletParticipantViewHolder holder){
        Double net_contribution = 0.00;
        databaseReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.username.setText(dataSnapshot.child("username").getValue().toString());
                if(uid.equals(wallet.getAdminId())){
                    holder.role.setText("Admin");
                    holder.role.setTypeface(null, Typeface.BOLD);
                }
                else{
                    holder.role.setText("Member");
                    holder.role.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG, "Error!" + databaseError);
            }
        });

        for(SharedTransaction st : wallet.getSharedTransaction()){
            SimpleDateFormat monthlyformat = new SimpleDateFormat("MM");
            Date date = new Date();
            String currentMonth = monthlyformat.format(date);
            SimpleDateFormat Wdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                //find wallet with most transaction in the same month as current month
                Date WalletTransactionDate = Wdf.parse(st.getTime());
                String transactionMonth = monthlyformat.format(WalletTransactionDate);
                if(transactionMonth.equals(currentMonth)){
                    if(st.getUid().equals(uid)){
                        if(st.getType().equals("Income") || st.getType().equals("Expenses")){
                            net_contribution += st.getAmount();
                        }
                    }
                }
            }
            catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
        }

        SimpleDateFormat monthName = new SimpleDateFormat("MMM");
        String currentMonthString = monthName.format(Calendar.getInstance().getTime());
        holder.monthly_contribution.setText(detailsActivity.getString(R.string.monthly_contribution, currentMonthString, net_contribution));

    }

}
