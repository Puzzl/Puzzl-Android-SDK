package com.library.network.model

data class HelloSignResponse(
    val signURL : String,
    val employee_sigId : String,
    val company_sigId : String,
    val signature_request_id : String,
    val status : String
)