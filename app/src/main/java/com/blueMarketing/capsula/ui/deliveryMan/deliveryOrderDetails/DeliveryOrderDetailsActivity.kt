package com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails

import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.data.OrderStatus
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
                mAdapter.setData(it.data?.products!!)

                when (mViewModel.mOrder.statusId?.value) {
                    OrderStatus.ON_WAY.value -> {
                        if (mViewModel.isBtnClicked) {
                            mViewModel.isBtnClicked = false
                            openStoreLocation()
                        }
                    }
                    OrderStatus.SHIPPED.value -> {
                        if (mViewModel.isBtnClicked) {
                            mViewModel.isBtnClicked = false
                            openCustomerLocation()
                        }
                    }
                }
                viewDataBinding?.order = it.data
            }
        })

        mViewModel.changeOrderStatusResponse.observe(this, Observer {
            if (mViewModel.mOrder.statusId?.value == OrderStatus.DELIVERED.value) {
                Constants.REFRESH_DELIVERY_ORDER = true
                finish()
            } else
                mViewModel.loadOrderDetails()
        })

        mViewModel.askDeliveryEvent.observe(this, Observer {
            showPopUp(
                R.string.is_the_order, getString(R.string.no),
                DialogInterface.OnClickListener { _, _ ->
                    mViewModel.endDelivery(OrderStatus.UNCOMPLETED.value)
                }, getString(R.string.yes),
                DialogInterface.OnClickListener { _, _ ->
                    mViewModel.endDelivery(OrderStatus.COMPLETED.value)
                }, true
            )
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
        if (!mViewModel.fromHistory.get())
            if (mViewModel.mOrder.storeLocationIsEnabled())
                Utils.navigateToLocation(
                    this, mViewModel.mOrder.storeLat.toString(),
                    mViewModel.mOrder.storeLong.toString()
                )
            else
                showPopUp(
                    "",
                    getString(R.string.please_start_delivery),
                    getString(android.R.string.ok),
                    false
                )
    }

    override fun openCustomerLocation() {
        if (!mViewModel.fromHistory.get()) {

            if (mViewModel.mOrder.clientLocationIsEnabled())
                Utils.navigateToLocation(
                    this, mViewModel.mOrder.customerLat.toString(),
                    mViewModel.mOrder.customerLong.toString()
                )
            else {
                showPopUp(
                    "",
                    getString(R.string.reach_out),
                    getString(android.R.string.ok),
                    false
                )
            }
        }
    }
}