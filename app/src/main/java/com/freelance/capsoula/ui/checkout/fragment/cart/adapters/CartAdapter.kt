package com.freelance.capsoula.ui.checkout.fragment.cart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.databinding.IndexCartBinding

class CartAdapter : BaseRecyclerAdapter<Product, CartAdapter.CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding: IndexCartBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_cart, parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items!![position])
    }


    class CartViewHolder(val binding: IndexCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }
}