package com.mad.budgetapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {

    private static final String TAG = "walletFragment";
    User user;
    RecyclerView recyclerView;
    FloatingActionButton button;

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        onViewCreated(getView(), null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.addSharedTransaction);
        user = ((HomeActivity)this.getActivity()).getUser();
        recyclerView = view.findViewById(R.id.recyclerView);
        ArrayList<Wallet> dataList = user.getWallets();

        //Create adapter and pass in the data
        WalletAdapter mAdapter = new WalletAdapter(dataList, getActivity());
        //Layout manager tells recyclerview how to draw the list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //pass in layout, animation and adapter.
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        showNewEntry(recyclerView, dataList);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWalletForm();
            }
        });
    }


    private void showNewEntry(RecyclerView rv, ArrayList data) {
        //scroll to the last item of the recyclerview
        rv.scrollToPosition(data.size() - 1);

        //auto hide keyboard after entry
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rv.getWindowToken(), 0);

    }

    //open wallet creation form
    public void openWalletForm() {
        Intent createAccForm = new Intent(getActivity(), WalletFormActivity.class);
        createAccForm.putExtra("User", user);
        startActivityForResult(createAccForm, ((HomeActivity) getActivity()).REQ_WALLET_CODE);
    }
}
