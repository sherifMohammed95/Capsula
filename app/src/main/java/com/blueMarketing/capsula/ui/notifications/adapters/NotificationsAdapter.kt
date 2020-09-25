package com.blueMarketing.capsula.ui.notifications.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Notification
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.databinding.IndexNotificationBinding
import com.blueMarketing.capsula.databinding.IndexOrderBinding
import com.blueMarketing.capsula.ui.myOrders.adapters.OrdersAdapter

class NotificationsAdapter :
    BaseRecyclerAdapter<Notification, NotificationsAdapter.NotificationViewHolder>() {


    class NotificationViewHolder(val binding: IndexNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification) {
            binding.notification = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding: IndexNotificationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_notification, parent, false
        )

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }
    }
}