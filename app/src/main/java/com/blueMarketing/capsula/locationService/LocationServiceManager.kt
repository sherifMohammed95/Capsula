package com.blueMarketing.capsula.locationService

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

class LocationServiceManager {

    fun startBackgroundService(context: Context) {
        val myIntent = Intent(context, LocationService::class.java)
        val pendingIntent = PendingIntent.getService(context, 1001, myIntent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, 1) // first time
        val frequency =  60 * 1000.toLong() // in ms
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, frequency, pendingIntent
        )
    }
}