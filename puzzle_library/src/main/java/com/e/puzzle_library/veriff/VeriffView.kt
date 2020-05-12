package com.library.veriff

interface VeriffView {
    fun startVeriff(sessionToken : String,url : String)
    fun error(error : String)
    fun signW2()
    fun checkPermission()
    fun showProgressbar()
    fun hintProgressBar()
}