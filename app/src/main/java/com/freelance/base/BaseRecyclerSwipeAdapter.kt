package com.freelance.base

import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.freelance.capsoula.R

abstract class BaseRecyclerSwipeAdapter<T, VH : RecyclerView.ViewHolder> :
    RecyclerSwipeAdapter<VH>() {

    var items: List<T>? = ArrayList()
    var selectedPos = -1

    lateinit var onITemClickListener: OnITemClickListener<T>

    override fun getItemCount(): Int {
        return if (items == null) 0 else items!!.size
    }

    fun setData(newItems: List<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }

    interface OnITemClickListener<T> {
        fun onItemClick(pos: Int, item: T)
    }
}