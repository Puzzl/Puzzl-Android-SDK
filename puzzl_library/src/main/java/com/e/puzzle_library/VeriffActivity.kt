package com.e.puzzl_library

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.e.puzzl_library.onboarding.OnBoardingFragment
import com.library.singletons.PuzzleSingleton
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import javax.xml.transform.Result


class VeriffActivity : AppCompatActivity(),HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSupportActionBar() != null ) getSupportActionBar()?.hide()
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_veriff)
        AndroidInjection.inject(this)



        if (intent.getStringExtra("api_key") != null ) {
            PuzzleSingleton.api_key = intent.getStringExtra("api_key")
        } else throw NullPointerException("Puzzle Api Key is null")
        if (intent.getStringExtra("employeeID") != null ) {
            Constants.PUZZL_WORKER_ID = intent.getStringExtra("employeeID")
        } else throw NullPointerException("Employee ID is null")
        if (intent.getStringExtra("companyID") != null ) {
            Constants.PUZZL_COMPANY_ID = intent.getStringExtra("companyID")
        } else throw NullPointerException("Company ID is null")

        replaceFragment(OnBoardingFragment(),true)

    }

    fun finishSetResult (){
        val intent = Intent()
        intent.putExtra("result","User cancelled the session.")
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val tag = fragment.javaClass.simpleName
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commitAllowingStateLoss()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun finishSuccessScreen(){
        val intent = Intent()
        intent.putExtra("result","success")
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

}
