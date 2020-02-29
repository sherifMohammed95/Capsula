package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Product
import com.google.gson.annotations.SerializedName

class ProductsResponse {
    @SerializedName("list")
    var productsList: ArrayList<Product>? = null

    var count = 0
}