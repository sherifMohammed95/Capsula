package com.freelance.capsoula.ui.checkout.fragment.cart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.base.BaseRecyclerSwipeAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.databinding.IndexCartBinding

class CartAdapter : BaseRecyclerSwipeAdapter<Product, CartAdapter.CartViewHolder>() {

    var onIconsClickListener: OnIconsClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding: IndexCartBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_cart, parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = items!![position]
        holder.binding.swipeLayout.close(true)

        holder.binding.plusImageView.setOnClickListener {
            onIconsClickListener?.onPlusClick(product)
        }

        holder.binding.minusImageView.setOnClickListener {
            if (product.quantity > 1)
                onIconsClickListener?.onMinusClick(product)
        }
        holder.binding.deleteImage.setOnClickListener {
            onIconsClickListener?.onDeleteClick(product)
        }

        holder.binding.productCard.setOnClickListener {
            onITemClickListener.onItemClick(position, product)
        }

        mItemManger.bindView(holder.itemView, position)

        holder.binding.swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onStartOpen(layout: SwipeLayout) {

                mItemManger.closeAllExcept(layout)
            }

            override fun onOpen(layout: SwipeLayout) {}

            override fun onStartClose(layout: SwipeLayout) {

            }

            override fun onClose(layout: SwipeLayout) {

            }

            override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {

            }

            override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {

            }
        })
        holder.bind(product)
    }

    class CartViewHolder(val binding: IndexCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    interface OnIconsClickListener {
        fun onPlusClick(product: Product)
        fun onMinusClick(product: Product)
        fun onDeleteClick(product: Product)
    }
}