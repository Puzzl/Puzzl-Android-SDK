package com.e.puzzle_library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.library.singletons.PuzzleSingleton
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (getSupportActionBar() != null ) getSupportActionBar()?.hide()
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_web_view)


        webview.settings.javaScriptEnabled = true

        webview.loadUrl(createUrl())

        webview.addJavascriptInterface(WebViewJavascriptInterface(this),"Android")
    }

    class WebViewJavascriptInterface(private val mContext: Context) {
        @JavascriptInterface
            fun parseHellosign(message: String?) {
            (mContext as WebViewActivity).setResult(Activity.RESULT_OK)
            mContext.finish()
        }
    }
    private fun createUrl () : String{
        val url = PuzzleSingleton.hellosign.signURL.split("=")
        if (url.size == 3){
            val signature_id = url.get(1)
            val token = url.get(2)
            return "https://app.joinpuzzl.com/mobile/hellosign/?signature_id=$signature_id=$token"
        }
        return ""
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }


}
