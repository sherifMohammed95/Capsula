package com.blueMarketing.capsula.data.responses

import kotlin.math.round

class PaymentDetailsResponse {

    var vatCost = 0.0
    var deliveryCost = 0.0
    var itemsCost = 0.0
    var finalTotalCost = 0.0

    fun getFinalCost(): String {
        return "" + round(finalTotalCost * 100) / 100
    }

    fun getItemsCostText(): String {
        return "" + itemsCost
    }

    fun getDeliveryCostText(): String {
        return "" + round(deliveryCost * 100) / 100
    }


    fun getVatCostText(): String {
        return "" + round(vatCost * 100) / 100
    }
}