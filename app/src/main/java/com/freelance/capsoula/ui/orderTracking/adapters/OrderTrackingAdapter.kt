package com.freelance.capsoula.ui.orderTracking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.OrderTracking
import com.freelance.capsoula.databinding.IndexOrderDetailsProductBinding
import com.freelance.capsoula.databinding.IndexOrderTrackingBinding
import com.freelance.capsoula.ui.orderDetails.adapters.ProductsDetailsAdapter

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