package com.blueMarketing.capsula.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import androidx.core.app.NotificationCompat
import com.blueMarketing.capsula.App
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.notificationReceiver.NotificationReceiver
import com.blueMarketing.capsula.ui.home.HomeActivity
import com.blueMarketing.capsula.ui.orderTracking.OrderTrackingActivity
import com.blueMarketing.capsula.ui.productDetails.ProductDetailsActivity
import com.blueMarketing.capsula.ui.splash.SplashActivity
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Constants.ACCEPT_ACTION
import com.blueMarketing.capsula.utils.Constants.CUSTOMER_GENERAL
import com.blueMarketing.capsula.utils.Constants.DELIVERY_GENERAL
import com.blueMarketing.capsula.utils.Constants.EXTRA_NOTIFICATION_ID
import com.blueMarketing.capsula.utils.Constants.EXTRA_ORDER_ID
import com.blueMarketing.capsula.utils.Constants.EXTRA_PRODUCT
import com.blueMarketing.capsula.utils.Constants.FROM_NOTIFICATIONS
import com.blueMarketing.capsula.utils.Constants.FROM_WHERE
import com.blueMarketing.capsula.utils.Constants.GENERAL_ALL
import com.blueMarketing.capsula.utils.Constants.NEW_CUSTOMER_OFFER
import com.blueMarketing.capsula.utils.Constants.NEW_DELIVERY_ORDER
import com.blueMarketing.capsula.utils.Constants.NEW_STORE
import com.blueMarketing.capsula.utils.Constants.NOTIFICATIONS_IS_ENABLED
import com.blueMarketing.capsula.utils.Constants.ORDER_TRACKING
import com.blueMarketing.capsula.utils.Constants.REJECT_ACTION
import com.blueMarketing.capsula.utils.preferencesGateway
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.intercom.android.sdk.push.IntercomPushClient
import java.util.*


@SuppressLint("Registered")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val NOTIFICATION_CHANNEL_ID = "confirm_channel_id"
    private val intercomPushClient = IntercomPushClient()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (intercomPushClient.isIntercomPush(remoteMessage.data) &&
            UserDataSource.getUser() != null &&
            (preferencesGateway.load(NOTIFICATIONS_IS_ENABLED, true))
        ) {
            popUpNotification(remoteMessage.data)
        } else {
            val type = remoteMessage.data["type"]?.toInt()
            if ((isCustomerNotification(type) && UserDataSource.getUser() != null &&
                        preferencesGateway.load(NOTIFICATIONS_IS_ENABLED, true)) ||
                (isDeliveryNotification(type) && UserDataSource.getDeliveryUser() != null)
            )
                popUpNotification(remoteMessage.data)
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        preferencesGateway.save(Constants.FCM_TOKEN, s)
        intercomPushClient.sendTokenToIntercom(application, s)
    }

    private fun isCustomerNotification(type: Int?): Boolean {
        if (type == CUSTOMER_GENERAL || type == NEW_STORE || type == NEW_CUSTOMER_OFFER ||
            type == ORDER_TRACKING || type == GENERAL_ALL
        )
            return true
        return false
    }

    private fun isDeliveryNotification(type: Int?): Boolean {
        if (type == DELIVERY_GENERAL || type == NEW_DELIVERY_ORDER || type == GENERAL_ALL)
            return true
        return false
    }

    private fun popUpNotification(data: Map<String, String>?) {
        if (data != null) {

            if (intercomPushClient.isIntercomPush(data)) {
                intercomPushClient.handlePush(application, data)
            } else {
                val type = data["type"]?.toInt()
                val image = data["image"]
                val title = data["title"]
                val body = data["body"]
                val orderId = data["orderId"]?.toInt()
                val product = data["product"]
                var acceptActionText = ""
                var rejectActionText = ""

                if (preferencesGateway.load(Constants.LANGUAGE, "en")
                        .contentEquals("en")
                ) {
                    acceptActionText = "Accept"
                    rejectActionText = "Reject"
                } else {
                    acceptActionText = "قبول"
                    rejectActionText = "رفض"
                }


                val random = Random()
                val notificationId = random.nextInt(9999 - 1000) + 1000
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                var builder: NotificationCompat.Builder? = null

                if (type != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val notificationChannel = NotificationChannel(
                            NOTIFICATION_CHANNEL_ID, "My Notifications",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        notificationChannel.description = "Channel description"
                        notificationManager.createNotificationChannel(notificationChannel)
                    }

                    builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText(body)
                        )
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                        .setAutoCancel(true)

                    if (type == NEW_DELIVERY_ORDER) {

                        val acceptIntent = Intent(
                            this,
                            NotificationReceiver::class.java
                        ).apply {
                            action = ACCEPT_ACTION
                            putExtra(EXTRA_ORDER_ID, orderId)
                            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                        }

                        val rejectIntent = Intent(
                            this,
                            NotificationReceiver::class.java
                        ).apply {
                            action = REJECT_ACTION
                            putExtra(EXTRA_ORDER_ID, orderId)
                            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                        }

                        val acceptPendingIntent = PendingIntent.getBroadcast(
                            this,
                            12,
                            acceptIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val rejectPendingIntent = PendingIntent.getBroadcast(
                            this,
                            123,
                            rejectIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        builder?.addAction(
                            R.mipmap.ic_launcher, acceptActionText,
                            acceptPendingIntent
                        )

                        builder?.addAction(
                            R.mipmap.ic_launcher, rejectActionText,
                            rejectPendingIntent
                        )

                        try {
                            App.ringtone.play()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        val resultPendingIntent = PendingIntent.getActivity(
                            this,
                            0,
                            openDesiredScreen(type, orderId, product),
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        builder.setContentIntent(resultPendingIntent)
                        val alarmSound =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        builder.setSound(alarmSound)
                    }

                    notificationManager.notify(notificationId, builder.build())

                }
            }
        }
    }

    private fun openDesiredScreen(type: Int?, orderId: Int?, product: String?): Intent? {
        var intent: Intent? = null
        when (type) {
            CUSTOMER_GENERAL, DELIVERY_GENERAL, GENERAL_ALL -> {
                intent = Intent(this, SplashActivity::class.java)
            }
            NEW_STORE -> {
                intent = Intent(this, HomeActivity::class.java)
            }
            NEW_CUSTOMER_OFFER -> {
                intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra(EXTRA_PRODUCT, product)
                intent.putExtra(FROM_WHERE, FROM_NOTIFICATIONS)
            }
            ORDER_TRACKING -> {
                intent = Intent(this, OrderTrackingActivity::class.java)
                intent.putExtra(EXTRA_ORDER_ID, orderId)
            }
        }
        return intent
    }
}