package com.e.puzzle_library.onboarding

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.puzzle_library.Constants
import com.e.puzzle_library.R
import com.e.puzzle_library.VeriffActivity
import com.e.puzzle_library.info.InfoFragment
import com.library.network.model.businessName.BusinessNameModel
import com.library.network.model.workerInfo.WorkerInfoModel
import com.library.network.repository.PuzzlRepository
import com.library.network.repository.VeriffRepository
import com.library.onboarding.OnBoardingPresenter
import com.library.onboarding.OnBoardingView
import com.library.singletons.PuzzleSingleton
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import javax.inject.Inject


class OnBoardingFragment : Fragment(),OnBoardingView {

    @Inject lateinit var puzzlRepository: PuzzlRepository
    @Inject lateinit var sessionRepository: VeriffRepository

    lateinit var presenter : OnBoardingPresenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        presenter = OnBoardingPresenter(puzzlRepository,this)


        terms.movementMethod = LinkMovementMethod.getInstance()

        close_app.setOnClickListener {(activity as VeriffActivity).finishSetResult() }

        start.setOnClickListener { (activity as VeriffActivity).replaceFragment(InfoFragment(),true) }

        presenter.getUserInfo(Constants.PUZZL_COMPANY_ID)
        presenter.getWorkerInfo(
            Constants.PUZZL_COMPANY_ID,
            Constants.PUZZL_WORKER_ID
        )

    }

    override fun saveBusinessName(businessName: BusinessNameModel) {
        PuzzleSingleton.businessModel = businessName
    }

    override fun saveWorkerInfo(workerInfoModel: WorkerInfoModel) {
        PuzzleSingleton.workerInfoModel = workerInfoModel
    }

}
