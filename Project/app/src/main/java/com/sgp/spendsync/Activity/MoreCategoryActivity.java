package com.sgp.spendsync.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sgp.spendsync.Adapter.MoreCatlistAdapter;
import com.sgp.spendsync.Database.Databasehelper;
import com.sgp.spendsync.Others.ItemClick;
import com.sgp.spendsync.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MoreCategoryActivity extends AppCompatActivity implements ItemClick {

    ImageView ivBack, ivMoreAdd;
    RecyclerView rvMorelist;
    public static int image_pos = -1;
    Databasehelper databasehelper;
    String which;
    FrameLayout frameBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_category);

        Intent intent = getIntent();
        which = intent.getStringExtra("from");
        findView();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        ivMoreAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                createBottomSheet();
//            }
//        });
        MoreCatlistAdapter categoryAddAdapter = new MoreCatlistAdapter(MoreCategoryActivity.this, this);
        rvMorelist.setAdapter(categoryAddAdapter);
        rvMorelist.setLayoutManager(new GridLayoutManager(MoreCategoryActivity.this, 4));

    }

    private void findView() {

        databasehelper = new Databasehelper(this);
        ivBack = findViewById(R.id.ivBack);
        ivMoreAdd = findViewById(R.id.ivMoreAdd);
        rvMorelist = findViewById(R.id.rvMorelist);
        frameBanner = findViewById(R.id.frameBanner);
        loadBanner();
    }
    public void loadBanner() {
        if (frameBanner != null) {
            frameBanner.setVisibility(View.VISIBLE);
            AdView adView = new AdView(MoreCategoryActivity.this);
            adView.setAdUnitId(getResources().getString(R.string.banner_ID));
            frameBanner.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(MoreCategoryActivity.this);
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

    public void createBottomSheet() {
        View view = LayoutInflater.from(MoreCategoryActivity.this).inflate(R.layout.bottomsheet_more_item, null);
        BottomSheetDialog bottomSheetDialogAdd = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNew);
        bottomSheetDialogAdd.setContentView(view);
        bottomSheetDialogAdd.setCanceledOnTouchOutside(false);
        bottomSheetDialogAdd.setDismissWithAnimation(true);

        ImageView ivCloseAdd = bottomSheetDialogAdd.findViewById(R.id.ivCloseAdd);
        EditText etCategory = bottomSheetDialogAdd.findViewById(R.id.etCategory);
        LinearLayout lAddItem = bottomSheetDialogAdd.findViewById(R.id.lAddItem);

        etCategory.setText("");
        etCategory.setHint("Enter Category");
        ivCloseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogAdd.dismiss();
            }
        });

        lAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogAdd.dismiss();

                if (etCategory.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MoreCategoryActivity.this, "Please enter category name", Toast.LENGTH_SHORT).show();
                } else if (image_pos == -1) {
                    Toast.makeText(MoreCategoryActivity.this, "Please select icon", Toast.LENGTH_SHORT).show();
                } else {
                    String categoryitem = etCategory.getText().toString();

                    if (which.matches("INCOME")) {

                        boolean result = databasehelper.categoryIncomeData(categoryitem, image_pos);
                        if (result) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        boolean res = databasehelper.categoryExpenseData(categoryitem, image_pos);
                        if (res) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                }
            }
        });

        if (bottomSheetDialogAdd != null && !bottomSheetDialogAdd.isShowing()) {
            bottomSheetDialogAdd.show();
        }


    }

    @Override
    public void onItemClick(int pos) {
        image_pos = pos;

        createBottomSheet();
    }
}