package com.e.puzzl_library.network.model

import java.util.*

data class WorkerProfileInfo(
    val companyID : String,
    val employeeID : String,//puzzl worker id
    val first_name : String,
    val last_name : String,
    val middle_initial : String,
    val dob : String,
    val address : String,
    val city : String,
    val state : String,
    val zip : Int,
    val ssn : String,
    val phone_number: String,
    val email : String,
    val password : String
)