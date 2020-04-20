package com.freelance.capsoula.data

import com.freelance.capsoula.R
import com.freelance.capsoula.data.OrderStatus.PENDING
import com.freelance.capsoula.data.OrderStatus.CANCELLED
import com.freelance.capsoula.data.OrderStatus.REJECTED
import com.freelance.capsoula.data.OrderStatus.APPROVED
import com.freelance.capsoula.data.OrderStatus.SHIPPED
import com.freelance.capsoula.data.OrderStatus.DELIVERED
import com.freelance.capsoula.data.PaymentMethodOption.CASH
import com.freelance.capsoula.data.PaymentMethodOption.STC_PAY
import com.freelance.capsoula.data.PaymentMethodOption.MADA
import com.freelance.capsoula.data.PaymentMethodOption.GOOGLE_PAY
import com.freelance.capsoula.data.PaymentMethodOption.CREDIT_CARD
import com.freelance.capsoula.utils.DateUtils
import com.freelance.capsoula.utils.Domain

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

    fun getFinalCost(): String {
        return "" + finalTotalCost
    }

    fun getItemsCostText(): String {
        return "" + itemsCost
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
        return when {
            orderStatusId?.value == PENDING.value -> Domain.application.getString(R.string.pending)
            orderStatusId?.value == CANCELLED.value -> Domain.application.getString(R.string.cancelled)
            orderStatusId?.value == REJECTED.value -> Domain.application.getString(R.string.rejected)
            orderStatusId?.value == APPROVED.value -> Domain.application.getString(R.string.approved)
            orderStatusId?.value == SHIPPED.value -> Domain.application.getString(R.string.shipped)
            orderStatusId?.value == DELIVERED.value -> Domain.application.getString(R.string.delivered)
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
        return when {
            paymentMethodId?.value == CASH.value -> Domain.application.getString(R.string.cash_on_delivery)
            orderStatusId?.value == STC_PAY.value -> Domain.application.getString(R.string.stc_pay)
            orderStatusId?.value == MADA.value -> Domain.application.getString(R.string.mada)
            orderStatusId?.value == CREDIT_CARD.value -> Domain.application.getString(R.string.credit_card)
            orderStatusId?.value == GOOGLE_PAY.value -> Domain.application.getString(R.string.google_pay)
            else -> ""
        }
    }


    fun getOrderDateText(): String {
        val text = DateUtils.reformatOrderDate(this.orderDate!!)
        return DateUtils.getOrderDate(text)
    }
}