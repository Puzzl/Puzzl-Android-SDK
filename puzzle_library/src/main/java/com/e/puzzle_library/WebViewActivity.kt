package com.e.puzzle_library

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import com.library.network.model.HelloSignResponse
import com.library.singletons.PuzzleSingleton
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webview.settings.javaScriptEnabled = true
        webview.loadUrl("https://app.joinpuzzl.com/mobile/hellosign/?signURL=${PuzzleSingleton.hellosign.signURL.replace("=","%3D").replace("&","%26")}")
        webview.addJavascriptInterface(WebAppInterface(this),"Android")
    }

    class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun parseHellosign(hellosign : HelloSignResponse) {
            Log.e("---","-------------yeeesssss")
            val a = 3
        }
    }
}
