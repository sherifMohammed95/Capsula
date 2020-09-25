package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.utils.DateUtils
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.TimeAgoUtils

class Notification {
    var title: String? = ""
    var body: String? = ""
    var image: String? = ""
    var date: String? = ""
    var type: Int? = -1
    var orderId: Int? = -1
    var product: Product? = null

    fun hasImage(): Boolean {
        return !image.isNullOrEmpty()
    }

    fun formatDate(): String? {
        return DateUtils.reformatNotificationDate(date!!)
    }
}