package com.library.network.model

data class SessionResponse(
    val status : String,
    val verification : VerificationResponse
)