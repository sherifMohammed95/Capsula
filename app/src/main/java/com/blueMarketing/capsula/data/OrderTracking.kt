package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.utils.DateUtils
import com.blueMarketing.capsula.utils.Domain

class OrderTracking {

    var orderStatusId: OrderStatus? = null
    var orderStatusDesc = ""
    var operationDate = ""

    fun getEstimatedTime():String{
        val text = DateUtils.reformatOrderDate(this.operationDate)
        return DateUtils.getEstimatedOrderTime(text)
    }

    fun getEstimatedDate():String{
        val text = DateUtils.reformatOrderDate(this.operationDate)
        return DateUtils.getEstimatedOrderDate(text)
    }
}