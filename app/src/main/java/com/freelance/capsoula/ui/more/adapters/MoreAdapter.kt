package com.freelance.capsoula.ui.more.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.MoreItem
import com.freelance.capsoula.databinding.IndexMoreBinding

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