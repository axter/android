package com.axter.example.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.axter.example.R;

/**
 * Created by moyun on 2016/1/21.
 */
public class SecondActivity extends Activity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;
    private String loadUrl = "http://192.168.0.118/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initViews();
    }

    private void initViews() {
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.loadUrl(loadUrl);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
