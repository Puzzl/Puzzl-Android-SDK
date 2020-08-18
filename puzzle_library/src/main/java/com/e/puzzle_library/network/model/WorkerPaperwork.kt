package com.e.puzzle_library.network.model

data class WorkerPaperwork(
    val companyID : String,
    val employeeID : String,//puzzl worker id
    val email : String,
    val employee_sigId : String,
    val company_sigId : String,
    val signature_request_id : String
)