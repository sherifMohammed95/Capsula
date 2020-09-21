package com.blueMarketing.capsula.ui.faqs

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.FAQ
import com.blueMarketing.capsula.databinding.ActivityFaqsBinding
import com.blueMarketing.capsula.ui.faqs.adapters.FAQsAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val FAQsModule = module {
    factory { FAQsAdapter() }
}
class FaqsActivity : BaseActivity<ActivityFaqsBinding,FAQsViewModel>(), FAQsNavigator,
    BaseRecyclerAdapter.OnITemClickListener<FAQ> {

    private val mViewModel: FAQsViewModel by viewModel()
    private val mAdapter: FAQsAdapter by inject()
    var mList = ArrayList<FAQ>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()

    }

    override fun getMyViewModel(): FAQsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_faqs
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    private fun initRecyclerView() {
        viewDataBinding?.faqsRecyclerView?.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    fun subscribeToLiveData() {

        mViewModel.faqsResponse.observe(this, Observer {
            mList = it
            mAdapter.setData(mList)
        })
    }

    override fun onItemClick(pos: Int, item: FAQ) {
        for (i in 0 until mList.size) {
            if (i == pos)
                mList[i].isExpanded = !mList[i].isExpanded
            else
                mList[i].isExpanded = false
        }
        mAdapter.setData(mList)
    }

    override fun backAction() {
        finish()
    }
}