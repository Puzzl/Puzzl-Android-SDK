package com.e.puzzl_library

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import com.library.singletons.PuzzleSingleton
import kotlinx.android.synthetic.main.activity_web_view.*
import org.json.JSONObject


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
        val url = createUrl()

        println("URL")
        println(url)
        webview.settings.builtInZoomControls = true;
        webview.settings.displayZoomControls = false;
        webview.loadUrl(url)

        println("**** ADDING LISTENER ****")
        webview.addJavascriptInterface(WebViewJavascriptInterface(this),"Android")
    }

    class WebViewJavascriptInterface(private val mContext: Context) {
        @JavascriptInterface
            fun parseHellosign(message: String?) {
            try {
                val data = JSONObject(message) //Convert from string to object, can also use JSONArray
                println("************ PARSE HELLO SIGN ************")
                println(data)
                println("************ END HELLO SIGN ************")
                if(data.get("status") == "finished"){
                    (mContext as WebViewActivity).setResult(Activity.RESULT_OK)
                    mContext.finish()
                }
            } catch (ex: Exception) {
                println("ERROR WITH HELLO SIGN STRING")
            }
            //TODO: check if JSON object is "finished", then send result ok and finish
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
