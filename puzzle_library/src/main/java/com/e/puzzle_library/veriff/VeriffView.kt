package com.library.veriff

interface VeriffView {
    fun startVeriff(sessionToken : String,url : String)
    fun error(error : String)
    fun successVeriff(sessionToken: String)
}