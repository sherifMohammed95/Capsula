package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.preferencesGateway

class MoreItem(
    var icon: Int = 0,
    var text: String = "",
    var hasNotificationSwitch: Boolean = false
) {

    fun setNotificationValue(): Boolean {
        return preferencesGateway.load(
            Constants.NOTIFICATIONS_IS_ENABLED,
            true
        )
    }

}