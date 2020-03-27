package com.freelance.capsoula.data

import com.freelance.capsoula.R
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Domain

class Product {

    var mainId = 0
    var productName: String? = ""
    var productDesc: String? = ""
    var imagePath: String? = ""
    var price = 0
    var storeName: String? = ""
    var isTreatment = false
    var offerDesc: String? = ""
    var offerType: Int? = -1
    var priceInOffer: Int? = null
    var quantity = 1


    fun getQuantityText(): String {
        return quantity.toString()
    }

    fun getPriceText(): String {
        return Domain.application.getString(R.string.rsd) + " " + price.toString()
    }

    fun getWasPriceText(): String {
        return Domain.application.getString(R.string.was) + " " +
                Domain.application.getString(R.string.rsd) + " " + price.toString()
    }

    fun getPriceDetailsText(): String {
        return if (offerType == Constants.DISCOUNT_OFFER)
            Domain.application.getString(R.string.rsd) + " " + priceInOffer.toString()
        else
            Domain.application.getString(R.string.rsd) + " " + price.toString()
    }

    fun getOfferTag(): String {
        return when (offerType) {
            Constants.FREE_DELIVERY_OFFER -> Domain.application.getString(R.string.free_delivery)
            Constants.PRODUCT_OFFER -> Domain.application.getString(R.string.offer)
            Constants.DISCOUNT_OFFER -> Domain.application.getString(R.string.discount)
            else -> ""
        }
    }
}