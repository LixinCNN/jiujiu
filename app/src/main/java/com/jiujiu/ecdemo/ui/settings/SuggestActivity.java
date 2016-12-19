package com.jiujiu.ecdemo.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jiujiu.ecdemo.common.CCPAppManager;
import com.jiujiu.ecdemo.ui.ECSuperActivity;


public class SuggestActivity extends ECSuperActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.activity_web_suggest_url;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt,
                -1, null,
                null,
                getString(com.jiujiu.ecdemo.R.string.app_suggest), null, this);

          WebView mWebView =(WebView)findViewById(com.jiujiu.ecdemo.R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSaveFormData(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        String url = "http://imslb.yuntongxun.com:8999/2016-08-15/Application/"+ CCPAppManager.getClientUser().getAppKey()+"/IMPlus/Suggestion.shtml?userName="+CCPAppManager.getClientUser().getUserId();
        mWebView.loadUrl(url);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
        }

    }
}
