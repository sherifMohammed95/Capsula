package com.freelance.capsoula.ui.myOrders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.databinding.IndexMoreBinding
import com.freelance.capsoula.databinding.IndexOrderBinding
import com.freelance.capsoula.ui.more.adapters.MoreAdapter

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