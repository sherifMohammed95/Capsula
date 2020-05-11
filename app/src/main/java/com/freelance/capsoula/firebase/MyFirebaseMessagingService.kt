package com.freelance.capsoula.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.freelance.capsoula.data.MessageEvent
import com.freelance.capsoula.ui.splash.SplashActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.preferencesGateway
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.push.IntercomPushClient
import org.greenrobot.eventbus.EventBus
import java.util.*


@SuppressLint("Registered")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val NOTIFICATION_CHANNEL_ID = "confirm_channel_id"
    private val intercomPushClient = IntercomPushClient()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        popUpNotification(remoteMessage.data)
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        preferencesGateway.save(Constants.FCM_TOKEN, s)
        intercomPushClient.sendTokenToIntercom(application, s)

    }

    private fun popUpNotification(data: Map<String, String>?) {
        if (data != null) {

            if (intercomPushClient.isIntercomPush(data)) {
                intercomPushClient.handlePush(application, data)
            }
//            EventBus.getDefault().post(MessageEvent(Constants.NEW_NOTIFICATION))
//            DataUtil.saveData(this, Constants.NEW_NOTIFICATIONS, true)
//            val random = Random()
//            val notificationId = random.nextInt(9999 - 1000) + 1000
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val notificationChannel = NotificationChannel(
//                    NOTIFICATION_CHANNEL_ID, "My Notifications",
//                    NotificationManager.IMPORTANCE_HIGH
//                )
//                notificationChannel.description = "Channel description"
//                notificationManager.createNotificationChannel(notificationChannel)
//            }
//
//            val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
////                .setSmallIcon(com.mattel.mymattel.R.mipmap.ic_launcher)
//                .setContentTitle(data["title"])
//                .setContentText(data["body"])
//                .setAutoCancel(true)
//
//
//            val resultIntent = Intent(this, SplashActivity::class.java)
//
//            val resultPendingIntent = PendingIntent.getActivity(
//                this,
//                0,
//                resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//            builder.setContentIntent(resultPendingIntent)
//            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            builder.setSound(alarmSound)
//            builder.setAutoCancel(true)
//            notificationManager.notify(notificationId, builder.build())

        }
    }
}