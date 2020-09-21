package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.Product
import com.google.gson.annotations.SerializedName

class ProductsResponse {
    @SerializedName("list")
    var productsList: ArrayList<Product>? = null

    var count = 0
}