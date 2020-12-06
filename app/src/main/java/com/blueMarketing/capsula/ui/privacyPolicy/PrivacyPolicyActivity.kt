package com.blueMarketing.capsula.ui.privacyPolicy

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Policy
import com.blueMarketing.capsula.databinding.ActivityPrivacyPolicyBinding
import com.blueMarketing.capsula.ui.faqs.adapters.FAQsAdapter
import com.blueMarketing.capsula.ui.privacyPolicy.adapters.PolicyAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val policyModule = module {
    factory { PolicyAdapter() }
}

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding, PrivacyPolicyViewModel>(),
    PrivacyPolicyNavigator, BaseRecyclerAdapter.OnITemClickListener<Policy> {

    private val mViewModel: PrivacyPolicyViewModel by viewModel()
    private val mAdapter: PolicyAdapter by inject()
    var mList = ArrayList<Policy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()
    }

    override fun getMyViewModel(): PrivacyPolicyViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_privacy_policy
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    fun subscribeToLiveData() {

        mViewModel.policyResponse.observe(this, Observer {
            mList = it
            mAdapter.setData(mList)
        })
    }

    private fun initRecyclerView() {
        viewDataBinding?.policyRecyclerView?.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun backAction() {
        finish()
    }

    override fun onItemClick(pos: Int, item: Policy) {
        for (i in 0 until mList.size) {
            if (i == pos)
                mList[i].expanded = !mList[i].expanded
            else
                mList[i].expanded = false
        }
        mAdapter.setData(mList)
    }
}