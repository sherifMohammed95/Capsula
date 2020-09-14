package com.freelance.capsoula.data.requests

import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.preferencesGateway

class RefreshDeviceRequest {

    var token = preferencesGateway.load(Constants.FCM_TOKEN,"")
    var deviceType = 1
    var language = preferencesGateway.load(Constants.LANGUAGE,"en")

}