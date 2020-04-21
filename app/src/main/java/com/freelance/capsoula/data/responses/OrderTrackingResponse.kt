package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Order
import com.freelance.capsoula.data.OrderTracking
import com.google.gson.annotations.SerializedName

class OrderTrackingResponse {

    @SerializedName("list")
    var orderTrackingList: ArrayList<OrderTracking>? = null
}