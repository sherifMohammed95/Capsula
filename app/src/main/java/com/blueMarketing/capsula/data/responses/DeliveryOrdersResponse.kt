package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.DeliveryOrder
import com.google.gson.annotations.SerializedName

class DeliveryOrdersResponse {

    @SerializedName("list")
    var ordersList: ArrayList<DeliveryOrder>? = null

    var count = 0

}