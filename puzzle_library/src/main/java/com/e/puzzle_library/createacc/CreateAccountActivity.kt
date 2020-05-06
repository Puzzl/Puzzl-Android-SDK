package com.library.createacc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.puzzle_library.Constants
import com.e.puzzle_library.R
import com.e.puzzle_library.WebViewActivity
import com.e.puzzle_library.network.model.SignW2Response
import com.google.android.material.textfield.TextInputEditText
import com.library.network.model.SignW2Request
import com.library.network.repository.PuzzlRepository
import com.library.network.repository.VeriffRepository
import com.library.singletons.PuzzleSingleton
import com.library.singletons.UserSingleton
import com.library.veriff.VeriffPresenter
import com.library.veriff.VeriffView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_create_account.*
import mobi.lab.veriff.data.VeriffConstants
import javax.inject.Inject


class CreateAccountActivity : Activity(), VeriffView,CreateAccountView, RadioGroup.OnCheckedChangeListener {
    @Inject
    lateinit var veriffRepository: VeriffRepository
    @Inject lateinit var puzzlRepository: PuzzlRepository
    lateinit var presenterCreateAcc : CreateAccountPresenter
    lateinit var presenterVeriff : VeriffPresenter
    private var document = Constants.PASSPORT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_create_account)
        presenterVeriff = VeriffPresenter(veriffRepository,this)
        presenterCreateAcc = CreateAccountPresenter(puzzlRepository,this)
        close_app.setOnClickListener { finishAffinity() }

        create_account.setOnClickListener {
            if (presenterCreateAcc.checkEmptyFields() && presenterCreateAcc.checkPassword(edt_password.text.toString(),edt_confirm_password.text.toString())) presenterVeriff.startSession(document)
        }

        presenterCreateAcc.initFields(edt_email,edt_password,edt_confirm_password)

        radio_passport.isChecked = true
    }

    override fun startVeriff(sessionToken: String, url: String) {
//        val veriffBuilder = Veriff.Builder(url,sessionToken)
//        veriffBuilder.launch(this,Constants.VERIFF_REQUEST_CODE)
        getUserInfo()
        presenterCreateAcc.signW2(
            SignW2Request(Constants.PUZZL_COMPANY_ID,
                UserSingleton.firstName, UserSingleton.last4_ssn,"Vlad",PuzzleSingleton.workerInfoModel.title,
        UserSingleton.address,UserSingleton.city,UserSingleton.state,UserSingleton.zip,UserSingleton.ssn,UserSingleton.email,PuzzleSingleton.workerInfoModel.default_wage,
            PuzzleSingleton.workerInfoModel.default_ot_wage,PuzzleSingleton.workerInfoModel.createdAt)
        )
    }

    override fun error(error : String) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show()
    }

    override fun successVeriff(sessionToken: String) {
        startActivityForResult(Intent(this, WebViewActivity::class.java), Constants.HELLOSIGN_REQUEST_CODE)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterVeriff.destroyPresenter()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.VERIFF_REQUEST_CODE && data != null){
            val statusCode = data.getIntExtra(VeriffConstants.INTENT_EXTRA_STATUS,Integer.MIN_VALUE)
            val sessionCode = data.getStringExtra(VeriffConstants.INTENT_EXTRA_SESSION_URL)
            presenterVeriff.handleVeriffResult(statusCode,sessionCode)
        }else if (requestCode == Constants.HELLOSIGN_REQUEST_CODE && data != null){

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserInfo(){
        UserSingleton.email = edt_email.text.toString()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (radio_passport.isChecked) document = Constants.PASSPORT else document = Constants.DRIVERS_LICENSE
    }

    override fun showEmptyFields(field: TextInputEditText) {
        field.setError("Empty field")
    }

    override fun confirmPasswordError() {
        edt_confirm_password.setError("Password mismatch")
    }

    override fun showHellosign() {
        startActivityForResult(Intent(this,WebViewActivity::class.java),Constants.HELLOSIGN_REQUEST_CODE)
    }
}
