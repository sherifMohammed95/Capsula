package com.freelance.capsoula.data

import com.freelance.capsoula.utils.DateUtils
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.TimeAgoUtils

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
        return TimeAgoUtils().getTimeAgo(
            DateUtils.getMilliSecondsFromDate(date!!),
            Domain.application.applicationContext
        )
    }
}