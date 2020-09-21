package com.blueMarketing.capsula.ui.faqs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.FAQ
import com.blueMarketing.capsula.databinding.IndexFaqBinding

class FAQsAdapter : BaseRecyclerAdapter<FAQ, FAQsAdapter.FAQsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQsViewHolder {
        val binding: IndexFaqBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_faq, parent, false
        )
        return FAQsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FAQsViewHolder, position: Int) {
        val faq = items!![position]

        holder.binding.header.setOnClickListener {
            onITemClickListener.onItemClick(position,faq)
        }
        holder.bind(faq)
    }

    class FAQsViewHolder(val binding: IndexFaqBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(FAQ: FAQ) {
            binding.faq = FAQ
            binding.executePendingBindings()
        }
    }
}