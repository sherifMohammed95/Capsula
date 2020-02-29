package com.freelance.capsoula.ui.brands.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Brand
import com.freelance.capsoula.databinding.IndexBrandBinding

class BrandsAdapter : BaseRecyclerAdapter<Brand, BrandsAdapter.BrandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding: IndexBrandBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_brand, parent,
            false
        )
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }
    }


    class BrandViewHolder(val binding: IndexBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: Brand) {
            binding.brand = brand
            binding.executePendingBindings()
        }
    }
}