package com.freelance.capsoula.data

import com.freelance.capsoula.R
import com.freelance.capsoula.utils.Domain
import kotlin.math.round

class Wallet {
    var countOfCompletedOrders = 0.0

    var totalDeliveryCostCashOrder = 0.0

    var totalDeliveryCostOnlineOrder = 0.0

    var totalDeliveryCost = 0.0

    var discount = 0.0

    var totalCreditCustomerOrderAmountCash = 0.0

    var bonuses = 0.0

    var compensations = 0.0

    var balance = 0.0

    var payments = 0.0

    var collection = 0.0

    var endingBalance = 0.0

    fun getFinalBalanceText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(endingBalance * 100)) / 100
    }

    fun getCollectionText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(collection * 100)) / 100
    }

    fun getPaymentsText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(payments * 100)) / 100
    }

    fun getCompensationsText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(compensations * 100)) / 100
    }

    fun getBalanceText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(balance * 100)) / 100
    }

    fun getBonusesText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(bonuses * 100)) / 100
    }

    fun getCashOrdersCostText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(totalCreditCustomerOrderAmountCash * 100)) / 100
    }

    fun getDiscountText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(discount * 100)) / 100
    }

    fun getTotalDeliveryCostText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(totalDeliveryCost * 100)) / 100
    }

    fun getOnlineDeliveryCostText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(totalDeliveryCostOnlineOrder * 100)) / 100
    }

    fun getCashDeliveryCostText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(totalDeliveryCostCashOrder * 100)) / 100
    }

    fun getCompletedOrdersCountText(): String {
        return Domain.application.getString(R.string.rsd) + " " +
                (round(countOfCompletedOrders * 100)) / 100
    }

}