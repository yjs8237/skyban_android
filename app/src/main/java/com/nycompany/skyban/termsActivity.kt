package com.nycompany.skyban

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_terms.*

class termsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        //webview.webViewClient = null
        webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        })
        webview.loadUrl("file:///android_asset/test.html")

        backBtn.setOnClickListener{
            finish()
        }

        agreeBtn.setOnClickListener{
            startActivity(Intent().setClass(this, RegisterActivity::class.java))
            finish()
        }
    }
}
