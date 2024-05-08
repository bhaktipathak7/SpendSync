package com.sgp.spendsync.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sgp.spendsync.Adapter.BanklistAdapter;
import com.sgp.spendsync.R;
import com.sgp.spendsync.Activity.MainActivity;

public class FragBank extends Fragment {

    RecyclerView rvBanklist;
    BanklistAdapter mBanklistAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bank, container, false);

        rvBanklist=rootView.findViewById(R.id.rvBanklist);

        mBanklistAdapter=new BanklistAdapter(getActivity(), MainActivity.modelArrayList);
        rvBanklist.setAdapter(mBanklistAdapter);
        rvBanklist.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        return rootView;
    }
}
