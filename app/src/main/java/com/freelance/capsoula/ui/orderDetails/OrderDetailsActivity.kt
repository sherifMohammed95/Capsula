package com.freelance.capsoula.ui.orderDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.databinding.ActivityOrderDetailsBinding
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
        val mOrder: Order =
            Gson().fromJson(intent.getStringExtra(Constants.EXTRA_ORDER), Order::class.java)

        mViewModel.orderId = mOrder.id
        if (mViewModel.orderId != -1)
            mViewModel.loadOrderDetails()
    }

    private fun subscribeToLiveData() {
        mViewModel.orderDetailsResponse.observe(this, Observer {
            if (it.data != null) {
                viewDataBinding?.order = it.data
                mAdapter.setData(it.data?.products!!)
            }
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
        intent.putExtra(Constants.EXTRA_ORDER_ID, mViewModel.orderId)
        startActivity(intent)
    }

}
