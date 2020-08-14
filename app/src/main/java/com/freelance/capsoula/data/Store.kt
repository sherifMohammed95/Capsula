package com.freelance.capsoula.data

import com.freelance.capsoula.R
import com.freelance.capsoula.utils.Domain
import kotlin.math.roundToInt

class Store {

    var storeId = 0

    var storeName = ""

    var imageLink = ""

    var distance = 0.0

    fun getDistanceText(): String {
        val dist = (distance * 100).roundToInt() / 100
        return Domain.application.getString(R.string.store_distance) + " " +
                dist.toString() + " " + Domain.application.getString(R.string.km)
    }
}