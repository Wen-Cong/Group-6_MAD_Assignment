package com.mad.budgetapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SharedWalletParticipantFragment extends Fragment {
    private static final String TAG = "sharedWalletParticipant";
    SharedWallet wallet;
    RecyclerView recyclerView;

    public SharedWalletParticipantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        wallet = ((SharedWalletDetailsActivity) getActivity()).sharedWallet;
        return inflater.inflate(R.layout.fragment_shared_wallet_participant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.participantRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        SharedWalletParticipantAdapter adapter = new SharedWalletParticipantAdapter(wallet.getParticipants(),
                wallet, getActivity());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(itemDecoration);
    }
}