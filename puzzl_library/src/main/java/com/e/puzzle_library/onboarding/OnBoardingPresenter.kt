package com.library.onboarding

import com.library.network.repository.PuzzlRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class OnBoardingPresenter (val puzzlRepository: PuzzlRepository, val view : OnBoardingView){
    val disposable = CompositeDisposable()
    fun getUserInfo(companyId : String){
        disposable.add(puzzlRepository.getUserInfo(companyId).observeOn(AndroidSchedulers.mainThread()).subscribe({
            result -> view.saveBusinessName(result.data)

        },{error ->}))
    }
    fun getWorkerInfo(companyId : String,id : String){
        disposable.add(puzzlRepository.getWorkerInfo(companyId,id).observeOn(AndroidSchedulers.mainThread()).subscribe({
                result -> view.saveWorkerInfo(result.data)
        },{error ->}))
    }

    fun destroyPresenter() = disposable.dispose()
}