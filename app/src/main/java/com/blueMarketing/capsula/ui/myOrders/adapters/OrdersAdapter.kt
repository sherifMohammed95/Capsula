package com.blueMarketing.capsula.ui.myOrders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.databinding.IndexOrderBinding

class OrdersAdapter : BaseRecyclerAdapter<Order, OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding: IndexOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_order, parent, false
        )

        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }
    }


    class OrderViewHolder(val binding: IndexOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order) {
            binding.order = item
            binding.executePendingBindings()
        }
    }
}