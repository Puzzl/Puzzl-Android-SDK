package com.library.network.model

data class SignW2Request(
    val companyID : String,
    val first_name : String,
    val last_name : String,
    val middle_initial : String,
    val address : String,
    val city : String,
    val state : String,
    val zip : Int,
    val ssn : String,
    val dob : String,
    val email : String,
    val phone_number : String
)