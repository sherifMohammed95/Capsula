package com.blueMarketing.capsula.ui.orderTracking

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityOrderTrackingBinding
import com.blueMarketing.capsula.ui.orderTracking.adapters.OrderTrackingAdapter
import com.blueMarketing.capsula.utils.Constants
import kotlinx.android.synthetic.main.activity_order_tracking.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val orderTrackingModule = module {
    factory { OrderTrackingAdapter() }
}

class OrderTrackingActivity : BaseActivity<ActivityOrderTrackingBinding, OrderTrackingViewModel>(),
    OrderTrackingNavigator {

    private val mViewModel: OrderTrackingViewModel by viewModel()
    private val mAdapter: OrderTrackingAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        initRecyclerView()
        subscribeToLiveData()
    }

    override fun getMyViewModel(): OrderTrackingViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order_tracking
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    private fun getIntentsData() {
        mViewModel.orderId = intent.getIntExtra(Constants.EXTRA_ORDER_ID, -1)

        if (mViewModel.orderId != -1)
            mViewModel.loadOrderTracking()
    }

    private fun subscribeToLiveData() {
        mViewModel.orderTrackingResponse.observe(this, Observer {
            if (!it.data?.orderTrackingList.isNullOrEmpty()) {
                viewDataBinding?.item = it.data?.orderTrackingList?.get(0)
                mAdapter.setData(it.data?.orderTrackingList!!)
            }
        })
    }

    private fun initRecyclerView() {
        order_tracking_recyclerView.adapter = mAdapter
    }

    override fun backAction() {
        finish()
    }

}
