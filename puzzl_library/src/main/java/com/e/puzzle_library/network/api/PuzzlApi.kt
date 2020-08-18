package com.library.network.api


import com.e.puzzl_library.network.model.*
import com.library.network.model.SignW2Request
import com.library.network.model.businessName.BusinessNameResponse
import com.library.network.model.workerInfo.WorkerInfoResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface PuzzlApi {
//    @Headers("Authorization: Bearer ce1bc81624c943c6b47a9fbbac2aac53")
    @GET("getUserInfo")
    fun getUserInfo(@Query("companyID") companyId : String) : io.reactivex.Observable<BusinessNameResponse>
    @GET("getWorkerInfo")
    fun getWorkerInfo(@Query("companyID") companyId : String,@Query("employeeID") id : String) : io.reactivex.Observable<WorkerInfoResponse>
    @POST("submitWorkerProfileInfo")
    fun submitWorkerProfileInfo(@Body profileInfo: WorkerProfileInfo) : io.reactivex.Observable<ResponseBody>
    @POST("submitWorkerVerification")
    fun submitWorkerVerification(@Body verification: WorkerVerification) : io.reactivex.Observable<ResponseBody>
    @POST("signW2")
    fun signW2(@Body signInW2 : SignW2Request) : io.reactivex.Observable<SignW2Response>
    @POST("submitWorkerPaperwork")
    fun submitWorkerPaperwork(@Body paperwork: WorkerPaperwork) : io.reactivex.Observable<ResponseBody>
//    @POST("onboardWorker")
//    fun createWorker(@Body newWorker  : NewWorkerRequest) : io.reactivex.Observable<ResponseBody>

}