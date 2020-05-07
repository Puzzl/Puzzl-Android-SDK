package com.library.singletons

import com.e.puzzle_library.network.model.SignW2Response
import com.library.network.model.VerificationResponse
import com.library.network.model.businessName.BusinessNameModel
import com.library.network.model.workerInfo.WorkerInfoModel

object PuzzleSingleton {
    var businessModel : BusinessNameModel = BusinessNameModel("")
    var workerInfoModel = WorkerInfoModel("","","","",20,30,40,"")
    var hellosign = SignW2Response("","","","")
    var veriff = VerificationResponse("","","","","")
    var api_key = ""
}