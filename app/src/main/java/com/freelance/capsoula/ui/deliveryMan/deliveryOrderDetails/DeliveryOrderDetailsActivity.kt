package com.freelance.capsoula.ui.deliveryMan.deliveryOrderDetails

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.databinding.ActivityDeliveryOrderDetailsBinding
import com.freelance.capsoula.ui.orderDetails.adapters.ProductsDetailsAdapter
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_delivery_order_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryOrderDetailsActivity :
    BaseActivity<ActivityDeliveryOrderDetailsBinding, DeliveryOrderDetailsViewModel>(),
    DeliveryOrderDetailsNavigator {

    private val mAdapter: ProductsDetailsAdapter by inject()
    private val mViewModel: DeliveryOrderDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        initRecyclerView()
        subscribeToLiveData()
    }

    private fun getIntentsData() {
        mViewModel.mOrder =
            Gson().fromJson(intent.getStringExtra(Constants.EXTRA_ORDER), DeliveryOrder::class.java)
        mViewModel.fromHistory.set(intent.getBooleanExtra(Constants.FROM_HISTORY, false))
        mViewModel.loadOrderDetails()
    }

    private fun subscribeToLiveData() {
        mViewModel.deliveryOrderDetailsResponse.observe(this, Observer {
            if (it.data != null) {
                mViewModel.mOrder = it.data!!
                viewDataBinding?.order = it.data
                mAdapter.setData(it.data?.products!!)
            }
        })

        mViewModel.startDeliveryResponse.observe(this, Observer {
            Constants.REFRESH_DELIVERY_ORDER = true
            backAction()
        })

        mViewModel.finishDeliveryResponse.observe(this, Observer {
            Constants.REFRESH_DELIVERY_ORDER = true
            backAction()
        })
    }


    private fun initRecyclerView() {
        delivery_order_details_products_recyclerView.adapter = mAdapter
    }

    override fun getMyViewModel(): DeliveryOrderDetailsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_delivery_order_details
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun backAction() {
        finish()
    }
}