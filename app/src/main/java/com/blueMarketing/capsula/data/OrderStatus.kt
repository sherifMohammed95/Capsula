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
    ACCEPTED(4),
    @SerializedName("5")
    SHIPPED(5),
    @SerializedName("6")
    DELIVERED(6),
    @SerializedName("7")
    ON_WAY(7),
    @SerializedName("8")
    REACHED_STORE(8),
    @SerializedName("9")
    COMPLETED(9),
    @SerializedName("10")
    UNCOMPLETED(10);

    companion object {
        operator fun invoke(rawValue: Int) = values()
            .find { it.value == rawValue }
    }
}