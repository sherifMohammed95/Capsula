package com.blueMarketing.capsula.ui.home.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Brand
import com.blueMarketing.capsula.databinding.IndexHomeBrandBinding
import com.blueMarketing.capsula.ui.products.ProductsActivity
import com.blueMarketing.capsula.utils.Constants

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