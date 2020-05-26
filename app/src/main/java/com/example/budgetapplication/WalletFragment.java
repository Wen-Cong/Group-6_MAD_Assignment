package com.example.budgetapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {

    private static final String TAG = "walletFragment";
    User user;
    RecyclerView recyclerView;

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
        user = ((HomeActivity)this.getActivity()).getUser();
        Wallet last = user.getWallets().get(user.getWallets().size()-1);
        Log.d(TAG, last.getName());
        showNewEntry(recyclerView, user.getWallets());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        user = ((HomeActivity)this.getActivity()).getUser();
        recyclerView = getView().findViewById(R.id.recyclerView);
        ArrayList<Wallet> dataList = user.getWallets();

        //Create adapter and pass in the data
        WalletAdapter mAdapter = new WalletAdapter(dataList);
        //Layout manager tells recyclerview how to draw the list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //pass in layout, animation and adapter.
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    private void showNewEntry(RecyclerView rv, ArrayList data) {
        //scroll to the last item of the recyclerview
        rv.scrollToPosition(data.size() - 1);

        //auto hide keyboard after entry
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rv.getWindowToken(), 0);

        Log.d(TAG, "showNewEntry: I did it!");
    }
}
