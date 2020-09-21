package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.OrderTracking
import com.google.gson.annotations.SerializedName

class OrderTrackingResponse {

    @SerializedName("list")
    var orderTrackingList: ArrayList<OrderTracking>? = null
}