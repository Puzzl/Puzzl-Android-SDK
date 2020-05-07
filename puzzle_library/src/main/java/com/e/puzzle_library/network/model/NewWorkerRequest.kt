package com.e.puzzle_library.network.model

data class NewWorkerRequest(
    val companyID : String,
    val id : String,//puzzl worker id
    val first_name : String,
    val last_name : String,
    val middle_initial : String,
    val dob : String,
    val title : String,
    val address : String,
    val city : String,
    val state : String,
    val zip : Int,
    val ssn : String,
    val last4_ssn : String,
    val email : String,
    val password : String,
    val default_wage : Int,
    val default_ot_wage : Int,
    val createdAt : String,
    val employee_sigId : String,
    val company_sigId : String,
    val signature_request_id : String,
    val veriff_id : String,
    val ss_card : List<Byte>?
)