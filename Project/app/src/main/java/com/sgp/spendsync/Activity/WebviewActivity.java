package com.sgp.spendsync.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sgp.spendsync.R;

import im.delight.android.webview.AdvancedWebView;

public class WebviewActivity extends AppCompatActivity {

    AdvancedWebView adWv;
    ImageButton btnErr;
    CookieManager cookieManager;
    RelativeLayout fl;
    ProgressDialog pd;
    WebSettings ws;

    public class MyBrowser extends WebViewClient {
        public MyBrowser() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            if (!WebviewActivity.this.pd.isShowing()) {
                WebviewActivity.this.pd.show();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                return shouldOverrideUrlLoading(webView, webResourceRequest.getUrl().toString());
            }
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (WebviewActivity.this.pd.isShowing()) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        WebviewActivity.this.pd.dismiss();
                    }
                }, 3500);
            }

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (WebviewActivity.this.pd.isShowing()) {
                WebviewActivity.this.pd.dismiss();
            }
            WebviewActivity.this.fl.removeAllViews();
            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public class WebClient extends WebChromeClient {
        Context context;

        public WebClient(Context context2) {
            this.context = context2;
        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        this.pd = new ProgressDialog(this);
        this.pd.setMessage("Please wait...");
        this.pd.show();
//        this.actionBar = getSupportActionBar();
        init();
    }
    @SuppressLint("WrongConstant")
    private void init() {
        this.cookieManager = CookieManager.getInstance();
        this.fl = findViewById(R.id.parentLayout);
//        this.actionBar.hide();
        String data = getIntent().getExtras().getString("link");
        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

        this.adWv = (AdvancedWebView) findViewById(R.id.webView);
        if (!isNetworkAvailable()) {
            if (this.pd.isShowing()) {
                this.pd.dismiss();
            }
            this.fl.removeAllViews();
            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
        this.adWv.loadUrl(data);
        this.adWv.setWebChromeClient(new WebClient(this));
        this.adWv.setWebViewClient(new MyBrowser());
        this.ws = this.adWv.getSettings();
        this.adWv.getSettings().setAllowFileAccess(true);
        this.adWv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.adWv.getSettings().setLoadsImagesAutomatically(true);
        this.adWv.getSettings().setDatabaseEnabled(true);
        this.adWv.getSettings().setSaveFormData(true);
        this.adWv.getSettings().setJavaScriptEnabled(true);
        this.adWv.getSettings().setDomStorageEnabled(true);
        this.adWv.getSettings().setDisplayZoomControls(false);
        this.adWv.getSettings().setBuiltInZoomControls(true);
        this.adWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.adWv.getSettings().setDatabaseEnabled(true);
        this.adWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        this.adWv.getSettings().setGeolocationEnabled(true);
        this.adWv.getSettings().setGeolocationDatabasePath(getFilesDir().getPath());
        this.adWv.getSettings().setSupportMultipleWindows(true);
        this.adWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.adWv.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.adWv.getSettings().setMixedContentMode(0);
        }
        if (Build.VERSION.SDK_INT < 18) {
            this.adWv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        this.adWv.getSettings().setDatabaseEnabled(true);
        this.adWv.getSettings().setLoadWithOverviewMode(true);
        this.adWv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        this.adWv.getSettings().setAppCacheEnabled(true);
        this.adWv.setScrollBarStyle(0);
        this.ws.setUseWideViewPort(true);
        this.ws.setSavePassword(true);
        this.ws.setSaveFormData(true);
        this.ws.setEnableSmoothTransition(true);
        this.cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.adWv.getSettings().setMixedContentMode(0);
            this.cookieManager.setAcceptThirdPartyCookies(this.adWv, true);
        }
        this.ws.setGeolocationEnabled(true);
    }

    public void onBackPressed() {
        if (this.adWv.canGoBack()) {
            this.adWv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
            if (this.pd.isShowing()) {
                this.pd.dismiss();
            }
            this.fl.removeAllViews();
            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}