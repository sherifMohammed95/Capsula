package com.freelance.capsoula.ui.subCategories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.databinding.IndexCategoryBinding
import com.freelance.capsoula.ui.categories.adapters.CategoriesAdapter

class SubCategoriesAdapter :
    BaseRecyclerAdapter<Category, SubCategoriesAdapter.SubCategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding: IndexCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_category, parent,
            false
        )
        return SubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(items!![position])

        holder.itemView.setOnClickListener {
            onITemClickListener.onItemClick(position, items!![position])
        }
    }

    class SubCategoryViewHolder(val binding: IndexCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            binding.executePendingBindings()
        }
    }
}