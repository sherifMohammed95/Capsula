package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Category
import com.freelance.capsoula.data.DeliveryOrder
import com.google.gson.annotations.SerializedName

class DeliveryOrdersResponse {

    @SerializedName("list")
    var ordersList: ArrayList<DeliveryOrder>? = null

}