package com.sgp.spendsync.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgp.spendsync.Adapter.CatlistAdapter;
import com.sgp.spendsync.Database.Databasehelper;
import com.sgp.spendsync.Model.ModelCategory;
import com.sgp.spendsync.Others.ItemClick;
import com.sgp.spendsync.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class IncomelistActivity extends AppCompatActivity implements ItemClick {

    private ImageView ivBack,ivAdd;
    private TextView txtTitle;
    private RecyclerView rvIncomelist;
    ArrayList<ModelCategory> modelCategories;
    Databasehelper databasehelper;
    SQLiteDatabase db;
    Cursor cursor;
    String which;
    CatlistAdapter myIncomeAdapterTrans;
    InterstitialAd mInterstitialAd;
    FrameLayout frameBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomelist);

        Intent intent=getIntent();
        which=intent.getStringExtra("fromWhich");
        modelCategories = new ArrayList<>();
        databasehelper = new Databasehelper(this);
        findView();
        setAdapter();
        LoadAds();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(IncomelistActivity.this,MoreCategoryActivity.class);
                intent1.putExtra("from",which);
                startActivityForResult(intent1,123);
            }
        });
    }

    private void LoadAds() {
        InterstitialAd.load(this, getString(R.string.interstitial_ID), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {
            setAdapter();
        }
    }
    private void setAdapter() {

        modelCategories = new ArrayList<>();
        if(which.matches("INCOME"))
        {
            cursor = databasehelper.getCategoryIncomeData();
            while (cursor.moveToNext()) {
                ModelCategory modelCategory = new ModelCategory();
                modelCategory.setId(cursor.getInt(0));
                modelCategory.setName(cursor.getString(1));
                modelCategory.setImage(cursor.getInt(2));

                modelCategories.add(modelCategory);
            }

            myIncomeAdapterTrans = new CatlistAdapter(IncomelistActivity.this, modelCategories,this);
            rvIncomelist.setAdapter(myIncomeAdapterTrans);
            rvIncomelist.setLayoutManager(new LinearLayoutManager(IncomelistActivity.this,LinearLayoutManager.VERTICAL,false));
        }
        else
        {
            cursor = databasehelper.getCategoryExpenseData();
            while (cursor.moveToNext()) {
                ModelCategory modelCategory = new ModelCategory();
                modelCategory.setId(cursor.getInt(0));
                modelCategory.setName(cursor.getString(1));
                modelCategory.setImage(cursor.getInt(2));

                modelCategories.add(modelCategory);
            }

            myIncomeAdapterTrans = new CatlistAdapter(IncomelistActivity.this, modelCategories,this);
            rvIncomelist.setAdapter(myIncomeAdapterTrans);
            rvIncomelist.setLayoutManager(new LinearLayoutManager(IncomelistActivity.this,LinearLayoutManager.VERTICAL,false));
        }

    }

    private void findView()
    {
        ivBack=findViewById(R.id.ivBack);
        ivAdd=findViewById(R.id.ivAdd);
        txtTitle=findViewById(R.id.txtTitle);
        rvIncomelist=findViewById(R.id.rvIncomelist);
        frameBanner=findViewById(R.id.frameBanner);
        loadBanner();
        if(which.matches("INCOME"))
        {
            txtTitle.setText("Add Income Items");
        }
        else
        {
            txtTitle.setText("Add Expenses Items");
        }

    }
    public void loadBanner() {
        if (frameBanner != null) {
            frameBanner.setVisibility(View.VISIBLE);
            AdView adView = new AdView(IncomelistActivity.this);
            adView.setAdUnitId(getResources().getString(R.string.banner_ID));
            frameBanner.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(IncomelistActivity.this);
            adView.setAdSize(adSize);
            adView.loadAd(adRequest);
        }
    }
    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    @Override
    public void onItemClick(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IncomelistActivity.this);
        builder.setTitle("Choose an Option");
        builder.setPositiveButton("Add Amount", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                takeInputBottom(pos);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int ids = modelCategories.get(pos).getId();

                db = databasehelper.getReadableDatabase();
                int result = databasehelper.deleteincomecategory(ids);
                if (result > 0) {
                    modelCategories.remove(pos);
                    myIncomeAdapterTrans.notifyDataSetChanged();
                }
            }

        });
        builder.show();
    }
    public void takeInputBottom(int poss)
    {
        View view = LayoutInflater.from(IncomelistActivity.this).inflate(R.layout.bottomsheet_add_item, null);
        BottomSheetDialog bottomSheetDialogAdd = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNew);
        bottomSheetDialogAdd.setContentView(view);
        bottomSheetDialogAdd.setCanceledOnTouchOutside(false);
        bottomSheetDialogAdd.setDismissWithAnimation(true);

        ImageView ivCloseAdd=bottomSheetDialogAdd.findViewById(R.id.ivCloseAdd);
        ImageView ivLogo=bottomSheetDialogAdd.findViewById(R.id.ivLogo);
        EditText etAmount=bottomSheetDialogAdd.findViewById(R.id.etAmount);
        EditText etNote=bottomSheetDialogAdd.findViewById(R.id.etNote);
        TextView txtDone=bottomSheetDialogAdd.findViewById(R.id.txtDone);
        TextView txtTitle=bottomSheetDialogAdd.findViewById(R.id.txtTitle);


        String category_type=modelCategories.get(poss).getName();
        int image_pos= modelCategories.get(poss).getImage();

        ivLogo.setImageResource(CatlistAdapter.image[modelCategories.get(poss).getImage()]);
        txtTitle.setText(modelCategories.get(poss).getName());

        ivCloseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogAdd.dismiss();
            }
        });

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(IncomelistActivity.this, "Please enter the Income", Toast.LENGTH_SHORT).show();
                } else {

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(IncomelistActivity.this);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                bottomSheetDialogAdd.dismiss();
                                databasehelper.insertIncomeData(etAmount.getText().toString(),
                                        etNote.getText().toString(),
                                        which,
                                        category_type, image_pos);
                                finish();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                bottomSheetDialogAdd.dismiss();
                                databasehelper.insertIncomeData(etAmount.getText().toString(),
                                        etNote.getText().toString(),
                                        which,
                                        category_type, image_pos);
                                finish();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                            }
                        });

                    } else {
                        bottomSheetDialogAdd.dismiss();
                        databasehelper.insertIncomeData(etAmount.getText().toString(),
                                etNote.getText().toString(),
                                which,
                                category_type, image_pos);
                        finish();
                    }


                }
            }
        });

        bottomSheetDialogAdd.show();
    }

}