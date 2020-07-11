package com.freelance.capsoula.ui.deliveryMan.deliveryHome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.databinding.IndexBrandBinding
import com.freelance.capsoula.databinding.IndexDeliveryHomeOrderBinding
import com.freelance.capsoula.ui.brands.adapters.BrandsAdapter

class DeliveryOrdersAdapter :
    BaseRecyclerAdapter<DeliveryOrder, DeliveryOrdersAdapter.DeliveryOrdersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryOrdersViewHolder {
        val binding: IndexDeliveryHomeOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_delivery_home_order, parent,
            false
        )
        return DeliveryOrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryOrdersViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }
    }

    class DeliveryOrdersViewHolder(val binding: IndexDeliveryHomeOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: DeliveryOrder) {
            binding.order = order
            binding.executePendingBindings()
        }
    }
}