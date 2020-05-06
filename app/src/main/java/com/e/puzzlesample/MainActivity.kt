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

        check_library.setOnClickListener { startActivity(Intent(this,OnBoardingActivity::class.java)) }
    }
}
