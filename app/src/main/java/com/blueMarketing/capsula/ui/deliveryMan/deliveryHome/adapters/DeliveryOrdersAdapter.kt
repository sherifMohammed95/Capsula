package com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.databinding.IndexBrandBinding
import com.blueMarketing.capsula.databinding.IndexDeliveryHomeOrderBinding

class DeliveryOrdersAdapter :
    BaseRecyclerAdapter<DeliveryOrder, DeliveryOrdersAdapter.DeliveryOrdersViewHolder>() {

    lateinit var onNavigateClickListener: OnNavigateClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryOrdersViewHolder {
        val binding: IndexDeliveryHomeOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_delivery_home_order, parent,
            false
        )
        return DeliveryOrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryOrdersViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.binding.parentLayout.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }

        holder.binding.navigateTextView.setOnClickListener {
            onNavigateClickListener.onNavigateClick(items!![position])
        }
    }

    class DeliveryOrdersViewHolder(val binding: IndexDeliveryHomeOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: DeliveryOrder) {
            binding.order = order
            binding.executePendingBindings()
        }
    }

    interface OnNavigateClickListener {
        fun onNavigateClick(item: DeliveryOrder)
    }
}