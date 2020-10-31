package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.Order
import com.google.gson.annotations.SerializedName

class OrdersResponse {

    @SerializedName("list")
    var orderList: ArrayList<Order>? = null

    var count = 0
}