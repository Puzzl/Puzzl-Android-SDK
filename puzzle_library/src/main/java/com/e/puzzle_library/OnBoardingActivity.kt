package com.library.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.e.puzzle_library.Constants
import com.e.puzzle_library.R
import com.library.info.InfoActivity
import com.library.network.model.businessName.BusinessNameModel
import com.library.network.model.workerInfo.WorkerInfoModel
import com.library.network.repository.PuzzlRepository
import com.library.network.repository.VeriffRepository
import com.library.singletons.PuzzleSingleton
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.onboarding_layout.*
import javax.inject.Inject


class OnBoardingActivity : Activity(),OnBoardingView {

    @Inject lateinit var puzzlRepository: PuzzlRepository
    @Inject lateinit var sessionRepository: VeriffRepository

    lateinit var presenter : OnBoardingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.onboarding_layout)
        presenter = OnBoardingPresenter(puzzlRepository,this)


        terms.movementMethod = LinkMovementMethod.getInstance()

        close_app.setOnClickListener { finishAffinity() }
        start.setOnClickListener { startActivity(Intent(this, InfoActivity::class.java)) }

        presenter.getUserInfo(Constants.PUZZL_COMPANY_ID)
        presenter.getWorkerInfo(Constants.PUZZL_COMPANY_ID,Constants.PUZZL_WORKER_ID)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroyPresenter()
    }

    override fun saveBusinessName(businessName: BusinessNameModel) {
        PuzzleSingleton.businessModel = businessName
    }

    override fun saveWorkerInfo(workerInfoModel: WorkerInfoModel) {
        PuzzleSingleton.workerInfoModel = workerInfoModel
    }
}
