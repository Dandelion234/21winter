package com.example.twthomeworkjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContentMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_message);
        if (getSupportActionBar() !=null){      //隐藏标题栏
            getSupportActionBar().hide();
        }
        String data=getIntent().getStringExtra("url");
        Log.d("abcdef",data);
        WebView webView=findViewById(R.id.webView);
        webView.loadUrl(data);
        //webView.loadDataWithBaseURL(null,data,"text/html","utf-8",null);
        WebSettings webSettings=webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
}