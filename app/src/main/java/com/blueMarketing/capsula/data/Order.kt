package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.OrderStatus.PENDING
import com.blueMarketing.capsula.data.OrderStatus.CANCELLED
import com.blueMarketing.capsula.data.OrderStatus.REJECTED
import com.blueMarketing.capsula.data.OrderStatus.APPROVED
import com.blueMarketing.capsula.data.OrderStatus.SHIPPED
import com.blueMarketing.capsula.data.OrderStatus.DELIVERED
import com.blueMarketing.capsula.data.PaymentMethodOption.CASH
import com.blueMarketing.capsula.data.PaymentMethodOption.STC_PAY
import com.blueMarketing.capsula.data.PaymentMethodOption.MADA
import com.blueMarketing.capsula.data.PaymentMethodOption.GOOGLE_PAY
import com.blueMarketing.capsula.data.PaymentMethodOption.CREDIT_CARD
import com.blueMarketing.capsula.utils.DateUtils
import com.blueMarketing.capsula.utils.Domain
import kotlin.math.round

class Order {

    var id: Int = 0
    var orderStatusId: OrderStatus? = null
    var orderDate: String? = null
    var totalPrice = 0.0
    var deliveryAddress: String? = null
    var prescriptionImageLink: String? = null
    var insuranceNumberImageLink: String? = null
    var paymentMethodId: PaymentMethodOption? = null
    var itemsCost = 0.0
    var finalTotalCost = 0.0
    var products: ArrayList<Product>? = null
    var orderCode = ""
    var vatCost = 0.0
    var deliveryCost = 0.0

    fun getFinalCost(): String {
        return "" + round(finalTotalCost * 100) / 100
    }

    fun getItemsCostText(): String {
        return "" + round(itemsCost * 100) / 100
    }

    fun getDeliveryCostText(): String {
        return "" + round(deliveryCost * 100) / 100
    }


    fun getVatCostText(): String {
        return "" + round(vatCost * 100) / 100
    }

    fun hasDocument(): Boolean {
        return !prescriptionImageLink.isNullOrEmpty() || !insuranceNumberImageLink.isNullOrEmpty()
    }

    fun hasPrescription(): Boolean {
        return !prescriptionImageLink.isNullOrEmpty()
    }

    fun hasInsuranceNumber(): Boolean {
        return !insuranceNumberImageLink.isNullOrEmpty()
    }

    fun getOrderStatus(): String {
        return when (orderStatusId?.value) {
            PENDING.value -> Domain.application.getString(R.string.pending)
            CANCELLED.value -> Domain.application.getString(R.string.cancelled)
            REJECTED.value -> Domain.application.getString(R.string.rejected)
            APPROVED.value -> Domain.application.getString(R.string.approved)
            SHIPPED.value -> Domain.application.getString(R.string.shipped)
            DELIVERED.value -> Domain.application.getString(R.string.delivered)
            else -> ""
        }
    }

    fun hasAction():Boolean{
        return orderStatusId?.value == PENDING.value || orderStatusId?.value == CANCELLED.value ||
                orderStatusId?.value == REJECTED.value || orderStatusId?.value == DELIVERED.value
    }

    fun hasCancelAction():Boolean{
        return orderStatusId?.value == PENDING.value
    }

    fun getPaymentMethod(): String {
        return when (paymentMethodId?.value) {
            CASH.value -> Domain.application.getString(R.string.cash_on_delivery)
            STC_PAY.value -> Domain.application.getString(R.string.stc_pay)
            MADA.value -> Domain.application.getString(R.string.mada)
            CREDIT_CARD.value -> Domain.application.getString(R.string.credit_card)
            GOOGLE_PAY.value -> Domain.application.getString(R.string.google_pay)
            else -> ""
        }
    }


    fun getOrderDateText(): String {
        val text = DateUtils.reformatOrderDate(this.orderDate!!)
        return DateUtils.getOrderDate(text)
    }
}