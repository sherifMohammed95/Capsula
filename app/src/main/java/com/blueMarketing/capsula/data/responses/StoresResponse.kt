package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.Store
import com.google.gson.annotations.SerializedName

class StoresResponse {

    @SerializedName("list")
    var storesList: ArrayList<Store>? = null

    var count = 0
}