package com.e.puzzle_library.createacc

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.e.puzzle_library.*

import com.e.puzzle_library.network.model.NewWorkerRequest
import com.google.android.material.textfield.TextInputEditText
import com.library.createacc.CreateAccountPresenter
import com.library.createacc.CreateAccountView
import com.library.network.model.SignW2Request
import com.library.network.repository.PuzzlRepository
import com.library.network.repository.VeriffRepository
import com.library.singletons.PuzzleSingleton
import com.library.singletons.UserSingleton
import com.library.veriff.VeriffPresenter
import com.library.veriff.VeriffView
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_create_account.*
import mobi.lab.veriff.data.Veriff
import mobi.lab.veriff.data.VeriffConstants
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class CreateAccountFragment : Fragment(), VeriffView, CreateAccountView, RadioGroup.OnCheckedChangeListener {
    @Inject lateinit var veriffRepository: VeriffRepository
    @Inject lateinit var puzzlRepository: PuzzlRepository
    lateinit var presenterCreateAcc : CreateAccountPresenter
    lateinit var presenterVeriff : VeriffPresenter
    private var document = Constants.PASSPORT

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenterVeriff = VeriffPresenter(veriffRepository,this)
        presenterCreateAcc = CreateAccountPresenter(puzzlRepository,this)
        close_app.setOnClickListener { (activity as VeriffActivity).finishSetResult()}

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
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, Constants.TAKE_PHOTO)
                }
            }
        }
    }

    override fun showProgressbar() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hintProgressBar(){
        progressbar.visibility = View.GONE
    }

    override fun startVeriff(sessionToken: String, url: String) {
        val veriffBuilder = Veriff.Builder(url,sessionToken)
        veriffBuilder.launch(activity,Constants.VERIFF_REQUEST_CODE)
    }

    override fun signW2() {
        getUserInfo()
        presenterCreateAcc.signW2(
            SignW2Request(Constants.PUZZL_COMPANY_ID,
            UserSingleton.firstName, UserSingleton.last4_ssn,"Vlad", PuzzleSingleton.workerInfoModel.title,
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
        Toast.makeText(context,error,Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterVeriff.destroyPresenter()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.VERIFF_REQUEST_CODE && data != null){
            val statusCode = data.getIntExtra(VeriffConstants.INTENT_EXTRA_STATUS,Integer.MIN_VALUE)
            val sessionCode = data.getStringExtra(VeriffConstants.INTENT_EXTRA_SESSION_URL)
            presenterVeriff.handleVeriffResult(statusCode,sessionCode)
        }else if (requestCode == Constants.HELLOSIGN_REQUEST_CODE ){
            presenterCreateAcc.createNewWorker(
                NewWorkerRequest(Constants.PUZZL_COMPANY_ID,Constants.PUZZL_WORKER_ID,UserSingleton.firstName,UserSingleton.lastName,
                UserSingleton.middle_initial,UserSingleton.dob,PuzzleSingleton.workerInfoModel.title,UserSingleton.address,UserSingleton.city,UserSingleton.state,
                UserSingleton.zip,UserSingleton.ssn,UserSingleton.last4_ssn,UserSingleton.email,UserSingleton.password,PuzzleSingleton.workerInfoModel.default_wage,
                PuzzleSingleton.workerInfoModel.default_ot_wage,PuzzleSingleton.workerInfoModel.createdAt,PuzzleSingleton.hellosign.employee_sigId,PuzzleSingleton.hellosign.company_sigId,
                PuzzleSingleton.hellosign.signature_request_id,PuzzleSingleton.veriff.id,null)
            )
        }else if (requestCode == Constants.TAKE_PHOTO && data != null && resultCode == Activity.RESULT_OK){
            val imageBitmap = data.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            signW2()
        }
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
        startActivityForResult(Intent(context, WebViewActivity::class.java),Constants.HELLOSIGN_REQUEST_CODE)
    }

    override fun showFinishScreen() {
        (activity as VeriffActivity).replaceFragment(FinishFragment(),true)
    }
}
