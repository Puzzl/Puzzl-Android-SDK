package com.library.network.api


import com.e.puzzle_library.network.model.SignW2Response
import com.library.network.model.SignW2Request
import com.library.network.model.businessName.BusinessNameResponse
import com.library.network.model.workerInfo.WorkerInfoResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PuzzlApi {
    @GET("getUserInfo")
    fun getUserInfo(@Query("companyID") companyId : String) : io.reactivex.Observable<BusinessNameResponse>
    @GET("getWorkerInfo")
    fun getWorkerInfo(@Query("companyID") companyId : String,@Query("id") id : String) : io.reactivex.Observable<WorkerInfoResponse>
    @POST("signW2")
    fun signW2(@Body signInW2 : SignW2Request) : io.reactivex.Observable<SignW2Response>
}