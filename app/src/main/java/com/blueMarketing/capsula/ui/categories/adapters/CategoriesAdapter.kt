package com.blueMarketing.capsula.ui.categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.databinding.IndexCategoryBinding

class CategoriesAdapter : BaseRecyclerAdapter<Category, CategoriesAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding: IndexCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_category, parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items!![position])

        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }

    }


    class CategoryViewHolder(val binding: IndexCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            binding.executePendingBindings()
        }
    }
}