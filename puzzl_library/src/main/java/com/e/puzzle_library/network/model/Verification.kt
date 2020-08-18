package com.library.network.model

data class Verification(
    val person : Person,
    val document : Document,
    val lang : String,
    val timestamp : String
)