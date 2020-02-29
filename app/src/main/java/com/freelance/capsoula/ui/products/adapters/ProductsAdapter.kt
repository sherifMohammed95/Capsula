package com.freelance.capsoula.ui.products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.databinding.IndexProductBinding

class ProductsAdapter : BaseRecyclerAdapter<Product, ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: IndexProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_product, parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items!![position])
    }


    class ProductViewHolder(val binding: IndexProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

}