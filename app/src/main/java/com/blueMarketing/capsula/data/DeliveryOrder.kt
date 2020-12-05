package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.utils.Domain
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.math.round

class DeliveryOrder {
    @SerializedName("orderId")
    @Expose
    var orderId = 0

    @SerializedName("orderCode")
    @Expose
    var orderCode: String? = null

    @SerializedName("customerName")
    @Expose
    var customerName: String? = null

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null

    @SerializedName("customerAddress")
    @Expose
    var customerAddress: String? = null

    @SerializedName("customerLat")
    @Expose
    var customerLat = 0.0

    @SerializedName("customerLong")
    @Expose
    var customerLong = 0.0

    @SerializedName("storeAddress")
    @Expose
    var storeAddress: String? = null

    @SerializedName("storeLat")
    @Expose
    var storeLat = 0.0

    @SerializedName("storeLong")
    @Expose
    var storeLong = 0.0

    @SerializedName("totalPrice")
    @Expose
    var totalPrice = 0.0

    @SerializedName("paymentMethodId")
    @Expose
    var paymentMethodId: PaymentMethodOption? = null

    @SerializedName("itemsCost")
    @Expose
    var itemsCost = 0.0

    @SerializedName("vatCost")
    @Expose
    var vatCost = 0.0

    @SerializedName("finalTotalCost")
    @Expose
    var finalTotalCost = 0.0

    @SerializedName("statusId")
    @Expose
    var statusId: OrderStatus? = null

    @SerializedName("orderStatusDesc")
    @Expose
    var orderStatusDesc = ""

    var products: ArrayList<Product>? = null

    var deliveryCost = 0.0

    fun hasProducts(): Boolean {
        return !products.isNullOrEmpty()
    }

    fun getPaymentMethod(): String {
        return when (paymentMethodId?.value) {
            PaymentMethodOption.CASH.value -> Domain.application.getString(
                R.string.cash_on_delivery
            )
            PaymentMethodOption.STC_PAY.value -> Domain.application.getString(
                R.string.stc_pay
            )
            PaymentMethodOption.MADA.value -> Domain.application.getString(R.string.mada)
            PaymentMethodOption.CREDIT_CARD.value -> Domain.application.getString(
                R.string.credit_card
            )
            PaymentMethodOption.GOOGLE_PAY.value -> Domain.application.getString(
                R.string.google_pay
            )
            else -> ""
        }
    }

    fun getFinalCost(): String {
        return "" + round(finalTotalCost * 100) / 100
    }

    fun getItemsCostText(): String {
        return "" + round(itemsCost * 100) / 100
    }

    fun getVatCostText(): String {
        return "" + round(vatCost * 100) / 100
    }

    fun getDeliveryCostText(): String {
        return "" + round(deliveryCost * 100) / 100
    }

    fun getNextOrderStatus(): String {
        return when (statusId?.value) {
            OrderStatus.ACCEPTED.value -> {
                Domain.application.getString(R.string.start_delivery)
            }
            OrderStatus.ON_WAY.value -> {
                Domain.application.getString(R.string.reached_store)
            }
            OrderStatus.REACHED_STORE.value -> {
                Domain.application.getString(R.string.shipped)
            }
            OrderStatus.SHIPPED.value -> {
                Domain.application.getString(R.string.delivered_delivery)
            }
            OrderStatus.DELIVERED.value -> {
                Domain.application.getString(R.string.finish_delivery)
            }
            else -> ""
        }
    }

    fun getNextDeliveryAction(): Int {
        return when (statusId?.value) {
            OrderStatus.ACCEPTED.value -> {
                OrderStatus.ON_WAY.value
            }
            OrderStatus.ON_WAY.value -> {
                OrderStatus.REACHED_STORE.value
            }
            OrderStatus.REACHED_STORE.value -> {
                OrderStatus.SHIPPED.value
            }
            OrderStatus.SHIPPED.value -> {
                OrderStatus.DELIVERED.value
            }
            else -> -1
        }
    }

    fun storeLocationIsEnabled(): Boolean {
        return when (statusId?.value) {
            OrderStatus.ON_WAY.value, OrderStatus.REACHED_STORE.value, OrderStatus.SHIPPED.value,
            OrderStatus.DELIVERED.value -> {
                true
            }
            else -> false
        }
    }

    fun clientLocationIsEnabled(): Boolean {
        return when (statusId?.value) {
            OrderStatus.ON_WAY.value, OrderStatus.REACHED_STORE.value -> {
                false
            }
            OrderStatus.SHIPPED.value, OrderStatus.DELIVERED.value -> {
                true
            }
            else -> false
        }
    }

}