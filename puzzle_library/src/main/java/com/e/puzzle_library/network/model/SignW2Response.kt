package com.e.puzzle_library.network.model

data class SignW2Response(
    val signURL : String,
    val employee_sigId : String,
    val company_sigId : String,
    val signature_request_id : String
)