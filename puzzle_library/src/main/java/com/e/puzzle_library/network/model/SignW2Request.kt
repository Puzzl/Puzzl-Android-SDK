package com.library.network.model

data class SignW2Request(
    val companyID : String,
    val first_name : String,
    val last_name : String,
    val middle_initial : String,
    val title : String,
    val address : String,
    val city : String,
    val state : String,
    val zip : Int,
    val ssn : String,
    val email : String,
    val default_wage : Int,
    val default_ot_wage : Int,
    val createdAt : String
)