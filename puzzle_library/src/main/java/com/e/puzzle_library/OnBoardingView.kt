package com.library.onboarding

import com.library.network.model.businessName.BusinessNameModel
import com.library.network.model.workerInfo.WorkerInfoModel

interface OnBoardingView {
    fun saveBusinessName(businessName : BusinessNameModel)
    fun saveWorkerInfo (workerInfoModel: WorkerInfoModel)
}