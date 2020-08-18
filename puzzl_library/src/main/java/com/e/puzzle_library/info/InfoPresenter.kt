package com.library.info

import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_info.*;

class InfoPresenter (private val view : InfoView){
    private var views = mutableListOf<TextInputEditText>()
    fun initViews(firstName : TextInputEditText,lastName : TextInputEditText, phoneNumber: TextInputEditText, mi : TextInputEditText,dob : TextInputEditText,
                  edtSocialFirst : TextInputEditText,edtSocialSecond : TextInputEditText,edtSocialThird : TextInputEditText,
                  address : TextInputEditText, city : TextInputEditText,state : TextInputEditText,zip : TextInputEditText){
        views.add(firstName)
        views.add(lastName)
        views.add(phoneNumber)
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
                if(view.id == 2131230897){
                    continue;
                }
//                println("*************")
//                println(view.id)
//                println("*************")
                else{
                    this.view.showEmptyField(view)
                    return false
                }

            }
        }
        return true
    }
}