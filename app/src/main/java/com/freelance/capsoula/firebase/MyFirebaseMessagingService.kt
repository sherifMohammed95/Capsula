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
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import com.freelance.capsoula.ui.home.HomeActivity
import com.freelance.capsoula.ui.orderTracking.OrderTrackingActivity
import com.freelance.capsoula.ui.productDetails.ProductDetailsActivity
import com.freelance.capsoula.ui.splash.SplashActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Constants.CUSTOMER_GENERAL
import com.freelance.capsoula.utils.Constants.DELIVERY_GENERAL
import com.freelance.capsoula.utils.Constants.EXTRA_ORDER_ID
import com.freelance.capsoula.utils.Constants.EXTRA_PRODUCT
import com.freelance.capsoula.utils.Constants.FROM_NOTIFICATIONS
import com.freelance.capsoula.utils.Constants.FROM_WHERE
import com.freelance.capsoula.utils.Constants.GENERAL_ALL
import com.freelance.capsoula.utils.Constants.NEW_CUSTOMER_OFFER
import com.freelance.capsoula.utils.Constants.NEW_DELIVERY_ORDER
import com.freelance.capsoula.utils.Constants.NEW_STORE
import com.freelance.capsoula.utils.Constants.NOTIFICATIONS_IS_ENABLED
import com.freelance.capsoula.utils.Constants.ORDER_TRACKING
import com.freelance.capsoula.utils.preferencesGateway
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
        if (preferencesGateway.load(NOTIFICATIONS_IS_ENABLED, true)) {
            val type = remoteMessage.data["type"]?.toInt()
            if ((isCustomerNotification(type) && UserDataSource.getUser() != null) ||
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
                val random = Random()
                val notificationId = random.nextInt(9999 - 1000) + 1000
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                var builder: NotificationCompat.Builder? = null

                if (type != null) {

                    when (type) {
                        CUSTOMER_GENERAL -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                        DELIVERY_GENERAL -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                        GENERAL_ALL -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                        NEW_STORE -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                        NEW_CUSTOMER_OFFER -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                        NEW_DELIVERY_ORDER -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                        ORDER_TRACKING -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel = NotificationChannel(
                                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                notificationChannel.description = "Channel description"
                                notificationManager.createNotificationChannel(notificationChannel)
                            }

                            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                        }
                    }
                }

                val resultPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    openDesiredScreen(type, orderId, product),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                builder?.setContentIntent(resultPendingIntent)
                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                builder?.setSound(alarmSound)
                builder?.setAutoCancel(true)
                notificationManager.notify(notificationId, builder?.build())
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
            NEW_DELIVERY_ORDER -> {
                intent = Intent(this, DeliveryHomeActivity::class.java)
            }
            ORDER_TRACKING -> {
                intent = Intent(this, OrderTrackingActivity::class.java)
                intent.putExtra(EXTRA_ORDER_ID, orderId)
            }
        }
        return intent
    }
}