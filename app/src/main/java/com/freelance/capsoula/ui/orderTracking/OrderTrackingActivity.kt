package com.freelance.capsoula.ui.orderTracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.databinding.ActivityOrderTrackingBinding
import com.freelance.capsoula.ui.orderTracking.adapters.OrderTrackingAdapter
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
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
