package com.bernovia.zajel

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bernovia.zajel.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebViewBinding


    companion object {
        const val PAGE_TITLE = "PAGE_TITLE"
        const val URL = "URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)

        if (intent.getStringExtra(URL) != null) {
            binding.backImageButton.setOnClickListener { finish() }

            binding.pageTitleTextView.text = intent.getStringExtra(PAGE_TITLE)

            binding.webView.loadUrl(intent.getStringExtra(URL)!!)
            binding.webView.webViewClient = WebViewClient()

            val webSettings: WebSettings = binding.webView.settings
            webSettings.domStorageEnabled = true

            webSettings.javaScriptEnabled = true
        }


    }
}