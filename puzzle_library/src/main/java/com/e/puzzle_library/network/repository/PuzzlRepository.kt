package com.library.network.repository

import com.e.puzzle_library.network.model.*
import com.library.network.api.PuzzlApi
import com.library.network.model.SignW2Request
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

class PuzzlRepository (private val api : PuzzlApi){
    fun getUserInfo(companyId : String) = api.getUserInfo(companyId).subscribeOn(Schedulers.io())
    fun getWorkerInfo(companyId : String,puzlWorkerId : String) = api.getWorkerInfo(companyId,puzlWorkerId).subscribeOn(Schedulers.io())
    fun signW2(signW2Request: SignW2Request) = api.signW2(signW2Request).subscribeOn(Schedulers.io())
//    fun createWorker(newWorker: NewWorkerRequest) = api.createWorker(newWorker).subscribeOn(Schedulers.io())
    fun submitWorkerProfileInfo(profileInfo: WorkerProfileInfo) = api.submitWorkerProfileInfo(profileInfo).subscribeOn(Schedulers.io())
    fun submitWorkerVerification(verification: WorkerVerification) = api.submitWorkerVerification(verification).subscribeOn(Schedulers.io())
    fun submitWorkerPaperwork(paperwork: WorkerPaperwork) = api.submitWorkerPaperwork(paperwork).subscribeOn(Schedulers.io())
}