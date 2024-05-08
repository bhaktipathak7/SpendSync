package com.sgp.spendsync.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sgp.spendsync.Activity.WebviewActivity;
import com.sgp.spendsync.Model.ModelBank;
import com.sgp.spendsync.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class BanklistAdapter extends RecyclerView.Adapter<BanklistAdapter.MyViewHolder> {

    Activity activity;
    List<ModelBank> modelArrayList;
    InterstitialAd mInterstitialAd;
    public BanklistAdapter(Activity activity, List<ModelBank> modelArrayList) {

        this.activity=activity;
        this.modelArrayList=modelArrayList;
        loadAds();
    }

    private void loadAds() {
        InterstitialAd.load(activity, activity.getString(R.string.interstitial_ID), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;

            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bank_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txtBankName.setText(modelArrayList.get(position).getBankName());

        holder.ivLogo.setImageResource(modelArrayList.get(position).getBanklogo());

        holder.relCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(activity);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {

                            loadAds();
                            Intent intent=new Intent(activity, WebviewActivity.class);
                            intent.putExtra("link", modelArrayList.get(position).getBankUrl());
                            activity.startActivity(intent);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Intent intent=new Intent(activity, WebviewActivity.class);
                            intent.putExtra("link", modelArrayList.get(position).getBankUrl());
                            activity.startActivity(intent);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    Intent intent=new Intent(activity, WebviewActivity.class);
                    intent.putExtra("link", modelArrayList.get(position).getBankUrl());
                    activity.startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtBankName;
        ImageView ivLogo;
        LinearLayout relCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBankName=itemView.findViewById(R.id.txtBankName);
            ivLogo=itemView.findViewById(R.id.ivBank);
            relCategory=itemView.findViewById(R.id.relCategory);
        }
    }
}
