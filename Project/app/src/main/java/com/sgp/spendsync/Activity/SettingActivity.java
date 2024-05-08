package com.sgp.spendsync.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sgp.spendsync.R;

public class SettingActivity extends AppCompatActivity {

    ImageView ivBack;
    LinearLayout relShare,relRate,relLock,relFeedBack,relPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ivBack=findViewById(R.id.ivBack);
        relShare=findViewById(R.id.relShare);
        relRate=findViewById(R.id.relRate);
        relLock=findViewById(R.id.relLock);
        relFeedBack=findViewById(R.id.relFeedBack);
        relPolicy=findViewById(R.id.relPolicy);

        ivBack.setOnClickListener(view -> finish());

        relPolicy.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("YOUR_PRIVACY_POLICY_URL"));//replace with your privacy policy url
            startActivity(intent1);
        });

        relShare.setOnClickListener(view -> {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/plain");
            intentShare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intentShare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(intentShare);
        });

        relRate.setOnClickListener(view -> {
            try {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent1);
            } catch (Exception ex) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(intent1);
            }
        });

        relLock.setOnClickListener(view -> new AlertDialog.Builder(SettingActivity.this).setTitle("Set Password")
                .setMessage("To set Password press on OK")
                .setPositiveButton("OK", (dialog, which) -> startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                }).show());

        relFeedBack.setOnClickListener(view -> {
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.EMAIL", new String[]{"YOUR_SUPPORT_EMAIL"});
                intent.setType("text/plain");
                ResolveInfo resolveInfo = null;
                for (ResolveInfo resolveInfo2 : getPackageManager().queryIntentActivities(intent, 0)) {
                    if (resolveInfo2.activityInfo.packageName.endsWith(".gm") || resolveInfo2.activityInfo.name.toLowerCase().contains("gmail")) {
                        resolveInfo = resolveInfo2;
                    }
                }
                if (resolveInfo != null) {
                    intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                }
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}