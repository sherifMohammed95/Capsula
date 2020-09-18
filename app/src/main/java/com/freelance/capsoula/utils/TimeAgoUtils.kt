package com.freelance.capsoula.utils

import android.content.Context
import com.freelance.capsoula.R

class TimeAgoUtils {
    /*
     * Copyright 2012 Google Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

    /*
     * Copyright 2012 Google Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS


    fun getTimeAgo(time: Long, ctx: Context): String? {
        var time = time
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        // TODO: localize
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            ctx.getString(R.string.just_now)
        } else if (diff < 2 * MINUTE_MILLIS) {
            ctx.getString(R.string.minute_ago)
        } else if (diff < 50 * MINUTE_MILLIS) {
            if (preferencesGateway.load(Constants.LANGUAGE, "en")
                    .contentEquals("ar")
            )
                ctx.getString(R.string.ago) + " " + diff / MINUTE_MILLIS + " " +
                        ctx.getString(R.string.minutes_ago)
            else (diff / MINUTE_MILLIS).toString() +
                    " " + ctx.getString(R.string.minutes_ago)
        } else if (diff < 90 * MINUTE_MILLIS) {
            ctx.getString(R.string.hour_ago)
        } else if (diff < 24 * HOUR_MILLIS) {
            if (preferencesGateway.load(Constants.LANGUAGE, "en")
                    .contentEquals("ar")
            )
                ctx.getString(R.string.ago) + " " + diff / HOUR_MILLIS + " " +
                        ctx.getString(R.string.hours_ago)
            else (diff / HOUR_MILLIS).toString() +
                    " " + ctx.getString(R.string.hours_ago)
        } else if (diff < 48 * HOUR_MILLIS) {
            ctx.getString(R.string.yesterday)
        } else {
            DateUtils.toString(DateUtils.formatNotificationDate(time)!!)
        }
    }

}