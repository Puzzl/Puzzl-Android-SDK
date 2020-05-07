package com.library.createacc

import com.e.puzzle_library.network.model.NewWorkerRequest
import com.e.puzzle_library.network.model.SignW2Response
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
        disposable.add(repository.signW2(signW2Request).observeOn(AndroidSchedulers.mainThread()).subscribe({
            result ->
                PuzzleSingleton.hellosign = result
            view.showHellosign()
        },{error -> }))
    }

    fun initFields(email : TextInputEditText, password : TextInputEditText, confirmPassword : TextInputEditText){
        views.add(email)
        views.add(password)
        views.add(confirmPassword)
    }

    fun createNewWorker(newWorker : NewWorkerRequest){
        disposable.add(repository.createWorker(newWorker).observeOn(AndroidSchedulers.mainThread()).subscribe({
            result -> view.showFinishScreen()
        },{error -> }))
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
