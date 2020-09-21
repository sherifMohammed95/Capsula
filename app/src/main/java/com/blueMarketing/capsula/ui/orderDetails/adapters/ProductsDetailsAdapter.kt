package com.blueMarketing.capsula.ui.orderDetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.databinding.IndexOrderDetailsProductBinding

class ProductsDetailsAdapter:BaseRecyclerAdapter<Product,ProductsDetailsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: IndexOrderDetailsProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_order_details_product, parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items!![position])
    }


    class ProductViewHolder(val binding: IndexOrderDetailsProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }
}