package com.blueMarketing.capsula.ui.privacyPolicy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.FAQ
import com.blueMarketing.capsula.data.Policy
import com.blueMarketing.capsula.databinding.IndexFaqBinding
import com.blueMarketing.capsula.databinding.IndexPolicyBinding
import com.blueMarketing.capsula.ui.faqs.adapters.FAQsAdapter

class PolicyAdapter: BaseRecyclerAdapter<Policy, PolicyAdapter.PolicyViewHolder>() {

    class PolicyViewHolder(val binding: IndexPolicyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(policy: Policy) {
            binding.policy = policy
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyViewHolder {
        val binding: IndexPolicyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_policy, parent, false
        )
        return PolicyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PolicyViewHolder, position: Int) {
        val policy = items!![position]

        holder.binding.header.setOnClickListener {
            onITemClickListener.onItemClick(position,policy)
        }
        holder.bind(policy)
    }
}