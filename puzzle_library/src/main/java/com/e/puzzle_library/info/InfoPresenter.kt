package com.library.info

import com.google.android.material.textfield.TextInputEditText

class InfoPresenter (private val view : InfoView){
    private var views = mutableListOf<TextInputEditText>()
    fun initViews(firstName : TextInputEditText,lastName : TextInputEditText,mi : TextInputEditText,dob : TextInputEditText,
                  edtSocialFirst : TextInputEditText,edtSocialSecond : TextInputEditText,edtSocialThird : TextInputEditText,
                  address : TextInputEditText, city : TextInputEditText,state : TextInputEditText,zip : TextInputEditText){
        views.add(firstName)
        views.add(lastName)
        views.add(mi)
        views.add(dob)
        views.add(edtSocialFirst)
        views.add(edtSocialSecond)
        views.add(edtSocialThird)
        views.add(address)
        views.add(city)
        views.add(state)
        views.add(zip)
    }

    fun checkEmptyFields() : Boolean{
        for (view in views){
            if (view.text.toString().isEmpty()){
                this.view.showEmptyField(view)
                return false }
        }
        return true
    }
}