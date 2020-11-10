package com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.databinding.ActivityDeliveryOrderDetailsBinding
import com.blueMarketing.capsula.ui.orderDetails.adapters.ProductsDetailsAdapter
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Utils
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
            openStoreLocation()
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

    override fun refreshAction() {
        mViewModel.loadOrderDetails()
    }

    override fun openStoreLocation() {
        Utils.navigateToLocation(
            this, mViewModel.mOrder.storeLat.toString(),
            mViewModel.mOrder.storeLong.toString()
        )
    }

    override fun openCustomerLocation() {
        if (mViewModel.mOrder.statusId == Constants.ORDER_IS_PROCESSING)
            Utils.navigateToLocation(
                this, mViewModel.mOrder.customerLat.toString(),
                mViewModel.mOrder.customerLong.toString()
            )
        else {
            showPopUp("",
                getString(R.string.reach_out),
                getString(android.R.string.ok),
                false
            )
        }
    }
}