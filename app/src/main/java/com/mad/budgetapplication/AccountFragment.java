package com.mad.budgetapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.Context.ALARM_SERVICE;


public class AccountFragment extends Fragment {
    private static final String TAG = "Account Activity";

    StorageReference storageReference;
    DatabaseReference databaseReference;
    CircleImageView profileImage;
    String userId;
    private RelativeLayout changeusername;
    private RelativeLayout supportbutton;
    private  RelativeLayout sharedWalletButton;
    private RelativeLayout logout;
    TextView username;
    Switch reminderOnOff;
    Boolean notificationSwtich;
    final String SHARED_PREF = "shared_pref";
    final String SWITCH = "notificationSwitch";
    private TextView expensesAmt;
    private TextView incomeAmt;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialise database reference
        storageReference = FirebaseStorage.getInstance().getReference();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supportbutton = view.findViewById(R.id.linearlayout_3);
        changeusername = view.findViewById(R.id.changeusername);
        sharedWalletButton = view.findViewById(R.id.sharedWalletButton);
        logout = view.findViewById(R.id.logout);
        username = view.findViewById(R.id.username);
        reminderOnOff = view.findViewById(R.id.notificationSwtich);
        expensesAmt = view.findViewById(R.id.expenseAmt);
        incomeAmt = view.findViewById(R.id.incomeAmt);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        profileImage = getView().findViewById(R.id.profile_pic);
        //load switch on/off for notification
        loadSettings();

        //load profile pic to local from online storage
        StorageReference profileRef =
                storageReference.child("users/"+ userId +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        //load username
        readUsername(userId);

        //bring user to change username page
        changeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangeUsernamePage();
            }
        });
        //bring user to support page
        supportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSupportPage();
            }
        });
        //bring user to shared wallet page
        sharedWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSharedWalletPage();
            }
        });

        //Change Profile Image
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).ProfileImageHandler(view.findViewById(R.id.profile_pic));
            }
        });

        //Log user out and redirect to login page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });

        //Calculate and display total income/expenses
        User user = ((HomeActivity) getActivity()).user;
        Double totalIncome = 0.0;
        Double totalExpenses = 0.0;
        for(Wallet wallet : user.getWallets()){
            for(Transaction transaction : wallet.getTransactions()){
                if(transaction.getType().equals("Expenses")){
                    Double exAmt = transaction.getAmount() * -1;
                    totalExpenses += exAmt;
                }
                else if(transaction.getType().equals("Income")){
                    totalIncome += transaction.getAmount();
                }
            }
        }
        expensesAmt.setText(getString(R.string.display_Bal_noUnit, totalExpenses));
        incomeAmt.setText(getString(R.string.display_Bal_noUnit, totalIncome));

        //turn on or off daily notification upon checking the switch
        reminderOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //set notification
                    Intent intent = new Intent(getActivity(), NotificationBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

                    //allow notification to be sent every 24hrs
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
                            1000 * 60 * 60 * 24, pendingIntent);
                    Toast.makeText(getActivity(), "Reminder set on everyday at this time", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getActivity(), "Daily reminder is turned Off", Toast.LENGTH_SHORT).show();
                }
                //save data of switch to shared preference
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit();
                editor.putBoolean(SWITCH, isChecked);
                editor.apply();

            }
        });
    }
    private void loadSettings(){
        //load share preference data for switch
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        notificationSwtich = sharedPreferences.getBoolean(SWITCH, false);
        reminderOnOff.setChecked(notificationSwtich);
    }

    private void openChangeUsernamePage(){
        Intent intent = new Intent(getActivity(), ChangeUsernameActivity.class);
        startActivity(intent);
    }

    private void openSupportPage(){
        Intent intent = new Intent(getActivity(), SupportActivity.class);
        startActivity(intent);
    }

    private void openSharedWalletPage(){
        Intent intent = new Intent(getActivity(), SharedWalletActivity.class);
        User user = ((HomeActivity) getActivity()).user;
        intent.putExtra("User", user);
        startActivityForResult(intent, ((HomeActivity) getActivity()).REQ_SHAREDWALLET_CODE);
    }

    public void readUsername(String id){
        //load username from firebase database to user object
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+ id +"/username");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String database_username = dataSnapshot.getValue().toString();
                username.setText(database_username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Unable to retrieve username", Toast.LENGTH_SHORT);
            }
        });
    }

    private void createNotificationChannel(){
        //create notification channel if OS is above android Oreo to support notification feature
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "DailyReminderChannel";
            String desc = "Channel for daily reminders";
            NotificationChannel channel = new NotificationChannel("dailyReminders", name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(desc);

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    //redirect to login page and sign out
    private void LogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(getActivity(),LoginActivity.class);
        startActivity(intToMain);
    }


}
