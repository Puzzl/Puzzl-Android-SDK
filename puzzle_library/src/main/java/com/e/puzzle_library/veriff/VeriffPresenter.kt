package com.library.veriff

import com.library.network.model.CreateSessionModel
import com.library.network.model.Document
import com.library.network.model.Person
import com.library.network.model.Verification
import com.library.network.repository.VeriffRepository
import com.library.singletons.PuzzleSingleton
import com.library.singletons.UserSingleton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import mobi.lab.veriff.data.Veriff
import mobi.lab.veriff.data.VeriffConstants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class VeriffPresenter (private val repository : VeriffRepository,private val view : VeriffView){
    private val disposable = CompositeDisposable()
    fun startSession(document : String){
        disposable.add(repository.getSession(
            CreateSessionModel(
                Verification(
                    Person(UserSingleton.firstName,UserSingleton.lastName),
                    Document(document,"US"),"en",PuzzleSingleton.workerInfoModel.createdAt
                )
            )
        ).observeOn(AndroidSchedulers.mainThread()).subscribe({
                result -> view.startVeriff(result.verification.sessionToken,result.verification.url)
        },{error -> view.error("Oops,something went wrong!")}))
    }

    fun destroyPresenter() = disposable.dispose()

    fun handleVeriffResult(statusCode : Int,sessionToken : String){
        if (statusCode == VeriffConstants.STATUS_USER_FINISHED) {
            view.successVeriff(sessionToken)
        } else if (statusCode == VeriffConstants.STATUS_ERROR_NO_IDENTIFICATION_METHODS_AVAILABLE) {
            view.error("No identifications methods available")
        } else if (statusCode == VeriffConstants.STATUS_ERROR_SETUP) {
            view.error("issue with the provided vendor data")
        } else if (statusCode == VeriffConstants.STATUS_ERROR_UNKNOWN) {
            view.error("Unknown error occurred.")
        } else if (statusCode == VeriffConstants.STATUS_ERROR_NETWORK) {
            view.error("Network is unavailable.")
        } else if (statusCode == VeriffConstants.STATUS_USER_CANCELED) {
            view.error("User cancelled the session.")
        } else if (statusCode == VeriffConstants.STATUS_UNABLE_TO_ACCESS_CAMERA) {
            view.error("Failed to access device's camera. (either access denied or there are no usable cameras")
        } else if (statusCode == VeriffConstants.STATUS_UNABLE_TO_START_CAMERA) {
            view.error("Failed to start the device's camera.")
        } else if (statusCode == VeriffConstants.STATUS_UNABLE_TO_RECORD_AUDIO) {
            view.error(" Failed to access device's microphone.")
        } else if (statusCode == VeriffConstants.STATUS_SUBMITTED) {
            view.error("Photos are successfully uploaded.")
        } else if (statusCode == VeriffConstants.STATUS_ERROR_SESSION) {
            view.error("Invalid sessionToken is passed to the Veriff SDK.")
        } else if (statusCode == VeriffConstants.STATUS_DONE) {
            view.error("Session is completed from clients perspective.")
        } else if (statusCode == VeriffConstants.STATUS_ERROR_UNSUPPORTED_SDK_VERSION) {
            view.error("This version of Veriff SDK is deprecated.")
        }
    }
}