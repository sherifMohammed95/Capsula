package com.blueMarketing.capsula.data.requests

import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.preferencesGateway

class RefreshDeviceRequest {

    var token = preferencesGateway.load(Constants.FCM_TOKEN,"")
    var deviceType = 1
    var language = preferencesGateway.load(Constants.LANGUAGE,"en")

}