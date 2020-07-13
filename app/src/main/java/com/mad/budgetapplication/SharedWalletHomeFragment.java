package com.mad.budgetapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedWalletHomeFragment extends Fragment {
    private static final String TAG = "sharedWalletHome";
    SharedWallet sharedWallet;
    String shareWalletId;
    private ImageView edit_name;
    private ImageView edit_password;
    private TextView sharedWalletId_Display;
    private TextView sharedWalletName_Display;
    private TextView sharedWalletPassword_Display;
    private TextView sharedWalletIncome_Display;
    private TextView sharedWalletExpenses_Display;
    private TextView sharedWalletTotal_Display;
    private TextView sharedWalletBal_Display;
    private TextView sharedWalletIncomeCount_Display;
    private TextView sharedWalletExpensesCount_Display;
    private TextView sharedWalletTotalCount_Display;

    public SharedWalletHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedWallet = ((SharedWalletDetailsActivity) getActivity()).sharedWallet;
        shareWalletId = ((SharedWalletDetailsActivity) getActivity()).shareWalletId;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared_wallet_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Find textview in view
        sharedWalletId_Display = view.findViewById(R.id.sharedWalletID);
        sharedWalletName_Display = view.findViewById(R.id.sharedWalletName);
        sharedWalletPassword_Display = view.findViewById(R.id.sharedWalletPassword);
        sharedWalletBal_Display = view.findViewById(R.id.sharedWalletBalance);
        sharedWalletIncome_Display = view.findViewById(R.id.sharedWalletIncome);
        sharedWalletExpenses_Display = view.findViewById(R.id.sharedWalletExpense);
        sharedWalletTotal_Display = view.findViewById(R.id.sharedWalletTotal);
        sharedWalletIncomeCount_Display = view.findViewById(R.id.sharedWalletIncomecount);
        sharedWalletExpensesCount_Display = view.findViewById(R.id.sharedWalletExpensecount);
        sharedWalletTotalCount_Display = view.findViewById(R.id.sharedWalletTotalcount);
        edit_name = view.findViewById(R.id.sharedWallet_editname);
        edit_password = view.findViewById(R.id.sharedWallet_editpw);

        //if user is admin, make edit button visible and set OnClick Listener
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(sharedWallet.getAdminId())){
            //Set image view to visible
            edit_name.setVisibility(View.VISIBLE);
            edit_password.setVisibility(View.VISIBLE);

            //Direct admin to change wallet name page
            edit_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().
                            getSupportFragmentManager().beginTransaction()
                            .replace(R.id.sharedWalletContainer,new EditSharedWalletNameFragment(shareWalletId)).commit();
                }
            });

            //Direct admin to change wallet password page
            edit_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().
                            getSupportFragmentManager().beginTransaction()
                            .replace(R.id.sharedWalletContainer,new EditSharedWalletPasswordFragment(shareWalletId)).commit();
                }
            });
        }

        // Display info to view
        sharedWalletId_Display.setText(shareWalletId);
        sharedWalletName_Display.setText(sharedWallet.getName());
        sharedWalletPassword_Display.setText(sharedWallet.getPassword());
        String display_bal = getString(R.string.display_Bal, sharedWallet.getBalance());
        sharedWalletBal_Display.setText(display_bal);

        Double total_income = 0.00;
        Double total_expenses = 0.00;
        Integer income_count = 0;
        Integer expenses_count = 0;

        for(SharedTransaction st : sharedWallet.getSharedTransaction()){
            SimpleDateFormat monthlyformat = new SimpleDateFormat("MM");
            Date date = new Date();
            String currentMonth = monthlyformat.format(date);
            SimpleDateFormat Wdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                //find transaction in the same month as current month
                Date WalletTransactionDate = Wdf.parse(st.getTime());
                String transactionMonth = monthlyformat.format(WalletTransactionDate);
                if(transactionMonth.equals(currentMonth)){
                    if(st.getType().equals("Income")){
                        //Sum of income transaction created this month
                        total_income += st.getAmount();
                        income_count++;
                    }
                    else if(st.getType().equals("Expenses")){
                        //Sum of expenses transaction created this month
                        Double expense_positiveFormat = st.getAmount() * -1;
                        total_expenses += expense_positiveFormat;
                        expenses_count++;
                    }
                }
            }
            catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }
        }

        //Display income and expense calculated
        Double total_netTransaction = total_income - total_expenses;
        sharedWalletIncome_Display.setText(getString(R.string.display_Bal, total_income));
        sharedWalletExpenses_Display.setText(getString(R.string.display_Bal, total_expenses));
        sharedWalletTotal_Display.setText(getString(R.string.display_Bal, total_netTransaction));

        sharedWalletIncomeCount_Display.setText(income_count.toString());
        sharedWalletExpensesCount_Display.setText(expenses_count.toString());
        sharedWalletTotalCount_Display.setText(((Integer) (income_count + expenses_count)).toString());
    }
}