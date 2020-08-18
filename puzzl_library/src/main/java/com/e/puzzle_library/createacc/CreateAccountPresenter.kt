package com.library.createacc

import com.e.puzzl_library.network.model.*
import com.google.android.material.textfield.TextInputEditText
import com.library.network.model.SignW2Request
import com.library.network.repository.PuzzlRepository
import com.library.singletons.PuzzleSingleton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class CreateAccountPresenter (private val repository : PuzzlRepository,private  val view : CreateAccountView){
    private val disposable  = CompositeDisposable()
    private val views = mutableListOf<TextInputEditText>()
    fun signW2(signW2Request: SignW2Request){
        view.showProgressbar()
        disposable.add(repository.signW2(signW2Request).observeOn(AndroidSchedulers.mainThread()).subscribe({
            result ->
            view.hintProgressBar()
                PuzzleSingleton.hellosign = result
            println("START HELLO SIGN")
            view.showHellosign()
        },{error -> view.hintProgressBar()}))
    }

    fun initFields(email : TextInputEditText, password : TextInputEditText, confirmPassword : TextInputEditText){
        views.add(email)
        views.add(password)
        views.add(confirmPassword)
    }

    fun submitPaperwork(paperwork: WorkerPaperwork ){
        view.showProgressbar()
        disposable.add(repository.submitWorkerPaperwork(paperwork).observeOn(AndroidSchedulers.mainThread()).subscribe({
            result ->
            view.hintProgressBar()
            view.showFinishScreen()
        },{error -> view.hintProgressBar()}))
    }

    fun submitWorkerProfileInfo(profileInfo: WorkerProfileInfo){
        view.showProgressbar()
        disposable.add(repository.submitWorkerProfileInfo(profileInfo).observeOn(AndroidSchedulers.mainThread()).subscribe({
                result ->
            view.hintProgressBar()
        },{error -> view.hintProgressBar()}))
    }

    fun submitWorkerVerification(verification: WorkerVerification){
        view.showProgressbar()
        disposable.add(repository.submitWorkerVerification(verification).observeOn(AndroidSchedulers.mainThread()).subscribe({
                result ->
            view.hintProgressBar()
        },{error -> view.hintProgressBar()}))
    }

    fun checkPassword(password : String,confirmPassword : String) : Boolean{
        if (!password.equals(confirmPassword)){
            view.confirmPasswordError()
            return false
        }
        return true
    }

    fun checkEmptyFields() : Boolean{
        for (viev in views) {
            if (viev.text.toString().isEmpty()) {
                view.confirmPasswordError()
                return false
            }
        }
        return true
    }

    fun destroyPresenter() = disposable.dispose()
}
