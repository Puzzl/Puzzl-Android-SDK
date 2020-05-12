package com.library.createacc

import android.Manifest
import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.puzzle_library.Constants
import com.e.puzzle_library.FinishActivity
import com.e.puzzle_library.R
import com.e.puzzle_library.WebViewActivity
import com.e.puzzle_library.network.model.NewWorkerRequest
import com.google.android.material.textfield.TextInputEditText
import com.library.network.model.SignW2Request
import com.library.network.repository.PuzzlRepository
import com.library.network.repository.VeriffRepository
import com.library.singletons.PuzzleSingleton
import com.library.singletons.UserSingleton
import com.library.veriff.VeriffPresenter
import com.library.veriff.VeriffView
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_create_account.*
import mobi.lab.veriff.data.Veriff
import mobi.lab.veriff.data.VeriffConstants
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class CreateAccountActivity : AppCompatActivity(), VeriffView,CreateAccountView, RadioGroup.OnCheckedChangeListener {
    @Inject
    lateinit var veriffRepository: VeriffRepository
    @Inject lateinit var puzzlRepository: PuzzlRepository
    lateinit var presenterCreateAcc : CreateAccountPresenter
    lateinit var presenterVeriff : VeriffPresenter
    private var document = Constants.PASSPORT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (getSupportActionBar() != null ) getSupportActionBar()?.hide()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_create_account)
        presenterVeriff = VeriffPresenter(veriffRepository,this)
        presenterCreateAcc = CreateAccountPresenter(puzzlRepository,this)
        close_app.setOnClickListener { finishAffinity() }

        create_account.setOnClickListener {
            if (presenterCreateAcc.checkEmptyFields() && presenterCreateAcc.checkPassword(edt_password.text.toString(),edt_confirm_password.text.toString())){
                presenterVeriff.startSession(document)
            }
        }

        presenterCreateAcc.initFields(edt_email,edt_password,edt_confirm_password)

        radio_passport.isChecked = true

        radioGroup.setOnCheckedChangeListener(this)
    }

    private fun showCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, Constants.TAKE_PHOTO)
            }
        }
    }

    override fun startVeriff(sessionToken: String, url: String) {
        val veriffBuilder = Veriff.Builder(url,sessionToken)
        veriffBuilder.launch(this,Constants.VERIFF_REQUEST_CODE)
    }

    override fun signW2() {
        getUserInfo()
        presenterCreateAcc.signW2(SignW2Request(Constants.PUZZL_COMPANY_ID,
            UserSingleton.firstName, UserSingleton.last4_ssn,"Vlad",PuzzleSingleton.workerInfoModel.title,
            UserSingleton.address,UserSingleton.city,UserSingleton.state,UserSingleton.zip,UserSingleton.ssn,UserSingleton.email,PuzzleSingleton.workerInfoModel.default_wage,
            PuzzleSingleton.workerInfoModel.default_ot_wage,PuzzleSingleton.workerInfoModel.createdAt)
        )
    }

    override fun checkPermission() {
        var subscribe = RxPermissions(this)
            .request(Manifest.permission.CAMERA)
            .subscribe { granted -> if (granted) showCamera() }
    }

    override fun error(error : String) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show()
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
        }else if (requestCode == Constants.HELLOSIGN_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            presenterCreateAcc.createNewWorker(NewWorkerRequest(Constants.PUZZL_COMPANY_ID,Constants.PUZZL_WORKER_ID,UserSingleton.firstName,UserSingleton.lastName,
            UserSingleton.middle_initial,UserSingleton.dob,PuzzleSingleton.workerInfoModel.title,UserSingleton.address,UserSingleton.city,UserSingleton.state,
                UserSingleton.zip,UserSingleton.ssn,UserSingleton.last4_ssn,UserSingleton.email,UserSingleton.password,PuzzleSingleton.workerInfoModel.default_wage,
            PuzzleSingleton.workerInfoModel.default_ot_wage,PuzzleSingleton.workerInfoModel.createdAt,PuzzleSingleton.hellosign.employee_sigId,PuzzleSingleton.hellosign.company_sigId,
            PuzzleSingleton.hellosign.signature_request_id,PuzzleSingleton.veriff.id,null))
        }else if (requestCode == Constants.TAKE_PHOTO && data != null && resultCode == Activity.RESULT_OK){
            val imageBitmap = data.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            signW2()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserInfo(){
        UserSingleton.email = edt_email.text.toString()
        UserSingleton.password = edt_password.text.toString()
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

    override fun showFinishScreen() {
        startActivity(Intent(this,FinishActivity::class.java))
    }
}
