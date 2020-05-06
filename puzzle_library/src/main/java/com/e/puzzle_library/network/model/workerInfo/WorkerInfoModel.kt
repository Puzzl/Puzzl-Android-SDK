package com.library.network.model.workerInfo

data class WorkerInfoModel(
    val first_name : String,
    val last_name : String,
    val title : String,
    val email : String,
    val default_wage : Int,
    val default_ot_wage : Int,
    val default_dt_wage : Int,
    val createdAt : String
)