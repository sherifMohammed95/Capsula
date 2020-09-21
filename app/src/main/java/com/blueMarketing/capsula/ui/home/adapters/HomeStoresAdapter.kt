package com.blueMarketing.capsula.ui.home.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Store
import com.blueMarketing.capsula.databinding.IndexHomeStoreBinding
import com.blueMarketing.capsula.ui.categories.CategoriesActivity
import com.blueMarketing.capsula.utils.Constants

class HomeStoresAdapter(val context: Context) :
    BaseRecyclerAdapter<Store, HomeStoresAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding: IndexHomeStoreBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_home_store, parent,
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


    class StoreViewHolder(val binding: IndexHomeStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.store = store
            binding.executePendingBindings()
        }
    }
}