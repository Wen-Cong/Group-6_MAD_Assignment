package com.example.budgetapplication;

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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        User user = ((HomeActivity)this.getActivity()).getUser();
        ArrayList<Wallet> dataList = new ArrayList<>();
        for (Wallet wallet: user.getWallets()) {
            dataList.add(wallet);
        }

        //Create RecyclerView in the layout and bind the data, ViewHolder and Adapter to the it
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        //Create adapter and pass in the data
        WalletAdapter mAdapter = new WalletAdapter(dataList);
        //Layout manager tells recyclerview how to draw the list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //pass in layout, animation and adapter.
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
