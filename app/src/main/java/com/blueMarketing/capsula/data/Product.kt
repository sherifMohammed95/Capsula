package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Domain
import kotlin.math.round

class Product {

    var mainId = 0
    var productName: String? = ""
    var productDesc: String? = ""
    var imagePath: String? = ""
    var price = 0.0
    var storeName: String? = ""
    var isTreatment = false
    var offerDesc: String? = ""
    var offerType: Int? = -1
    var priceInOffer: Double? = null
    var quantity = 1
    var vat: Int? = 0
    var offerExpirationDate: String? = null


    fun getQuantityText(): String {
        return quantity.toString()
    }

    fun getPriceText(): String {
        val mPrice = round(price * 100) / 100
        return Domain.application.getString(R.string.rsd) + " " + mPrice.toString()
    }

    fun getVatText(): String {
        return Domain.application.getString(R.string.vat) + " " + vat.toString() + "%"
    }

    fun getOrderDetailsQuantityText(): String {
        return quantity.toString() + " " + Domain.application.getString(R.string.pieces)
    }

    fun getWasPriceText(): String {
        val mPrice = round(price * 100) / 100
        return Domain.application.getString(R.string.was) + " " +
                Domain.application.getString(R.string.rsd) + " " + mPrice.toString()
    }

    fun getPriceDetailsText(): String {
        val mPrice = round(price * 100) / 100
        var mOfferPrice = 0.0
        if (priceInOffer != null)
            mOfferPrice = round(priceInOffer!! * 100) / 100
        return if (offerType == Constants.DISCOUNT_OFFER)
            Domain.application.getString(R.string.rsd) + " " + mOfferPrice.toString()
        else
            Domain.application.getString(R.string.rsd) + " " + mPrice.toString()
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