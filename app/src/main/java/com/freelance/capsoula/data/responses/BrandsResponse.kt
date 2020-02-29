package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Brand
import com.google.gson.annotations.SerializedName

class BrandsResponse {

    @SerializedName("list")
    var brandsList: ArrayList<Brand>? = null

    var count = 0

}