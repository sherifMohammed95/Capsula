package com.freelance.capsoula.ui.orderDetails

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.databinding.ActivityOrderDetailsBinding
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.orderDetails.adapters.ProductsDetailsAdapter
import com.freelance.capsoula.ui.orderTracking.OrderTrackingActivity
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val orderDetailsModule = module {
    factory { ProductsDetailsAdapter() }
}

class OrderDetailsActivity : BaseActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel>(),
    OrderDetailsNavigator {

    private val mViewModel: OrderDetailsViewModel by viewModel()
    private val mAdapter: ProductsDetailsAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        initRecyclerView()
        subscribeToLiveData()
    }

    override fun getMyViewModel(): OrderDetailsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order_details
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    private fun getIntentsData() {
        mViewModel.mOrder =
            Gson().fromJson(intent.getStringExtra(Constants.EXTRA_ORDER), Order::class.java)

        mViewModel.loadOrderDetails()
    }

    private fun subscribeToLiveData() {
        mViewModel.orderDetailsResponse.observe(this, Observer {
            if (it.data != null) {
                mViewModel.mOrder = it.data!!
                viewDataBinding?.order = it.data
                mAdapter.setData(it.data?.products!!)
            }
        })

        mViewModel.cancelOrderResponse.observe(this, Observer {
            showPopUp(
                it, android.R.string.ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    finish()
                }, false
            )
        })
    }

    private fun initRecyclerView() {
        order_details_products_recyclerView.adapter = mAdapter
    }

    override fun backAction() {
        finish()
    }

    override fun trackOrderAction() {
        val intent = Intent(this, OrderTrackingActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER_ID, mViewModel.mOrder.id)
        startActivity(intent)
    }

    override fun openCheckout() {
        val intent = Intent(this, CheckoutActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER, Gson().toJson(mViewModel.mOrder))
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_ORDER_DETAILS)
        startActivity(intent)
    }

}
