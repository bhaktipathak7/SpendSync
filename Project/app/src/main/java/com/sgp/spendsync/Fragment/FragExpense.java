package com.sgp.spendsync.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sgp.spendsync.Adapter.TranscationAdapter;
import com.sgp.spendsync.Database.Databasehelper;
import com.sgp.spendsync.Model.ModelTransaction;
import com.sgp.spendsync.Others.ItemClick;
import com.sgp.spendsync.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FragExpense extends Fragment implements ItemClick {

    TextView txtMonthName, txtTotalExpense, txtNoFound;
    ImageView ivPrevious, ivNext;
    RecyclerView rvExpenselist;
    int cMonth;
    TranscationAdapter mTranAdapter;
    public double sarvaloExpense=0;
    Databasehelper databasehelper;
    ArrayList<ModelTransaction> modelArrayList;
    SwipeRefreshLayout swipeRefreshExpense;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        findView(rootView);

        cMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        getExpenseData(cMonth);
        getTotalBalance(cMonth);

        swipeRefreshExpense.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshExpense.setRefreshing(false);

                        getExpenseData(cMonth);
                        getTotalBalance(cMonth);
                    }
                }, 1000);
            }
        });
        ivNext.setOnClickListener(view -> {
            cMonth = cMonth + 1;
            Log.e("cMonth--)", "" + cMonth);
            if (cMonth <= 12) {
                txtMonthName.setText(getMonthName(cMonth));
            } else {
                cMonth = 0;
                cMonth = cMonth + 1;
                txtMonthName.setText(getMonthName(cMonth));
            }
            getExpenseData(cMonth);
            getTotalBalance(cMonth);
        });

        ivPrevious.setOnClickListener(view -> {

            cMonth = cMonth - 1;
            if (cMonth < 1) {
                cMonth = 12;
                txtMonthName.setText(getMonthName(cMonth));
            } else {

                txtMonthName.setText(getMonthName(cMonth));
            }
            getExpenseData(cMonth);
            getTotalBalance(cMonth);
        });
        return rootView;
    }

    public void getTotalBalance(int mnt) {
        sarvaloExpense=0;
        Cursor cursorincome = databasehelper.getTotalExpense(mnt);
        cursorincome.moveToFirst();
        for (int i = 0; i < cursorincome.getCount(); i++) {
            String total = cursorincome.getString(cursorincome.getColumnIndex("amount"));
            sarvaloExpense = sarvaloExpense + Integer.parseInt(total);
            cursorincome.moveToNext();
        }
        txtTotalExpense.setText("\u20B9"+String.valueOf(sarvaloExpense));
    }


    public void getExpenseData(int mnt) {
        modelArrayList = new ArrayList<>();
        databasehelper = new Databasehelper(getActivity());

        Cursor cursor = databasehelper.getMonthExpense(mnt);
        while (cursor.moveToNext()) {
            ModelTransaction modelTransactions = new ModelTransaction();
            modelTransactions.setId(cursor.getInt(0));
            modelTransactions.setAmount(cursor.getString(1));
            modelTransactions.setNote(cursor.getString(2));
            modelTransactions.setTransaction_type(cursor.getString(3));
            modelTransactions.setCategory_type(cursor.getString(4));
            modelTransactions.setDatentime(cursor.getString(5));
            modelTransactions.setImage_pos(cursor.getInt(6));
            modelArrayList.add(modelTransactions);
        }
        cursor.close();

        if (modelArrayList.size()==0)
        {
            txtNoFound.setVisibility(View.VISIBLE);
        }
        else
        {
            txtNoFound.setVisibility(View.GONE);
        }
        mTranAdapter = new TranscationAdapter(getActivity(), modelArrayList, this);
        rvExpenselist.setAdapter(mTranAdapter);
        rvExpenselist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
    public String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        return month_date.format(cal.getTime());
    }

    public String getMonthName(int month) {
        if (month == 1) {
            return "January";
        } else if (month == 2) {
            return "February";
        } else if (month == 3) {
            return "March";
        } else if (month == 4) {
            return "April";
        } else if (month == 5) {
            return "May";
        } else if (month == 6) {
            return "June";
        } else if (month == 7) {
            return "July";
        } else if (month == 8) {
            return "August";
        } else if (month == 9) {
            return "September";
        } else if (month == 10) {
            return "October";
        } else if (month == 11) {
            return "November";
        } else if (month == 12) {
            return "December";
        }
        return null;
    }


    private void findView(View rootView) {

        txtTotalExpense = rootView.findViewById(R.id.txtTotalExpense);
        txtMonthName = rootView.findViewById(R.id.txtMonthName);
        ivPrevious = rootView.findViewById(R.id.ivPrevious);
        txtNoFound = rootView.findViewById(R.id.txtNoFound);
        ivNext = rootView.findViewById(R.id.ivNext);
        rvExpenselist = rootView.findViewById(R.id.rvExpenselist);
        swipeRefreshExpense = rootView.findViewById(R.id.swipeRefreshExpense);

        txtMonthName.setText(getCurrentMonth());
    }

    @Override
    public void onItemClick(int pos) {

    }
}
