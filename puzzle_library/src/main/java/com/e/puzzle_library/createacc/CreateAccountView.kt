package com.library.createacc

import com.e.puzzle_library.network.model.SignW2Response
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody

interface CreateAccountView{
    fun showEmptyFields(field : TextInputEditText)
    fun confirmPasswordError()
    fun showHellosign()
}