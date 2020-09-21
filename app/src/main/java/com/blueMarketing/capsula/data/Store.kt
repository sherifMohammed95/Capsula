package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.utils.Domain
import kotlin.math.round

class Store {

    var storeId = 0

    var storeName = ""

    var imageLink = ""

    var distance = 0.0

    fun getDistanceText(): String {
        val dist = (round(distance * 100)) / 100
        return Domain.application.getString(R.string.store_distance) + " " +
                dist.toString() + " " + Domain.application.getString(R.string.km)
    }
}