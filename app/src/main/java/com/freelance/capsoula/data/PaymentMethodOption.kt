package com.freelance.capsoula.data

import com.google.gson.annotations.SerializedName

enum class PaymentMethodOption(val value: Int) {

    @SerializedName("1")
    CASH(1),
    @SerializedName("3")
    STC_PAY(3),
    @SerializedName("4")
    CREDIT_CARD(4),
    @SerializedName("5")
    MADA(5),
    @SerializedName("6")
    GOOGLE_PAY(6);

    companion object {
        operator fun invoke(rawValue: Int) = OrderStatus.values()
            .find { it.value == rawValue }
    }
}