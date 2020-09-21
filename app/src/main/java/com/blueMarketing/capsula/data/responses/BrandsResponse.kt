package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.Brand
import com.google.gson.annotations.SerializedName

class BrandsResponse {

    @SerializedName("list")
    var brandsList: ArrayList<Brand>? = null

    var count = 0

}