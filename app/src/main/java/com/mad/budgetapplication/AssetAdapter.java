package com.mad.budgetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssetAdapter extends RecyclerView.Adapter<AssetViewHolder>{
    User user;
    Activity activityMain;
    ArrayList<Asset> assetArrayList;
    final private String TAG = "AssetAdapter";
    public AssetAdapter(User CurrentUser, Activity parentActivity){
        user = CurrentUser;
        activityMain = parentActivity;
        assetArrayList = user.getAssetList();
    }
    //This method is called by adapter when a new ViewHolder is created
    public AssetViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        //This line create the UI from the XML
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_view_item, parent, false);
        //return the ViewHolder
        return new AssetViewHolder(item);
    }
    /*This method is called by Adapter to bind data to the ViewHolder. Adapter pass in the ViewHolder, and the row id (index starts from 0)*/

    public void onBindViewHolder(AssetViewHolder holder, final int position){
            //Using the row id to retrieve data from list
            Asset a = assetArrayList.get(position);
            //Display the information on to the UI
            final String s = a.getName();
            String v = "$"+String.valueOf(a.getValue());
            holder.txt.setText(s);
            holder.value.setText(v);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DeleteDialog(s, position);
                    return false;
                }
            });
    }

    public int getItemCount(){
        return assetArrayList.size();
    }
    public void deleteItemFromDB(String name){
        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentId);
        Query deleteQuery = databaseReference.child("assets").orderByChild("name").equalTo(name);

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void DeleteDialog(final String Name, final int Position){
        AlertDialog.Builder builder = new AlertDialog.Builder(activityMain);
        builder.setTitle("Delete Asset");
        builder.setMessage("Are you sure you want to delete "+Name+"?");
        builder.setCancelable(false);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItemFromDB(Name);
                assetArrayList.remove(Position);
                notifyDataSetChanged();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
