package com.blueMarketing.base

import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

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

//    fun setOnITemClickListener(onITemClickListener: OnITemClickListener<T>) {
//        this.onITemClickListener = onITemClickListener
//    }

    interface OnITemClickListener<T> {
        fun onItemClick(pos: Int, item: T)
    }
}