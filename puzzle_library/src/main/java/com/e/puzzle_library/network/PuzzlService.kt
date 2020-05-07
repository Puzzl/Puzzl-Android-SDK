package com.library.network

import com.e.puzzle_library.Constants
import com.library.network.api.PuzzlApi
import com.library.network.api.VeriffApi
import com.library.singletons.PuzzleSingleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PuzzlService {

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
  fun createPuzzlService () : PuzzlApi {
      val okHttpClient = baseOkHttpBuilder(SessionInterceptor()).build()
      val retrofit = baseRetrofit(okHttpClient)
      return retrofit.create(PuzzlApi::class.java)
}

    fun createSessionService(): VeriffApi {
        val okHttpClient = baseOkHttpBuilder(InterceptorVeriff()).build()
        val retrofit = baseVeriffRetrofit(okHttpClient)
        return retrofit.create(VeriffApi::class.java)
    }

private fun baseOkHttpBuilder(vararg extraInterceptors: Interceptor) = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .apply { extraInterceptors.forEach { addInterceptor(it) } }
    .addInterceptor(loggingInterceptor)

private fun baseRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .client(okHttpClient)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://api.joinpuzzl.com/mobile/")
    .build()
}

private fun baseVeriffRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .client(okHttpClient)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://api.veriff.me/v1/")
    .build()


private class SessionInterceptor (): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request =  original.rebuildWithToken()
        val response = chain.proceed(request)
        return response
    }
    private fun Request.rebuildWithToken () = this.newBuilder().header("Authorization","Bearer ${PuzzleSingleton.api_key}").build()
}

private class InterceptorVeriff (): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request =  original.rebuildWithToken()
        val response = chain.proceed(request)
        return response
    }
    private fun Request.rebuildWithToken () = this.newBuilder().header("X-AUTH-CLIENT", Constants.X_AUTH_CLIENT).build()
}
