package com.example.ifuturusaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class WebViewActivity extends AppCompatActivity {
 private WebView webView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView1 = (WebView) findViewById(R.id.webview1);
        webView1.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView1.loadUrl("https://console.firebase.google.com/u/0/project/ifuturus-29327/notification/compose");

    }
@Override
    public void onBackPressed(){
        if(webView1.canGoBack()) {
            webView1.goBack();
        }else{
            super.onBackPressed();
        }

}



}
