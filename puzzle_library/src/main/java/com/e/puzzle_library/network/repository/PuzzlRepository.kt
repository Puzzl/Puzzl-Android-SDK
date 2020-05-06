package com.library.network.repository

import com.library.network.api.PuzzlApi
import com.library.network.model.SignW2Request
import io.reactivex.schedulers.Schedulers

class PuzzlRepository (private val api : PuzzlApi){
    fun getUserInfo(companyId : String) = api.getUserInfo(companyId).subscribeOn(Schedulers.io())
    fun getWorkerInfo(companyId : String,puzlWorkerId : String) = api.getWorkerInfo(companyId,puzlWorkerId).subscribeOn(Schedulers.io())
    fun signW2(signW2Request: SignW2Request) = api.signW2(signW2Request).subscribeOn(Schedulers.io())
}