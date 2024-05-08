package com.sgp.spendsync.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.sgp.spendsync.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewActivity extends AppCompatActivity {

    ImageView ivBack;
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        ivBack=findViewById(R.id.ivBack);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String filename = getIntent().getStringExtra("filename");

        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getResources().getString(R.string.app_name));

        File repostfile = new File(file1, filename);

        pdfView.fromFile(repostfile).load();
    }
}