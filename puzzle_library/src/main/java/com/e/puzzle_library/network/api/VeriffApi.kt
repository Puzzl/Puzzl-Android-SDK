package com.library.network.api

import com.library.network.model.CreateSessionModel
import com.library.network.model.SessionResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VeriffApi {
    @POST("sessions")
    fun getSessions(@Body model : CreateSessionModel) : Observable<SessionResponse>
}