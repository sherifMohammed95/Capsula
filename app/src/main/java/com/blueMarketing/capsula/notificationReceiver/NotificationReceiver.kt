package com.blueMarketing.capsula.notificationReceiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blueMarketing.capsula.App
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import com.blueMarketing.capsula.utils.Constants.ACCEPT_ACTION
import com.blueMarketing.capsula.utils.Constants.EXTRA_NOTIFICATION_ID
import com.blueMarketing.capsula.utils.Constants.EXTRA_ORDER_ID
import com.blueMarketing.capsula.utils.Constants.REJECT_ACTION
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationReceiver : BroadcastReceiver() {

    private val repo = OrdersRepository()

    override fun onReceive(context: Context?, intent: Intent?) {
        val orderId = intent?.getIntExtra(EXTRA_ORDER_ID, -1)
        val notificationId = intent?.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId!!)
        if (App.ringtone.isPlaying) App.ringtone.stop()

        when (intent.action) {
            ACCEPT_ACTION -> {
                CoroutineScope(Dispatchers.IO).launch {
                    repo.acceptOrder(orderId!!, Action {
                        val i = Intent(context, DeliveryHomeActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(i)
                    })
                }
            }
            REJECT_ACTION -> {
                CoroutineScope(Dispatchers.IO).launch {
                    repo.rejectOrder(orderId!!)
                }
            }
        }
    }
}