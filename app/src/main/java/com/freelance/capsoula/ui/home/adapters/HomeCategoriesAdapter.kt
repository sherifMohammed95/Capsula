package com.freelance.capsoula.ui.home.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.databinding.IndexHomeCategoryBinding
import com.freelance.capsoula.ui.subCategories.SubCategoriesActivity
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson

class HomeCategoriesAdapter(val context: Context) :
    BaseRecyclerAdapter<Category, HomeCategoriesAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding: IndexHomeCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.index_home_category, parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items!![position])
        holder.itemView.setOnClickListener {
            val intent  = Intent(context, SubCategoriesActivity::class.java)
            intent.putExtra(Constants.EXTRA_CATEGORY, Gson().toJson(items!![position]))
            context.startActivity(intent)
        }
    }


    class CategoryViewHolder(val binding: IndexHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.category = category
            binding.executePendingBindings()
        }
    }
}