package com.blueMarketing.capsula.ui.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.MoreItem
import com.blueMarketing.capsula.databinding.IndexMoreBinding
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.preferencesGateway

class MoreAdapter : BaseRecyclerAdapter<MoreItem, MoreAdapter.MoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder {
        val binding: IndexMoreBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_more, parent, false
        )

        return MoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoreViewHolder, position: Int) {
        holder.bind(items!![position])
        if (items!![position].hasNotificationSwitch)
            holder.binding.notificationsSwitch.visibility = View.VISIBLE
        else
            holder.binding.notificationsSwitch.visibility = View.GONE

        holder.binding.notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            preferencesGateway.save(Constants.NOTIFICATIONS_IS_ENABLED, isChecked)
        }

        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }
    }

    class MoreViewHolder(val binding: IndexMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MoreItem) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}