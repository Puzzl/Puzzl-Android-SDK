package com.e.puzzle_library

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.JavascriptInterface
import com.library.network.model.HelloSignResponse
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
        webview.addJavascriptInterface(WebAppInterface(this),"Android")
    }

    class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun parseHellosign(hellosign : HelloSignResponse) {
            Log.e("---","-------------yeeesssss")
            val a = 3
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

    private fun finishScreen(){
        val intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishScreen()
    }
}
