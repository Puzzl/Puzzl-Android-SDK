package com.e.puzzle_library.info

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker

import com.e.puzzle_library.R
import com.e.puzzle_library.VeriffActivity
import com.e.puzzle_library.createacc.CreateAccountFragment
import com.google.android.material.textfield.TextInputEditText
import com.library.info.InfoPresenter
import com.library.info.InfoView
import com.library.singletons.PuzzleSingleton
import com.library.singletons.UserSingleton
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.close_app
import kotlinx.android.synthetic.main.fragment_info.create_account
import java.text.SimpleDateFormat
import java.util.*


class InfoFragment : Fragment(), InfoView, DatePickerDialog.OnDateSetListener {
    lateinit var presenter: InfoPresenter
    private val inputDate = SimpleDateFormat("d/M/yyyy")
    val outputDate = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = InfoPresenter(this)

        close_app.setOnClickListener { (activity as VeriffActivity).finishSetResult() }

        create_account.setOnClickListener {

            if (presenter.checkEmptyFields()){
                setUserInfo()
                (activity as VeriffActivity).replaceFragment(CreateAccountFragment(),true)
            }
        }

        presenter.initViews(edt_first_name,edt_last_name,edt_phone_number, edt_mi,edt_date,edt_social_first,edt_social_second,
            edt_social_third,edt_address,edt_city,edt_state,edt_zip)


        edt_date.setOnClickListener { createDateDialog() }
        edt_first_name.setText(PuzzleSingleton.workerInfoModel.first_name)
        edt_last_name.setText(PuzzleSingleton.workerInfoModel.last_name)


    }

    override fun showEmptyField(field: TextInputEditText) {
        field.setError("Empty fields")
    }

    private fun setUserInfo(){
        UserSingleton.firstName = edt_first_name.text.toString()
        UserSingleton.lastName = edt_last_name.text.toString()
        UserSingleton.phoneNumber = edt_phone_number.text.toString()
        UserSingleton.middle_initial = edt_mi.text.toString()
        UserSingleton.dob = edt_date.text.toString()
        UserSingleton.last4_ssn = edt_social_third.text.toString()
        UserSingleton.ssn = edt_social_first.text.toString() + edt_social_second.text.toString() + edt_social_third.text.toString()
        UserSingleton.address = edt_address.text.toString()
        UserSingleton.city = edt_city.text.toString()
        UserSingleton.state = edt_state.text.toString()
        UserSingleton.zip = edt_zip.text.toString().toInt()
    }

    private fun createDateDialog(){
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dateDialog = context?.let { DatePickerDialog(it,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,this,year,month,day) }
        dateDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dateDialog?.show()
        dateDialog?.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setBackgroundColor(Color.TRANSPARENT)
        dateDialog?.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        edt_date.setText(outputDate.format(inputDate.parse("$dayOfMonth/${month + 1}/$year")))
    }

}
