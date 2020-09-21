package com.blueMarketing.capsula.ui.orderTracking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.OrderTracking
import com.blueMarketing.capsula.databinding.IndexOrderTrackingBinding

class OrderTrackingAdapter :
    BaseRecyclerAdapter<OrderTracking, OrderTrackingAdapter.OrderTrackingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderTrackingViewHolder {
        val binding: IndexOrderTrackingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_order_tracking, parent,
            false
        )
        return OrderTrackingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderTrackingViewHolder, position: Int) {
        holder.bind(items!![position])
    }


    class OrderTrackingViewHolder(val binding: IndexOrderTrackingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderTracking) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}