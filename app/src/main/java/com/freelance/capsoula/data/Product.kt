package com.freelance.capsoula.data

import com.freelance.capsoula.R
import com.freelance.capsoula.utils.Domain

class Product {

    var productId = 0
    var productName = ""
    var productDesc = ""
    var imageLink = ""
    var price = 0.0

    fun getPriceText(): String {
        return price.toString() + " " + Domain.application.getString(R.string.rsd)
    }
}