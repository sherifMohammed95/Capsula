package com.freelance.capsoula.ui.home.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Brand
import com.freelance.capsoula.databinding.IndexHomeBrandBinding
import com.freelance.capsoula.ui.products.ProductsActivity
import com.freelance.capsoula.utils.Constants

class HomeBrandsAdapter(val context: Context) :
    BaseRecyclerAdapter<Brand, HomeBrandsAdapter.BrandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding: IndexHomeBrandBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_home_brand, parent,
            false
        )
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductsActivity::class.java)
            intent.putExtra(Constants.EXTRA_BRAND_ID, items!![position].brandId)
            intent.putExtra(Constants.FROM_WHERE, Constants.FROM_BRANDS)
            context.startActivity(intent)
        }
    }

    class BrandViewHolder(val binding: IndexHomeBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: Brand) {
            binding.brand = brand
            binding.executePendingBindings()
        }
    }
}