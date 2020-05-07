package com.e.puzzlesample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.library.onboarding.OnBoardingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val PUZZL_LIVE_API_KEY = "ec8317abe03e4989a33a971b547742a1"

        check_library.setOnClickListener {
            val intent = Intent(this,OnBoardingActivity::class.java)
            intent.putExtra("api_key",PUZZL_LIVE_API_KEY)
            startActivity(intent)
        }
    }
}
