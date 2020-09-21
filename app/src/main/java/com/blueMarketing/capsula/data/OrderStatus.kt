package com.blueMarketing.capsula.data

import com.google.gson.annotations.SerializedName

enum class OrderStatus(val value: Int) {
    @SerializedName("1")
    PENDING(1),
    @SerializedName("2")
    CANCELLED(2),
    @SerializedName("3")
    REJECTED(3),
    @SerializedName("4")
    APPROVED(4),
    @SerializedName("5")
    SHIPPED(5),
    @SerializedName("6")
    DELIVERED(6);

    companion object {
        operator fun invoke(rawValue: Int) = values()
            .find { it.value == rawValue }
    }
}