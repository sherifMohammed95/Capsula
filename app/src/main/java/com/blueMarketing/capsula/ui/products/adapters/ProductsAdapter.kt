package com.blueMarketing.capsula.ui.products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.databinding.IndexProductBinding

class ProductsAdapter : BaseRecyclerAdapter<Product, ProductsAdapter.ProductViewHolder>() {

    var onPlusClickListener: OnPlusClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: IndexProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_product, parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items!![position])

        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }

        holder.binding.addCartImageView.setOnClickListener {
            onPlusClickListener?.onPlusClick(items!![position])
        }
    }

    class ProductViewHolder(val binding: IndexProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    interface OnPlusClickListener {
        fun onPlusClick(product: Product)
    }

}