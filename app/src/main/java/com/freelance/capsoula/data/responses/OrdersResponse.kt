package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Order
import com.google.gson.annotations.SerializedName

class OrdersResponse {

    @SerializedName("list")
    var orderList: ArrayList<Order>? = null
}