package com.library.network.model

data class VerificationResponse(
    val id : String,
    val url : String,
    val host : String,
    val status : String,
    val sessionToken : String
)