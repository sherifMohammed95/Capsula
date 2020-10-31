package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.utils.Domain
import kotlin.math.round

class PaymentDetailsResponse {

    var vatCost = 0.0
    var deliveryCost = 0.0
    var itemsCost = 0.0
    var finalTotalCost = 0.0
    var vatPercentage = 0.0

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


    fun getVatPercentageText(): String {
        return Domain.application.getString(R.string.order_price) + " " +
                round(vatPercentage * 100) / 100 + "% " +
                Domain.application.getString(R.string.and) + " " +
                round(vatCost * 100) / 100 + " " + Domain.application.getString(R.string.rsd)
    }
}