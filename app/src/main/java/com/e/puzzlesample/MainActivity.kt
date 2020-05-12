package com.e.puzzlesample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.e.puzzle_library.VeriffActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val PUZZL_LIVE_API_KEY = "ec8317abe03e4989a33a971b547742a1"

        check_library.setOnClickListener {
            val intent = Intent(this,VeriffActivity::class.java)
            intent.putExtra("api_key",PUZZL_LIVE_API_KEY)
            startActivityForResult(intent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null ) {
            Toast.makeText(this,data.getStringExtra("result"),Toast.LENGTH_LONG).show()
        }
    }
}
