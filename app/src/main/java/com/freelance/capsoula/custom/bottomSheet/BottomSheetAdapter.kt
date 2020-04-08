package com.freelance.capsoula.custom.bottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.IndexBottomSheetBinding

class BottomSheetAdapter : BaseRecyclerAdapter<BottomSheetModel,
        BottomSheetAdapter.BottomSheetViewHolder>() {

    var showIconImage = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding: IndexBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_bottom_sheet, parent, false
        )
        return BottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val item: BottomSheetModel = items!![position]
        item.selected = position == selectedPos
        holder.bind(item)
        holder.itemView.setOnClickListener {
            selectedPos = position
            onITemClickListener.onItemClick(pos = position, item = item)
            notifyDataSetChanged()
        }
    }

    class BottomSheetViewHolder(var binding: IndexBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BottomSheetModel) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}