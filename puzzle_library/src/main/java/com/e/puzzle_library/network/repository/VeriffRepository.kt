package com.library.network.repository

import com.library.network.api.VeriffApi
import com.library.network.model.CreateSessionModel
import io.reactivex.schedulers.Schedulers

class VeriffRepository(private val api : VeriffApi) {
    fun getSession(model  : CreateSessionModel) = api.getSessions(model).subscribeOn(Schedulers.io())
}
