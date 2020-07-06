package com.mad.budgetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    String uid;
    String walletId;
    ArrayList<String> walletParticipantList;
    ArrayList<String> userParticipatedWallet;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public SharedWalletParticipantAdapter(ArrayList<String> participantsId, SharedWallet sw, Activity activity){
        participantUid = participantsId;
        wallet = sw;
        detailsActivity = activity;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        walletId = ((SharedWalletDetailsActivity) detailsActivity).shareWalletId;
    }
    @NonNull
    @Override
    public SharedWalletParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedwalletparticipant_view, parent, false);
        SharedWalletParticipantViewHolder viewHolder = new SharedWalletParticipantViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SharedWalletParticipantViewHolder holder, int position) {
        final String id = participantUid.get(position);
        loadProfilePic(id, holder);
        loadUserdata(id, holder);

        if(uid.equals(wallet.getAdminId()) && !id.equals(wallet.getAdminId())){
            // set OnClick if user is admin
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(detailsActivity, holder.options);
                    popupMenu.inflate(R.menu.participantmenu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){
                                case R.id.setAdmin:
                                    // Update wallet admin and refresh participant page
                                    databaseReference.child("SharedWallets").child(walletId).child("adminId").setValue(id);
                                    wallet.setAdminId(id);
                                    ((SharedWalletDetailsActivity) detailsActivity).getSupportFragmentManager().
                                            beginTransaction().replace(R.id.sharedWalletContainer,new SharedWalletParticipantFragment()).commit();
                                    Toast.makeText(detailsActivity, "Admin Changed!", Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.deleteParticipant:
                                    showkickDialogBox(id);
                                    Toast.makeText(detailsActivity, "Participant has been kicked!", Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        else{
            // Hide options button if user not admin
            holder.options.setTextColor(Color.WHITE);
        }

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

    private void showkickDialogBox(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(detailsActivity);
        builder.setTitle("Leave Wallet");
        builder.setMessage("Are you sure you want to kick this participant?");
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
                        for(DataSnapshot participant : dataSnapshot.child("SharedWallets").child(walletId).
                                child("participants").getChildren()){

                            String user = participant.getValue().toString();
                            walletParticipantList.add(user);
                        }
                        // Delete current user from the participant list and upload updated list to database
                        walletParticipantList.remove(id);
                        databaseReference.child("SharedWallets").child(walletId).child("participants").
                                setValue(walletParticipantList);

                        // Get current user's participated wallet list
                        for(DataSnapshot walletId : dataSnapshot.child("Users").child(id).child("participatedWallet").getChildren()){
                            String userid = walletId.getValue().toString();
                            userParticipatedWallet.add(userid);
                        }
                        // Delete participated shared wallet from current user data
                        userParticipatedWallet.remove(walletId);
                        databaseReference.child("Users").child(id).child("participatedWallet").setValue(userParticipatedWallet);
                        Toast.makeText(detailsActivity, "This participant has been kicked out", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.v(TAG, "Error: " + databaseError);
                    }
                });
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
