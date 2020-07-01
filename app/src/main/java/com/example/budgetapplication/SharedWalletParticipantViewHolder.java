package com.example.budgetapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedWalletParticipantViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profile_pic;
    TextView username;
    TextView role;
    TextView monthly_contribution;
    public SharedWalletParticipantViewHolder(@NonNull View itemView) {
        super(itemView);
        profile_pic = itemView.findViewById(R.id.st_profile_pic_participant);
        username = itemView.findViewById(R.id.username_participant);
        role = itemView.findViewById(R.id.role_participant);
        monthly_contribution = itemView.findViewById(R.id.contribution_participant);
    }
}
