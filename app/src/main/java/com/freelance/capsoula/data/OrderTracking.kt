package com.freelance.capsoula.data

import com.freelance.capsoula.R
import com.freelance.capsoula.utils.DateUtils
import com.freelance.capsoula.utils.Domain

class OrderTracking {

    var orderStatusId: OrderStatus? = null
    var orderStatusDesc = ""
    var operationDate = ""

    fun getOrderStatus(): String {
        return when {
            orderStatusId?.value == OrderStatus.PENDING.value -> Domain.application.getString(R.string.order_is) +
                    " " + Domain.application.getString(R.string.pending)
            orderStatusId?.value == OrderStatus.CANCELLED.value -> Domain.application.getString(R.string.order_is) +
                    " " + Domain.application.getString(R.string.cancelled)
            orderStatusId?.value == OrderStatus.REJECTED.value -> Domain.application.getString(R.string.order_is) +
                    " " + Domain.application.getString(R.string.rejected)
            orderStatusId?.value == OrderStatus.APPROVED.value -> Domain.application.getString(R.string.order_is) +
                    " " + Domain.application.getString(R.string.approved)
            orderStatusId?.value == OrderStatus.SHIPPED.value -> Domain.application.getString(R.string.order_is) +
                    " " + Domain.application.getString(R.string.shipped)
            orderStatusId?.value == OrderStatus.DELIVERED.value -> Domain.application.getString(R.string.order_is) +
                    " " + Domain.application.getString(R.string.delivered)
            else -> ""
        }
    }

    fun getEstimatedTime():String{
        val text = DateUtils.reformatOrderDate(this.operationDate)
        return DateUtils.getEstimatedOrderTime(text)
    }

    fun getEstimatedDate():String{
        val text = DateUtils.reformatOrderDate(this.operationDate)
        return DateUtils.getEstimatedOrderDate(text)
    }
}