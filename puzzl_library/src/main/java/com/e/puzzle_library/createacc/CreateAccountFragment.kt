package com.e.puzzl_library.createacc

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
import com.e.puzzl_library.*

import com.e.puzzl_library.network.model.NewWorkerRequest
import com.e.puzzl_library.network.model.WorkerPaperwork
import com.e.puzzl_library.network.model.WorkerProfileInfo
import com.e.puzzl_library.network.model.WorkerVerification
import com.google.android.material.textfield.TextInputEditText
import com.library.createacc.CreateAccountPresenter
import com.library.createacc.CreateAccountView
import com.library.network.model.SignW2Request
import com.library.network.model.workerInfo.WorkerInfoModel
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
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CreateAccountFragment : Fragment(), VeriffView, CreateAccountView, RadioGroup.OnCheckedChangeListener {
    @Inject lateinit var veriffRepository: VeriffRepository
    @Inject lateinit var puzzlRepository: PuzzlRepository
    lateinit var presenterCreateAcc : CreateAccountPresenter
    lateinit var presenterVeriff : VeriffPresenter
    private var document = Constants.PASSPORT
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);


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
        edt_email.setText(PuzzleSingleton.workerInfoModel.email)
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
        presenterCreateAcc.submitWorkerProfileInfo(
            WorkerProfileInfo(
                Constants.PUZZL_COMPANY_ID,
                Constants.PUZZL_WORKER_ID,
                UserSingleton.firstName,
                UserSingleton.lastName,
                UserSingleton.middle_initial,
                UserSingleton.dob,
                UserSingleton.address,
                UserSingleton.city,
                UserSingleton.state,
                UserSingleton.zip,
                UserSingleton.ssn,
                UserSingleton.phoneNumber,
                UserSingleton.email,
                UserSingleton.password
            )
        )
        presenterCreateAcc.submitWorkerVerification(
            WorkerVerification(
                Constants.PUZZL_COMPANY_ID,
                Constants.PUZZL_WORKER_ID,
                PuzzleSingleton.veriff.id
            )
        )
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        presenterCreateAcc.signW2(
            SignW2Request(
                Constants.PUZZL_COMPANY_ID,
                UserSingleton.firstName,
                UserSingleton.lastName,
                UserSingleton.middle_initial,
                UserSingleton.address,
                UserSingleton.city,
                UserSingleton.state,
                UserSingleton.zip,
                UserSingleton.ssn,
                UserSingleton.dob,
                UserSingleton.email,
                UserSingleton.phoneNumber
            )
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
        }else if (requestCode == Constants.HELLOSIGN_REQUEST_CODE ) {
            if (resultCode == Activity.RESULT_OK) {
                //Submit Paperwork
                getUserInfo()
                presenterCreateAcc.submitPaperwork(
                    WorkerPaperwork(
                        Constants.PUZZL_COMPANY_ID,
                        Constants.PUZZL_WORKER_ID,
                        UserSingleton.email,
                        PuzzleSingleton.hellosign.employee_sigId,
                        PuzzleSingleton.hellosign.company_sigId,
                        PuzzleSingleton.hellosign.signature_request_id
                    )
                )
                (activity as VeriffActivity).replaceFragment(FinishFragment(),true)

            }else{
                Toast.makeText(context,"You didn't fill in the document",Toast.LENGTH_LONG).show()
            }
        }else if (requestCode == Constants.TAKE_PHOTO && data != null && resultCode == Activity.RESULT_OK){
            //Submit Worker Profile
            getUserInfo()

            val imageBitmap = data.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            //TODO: get SSCard URL
            val url = HttpUrl.parse("https://api.joinpuzzl.com/generate-sscard-put-url")?.newBuilder()
                ?.addQueryParameter("Key", UserSingleton.email + "-sscard")?.addQueryParameter("ContentType", "image/png")
                ?.build()
            println("BUILT URL")
            val getSSCardRequest =  Request.Builder().url(url).addHeader("Content-Type", "image/png").build()
            println("CREATED REQUEST")
            val SSCardclient = OkHttpClient()
//            val SSCardUrl = ""
            println("ESTABLISHED CLIENT")
//            val SSCardResponse = SSCardclient.newCall(getSSCardRequest).execute()
//            println("SENT QUERY AND GOT RESPONSE")
//            val token = SSCardResponse.body()?.string()
//            if(SSCardResponse.code() == 200){
//                println("GET SSCARD URL SUCCESS")
//                println(token)
//            }
            SSCardclient.newCall(getSSCardRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

//                        println(response.body()?.string())
                        val body = response.body()?.string()
                        println("LETS GET THE URL!!!!!!!!!!!!!")
                        val Jobject = JSONObject(body)
                        println("LETS GET THE BODY")
                        println(body)
                        val tempURL = Jobject.getJSONObject("data").get("putURL")
                        val SSCardURL = HttpUrl.parse(tempURL.toString())?.newBuilder()?.build()
                        val SSCardBody = RequestBody.create(MediaType.parse("image/png"), byteArray)
                        val uploadSSCard = Request.Builder().url(SSCardURL).put(SSCardBody).addHeader("Content-Type", "image/png").build()

                        SSCardclient.newCall(uploadSSCard).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                response.use {
                                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                                    println()
                                    println("SUCCESSFUL PUSH GO CHECK S3")
                                    println(document)
                                    startVeriff(PuzzleSingleton.veriff.sessionToken, PuzzleSingleton.veriff.url)

                                }
                            }
                        })
                    }
                }
            })


            //TODO: Put SSCard URL



//            presenterVeriff.startSession(document)
//            signW2()
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
        println("SHOW HELLO SIGN")
        startActivityForResult(Intent(context, WebViewActivity::class.java),Constants.HELLOSIGN_REQUEST_CODE)
    }

    override fun showFinishScreen() {
        (activity as VeriffActivity).replaceFragment(FinishFragment(),true)
    }
}
