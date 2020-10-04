package com.blueMarketing.capsula.ui.checkout.fragment.cart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.blueMarketing.base.BaseRecyclerSwipeAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.databinding.IndexCartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartAdapter : BaseRecyclerSwipeAdapter<Product, CartAdapter.CartViewHolder>() {

    var onIconsClickListener: OnIconsClickListener? = null
    var hasShowDemo = false

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

        if (!hasShowDemo) {
            if (position == 0) {
                CoroutineScope(Main).launch {
                    delay(500)
                    holder.binding.swipeLayout.open(true)
                    delay(800)
                    holder.binding.swipeLayout.close(true)
                }
            }
            hasShowDemo = true
        }

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