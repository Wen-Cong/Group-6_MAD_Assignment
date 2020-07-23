package com.mad.budgetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import java.util.Date;

public class SharedWalletTransactionAdapter extends RecyclerView.Adapter<SharedWalletTransactionViewHolder> {
    private static final String TAG = "ShareTransactionAdapter";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ArrayList<SharedTransaction> data;
    Activity activity;
    String uid;
    String sharedWalletId;
    SharedWallet wallet;

    public SharedWalletTransactionAdapter(ArrayList<SharedTransaction> transactions, Activity activity){
        data = transactions;
        this.activity = activity;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sharedWalletId = ((SharedWalletDetailsActivity) activity).shareWalletId;
        wallet = ((SharedWalletDetailsActivity) activity).sharedWallet;
    }

    @NonNull
    @Override
    public SharedWalletTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedtransaction_view, parent, false);
        SharedWalletTransactionViewHolder viewHolder = new SharedWalletTransactionViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SharedWalletTransactionViewHolder holder, int position) {
        final SharedTransaction sharedTransaction = data.get(position);
        String formatedDate;
        // Load and display transaction data
        getUsername(sharedTransaction.getUid(), holder);
        holder.transactionName.setText(sharedTransaction.getName());
        holder.amt.setText(activity.getString(R.string.display_Bal, sharedTransaction.getAmount()));

        // Set amount text color to green if transaction type is income
        if(sharedTransaction.getType().toString().equals("Income")){
            holder.amt.setTextColor(Color.GREEN);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            // format date time for display
            Date d = sdf.parse(sharedTransaction.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            formatedDate = format.format(d);
            holder.dateTime.setText(formatedDate);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
            holder.dateTime.setText(sharedTransaction.getTime());
        }

        loadProfilePic(sharedTransaction.getUid(), holder);

        if(uid.equals(sharedTransaction.getUid())){
            // If transaction belongs to user, set OnClick for options button to display menu
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, holder.option);
                    //Inflate menu display
                    popupMenu.inflate(R.menu.transactionmenu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.deleteTransaction:
                                    confirmDeleteTransaction(sharedTransaction);
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
            // Hide the option button if transaction does not belong to user
            holder.option.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void deleteTransaction(final SharedTransaction sharedTransaction) {
        //find primary key for selected transaction in database
        String tTime = sharedTransaction.getTime().toString();
        databaseReference.child("SharedWallets").child(sharedWalletId)
                .child("sharedTransaction").orderByChild("time")
                .equalTo(sharedTransaction.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String transactionKey = childSnapshot.getKey();

                    //delete selected transaction from database
                    databaseReference.child("SharedWallets").child(sharedWalletId).child("sharedTransaction")
                            .child(transactionKey).removeValue();

                    //update wallet balance after transaction deleted
                    double updatedBal = wallet.getBalance() - sharedTransaction.getAmount();
                    databaseReference.child("SharedWallets").child(sharedWalletId).child("balance").setValue(updatedBal);
                    wallet.setBalance(updatedBal);

                    //delete selected transaction from local data
                    wallet.removeSharedTransaction(sharedTransaction);
                    ((SharedWalletDetailsActivity) activity).sharedWallet = wallet;

                    //update recyclerview upon deleting
                    data.remove(sharedTransaction);
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Transaction Deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void confirmDeleteTransaction(final SharedTransaction sharedTransaction){
        //display dialog box for delete transaction confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this transaction?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTransaction(sharedTransaction);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadProfilePic(String uid, final SharedWalletTransactionViewHolder holder) {
        //load profile image from storage to image view
        StorageReference profileRef =
                storageReference.child("users/"+ uid +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profile_pic);
            }
        });
    }

    private void getUsername(String uid, final SharedWalletTransactionViewHolder holder){
        databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get username from database with user ID
                String name = dataSnapshot.child("username").getValue().toString();
                holder.username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG, "onCancelled: " + databaseError);
            }
        });
    }
}
