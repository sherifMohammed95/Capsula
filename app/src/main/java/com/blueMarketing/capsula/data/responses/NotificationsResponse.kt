package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.Notification
import com.blueMarketing.capsula.data.Store
import com.google.gson.annotations.SerializedName

class NotificationsResponse {

    @SerializedName("list")
    var notificationsList: ArrayList<Notification>? = null

    var count = 0
}