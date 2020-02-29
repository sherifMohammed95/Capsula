package com.freelance.capsoula.ui.stores.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Store
import com.freelance.capsoula.databinding.IndexStoreBinding
import com.freelance.capsoula.ui.categories.CategoriesActivity
import com.freelance.capsoula.utils.Constants

class StoresAdapter(val context: Context) :
    BaseRecyclerAdapter<Store, StoresAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding: IndexStoreBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_store, parent,
            false
        )
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(items!![position])

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoriesActivity::class.java)
            intent.putExtra(Constants.EXTRA_STORE_ID, items!![position].storeId)
            context.startActivity(intent)
        }
    }

    class StoreViewHolder(val binding: IndexStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.store = store
            binding.executePendingBindings()
        }
    }
}