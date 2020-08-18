package com.e.puzzlsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.e.puzzl_library.VeriffActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val REQUEST_CODE  = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val PUZZL_LIVE_API_KEY = "<YOUR PUZZL LIVE KEY>"
        val PUZZL_EMPLOYEE_ID = "<YOUR PUZZL EMPLOYEE ID>"
        val PUZZL_COMPANY_ID = "<YOUR PUZZL COMPANY ID>"


        check_library.setOnClickListener {
            val intent = Intent(this,VeriffActivity::class.java)
            intent.putExtra("api_key",PUZZL_LIVE_API_KEY)
            intent.putExtra("employeeID", PUZZL_EMPLOYEE_ID)
            intent.putExtra("companyID", PUZZL_COMPANY_ID)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null ) {
            Toast.makeText(this,data.getStringExtra("result"),Toast.LENGTH_LONG).show()
        }
    }
}
