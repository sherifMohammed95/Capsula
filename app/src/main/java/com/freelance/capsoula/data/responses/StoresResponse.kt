package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Store
import com.google.gson.annotations.SerializedName

class StoresResponse {

    @SerializedName("list")
    var storesList: ArrayList<Store>? = null

    var count = 0
}