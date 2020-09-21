package com.blueMarketing.capsula.ui.myOrders

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.databinding.ActivityMyOrdersBinding
import com.blueMarketing.capsula.ui.myOrders.adapters.OrdersAdapter
import com.blueMarketing.capsula.ui.orderDetails.OrderDetailsActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_orders.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val ordersModule = module {
    factory { OrdersAdapter() }
    factory { OrdersRepository() }
}

class MyOrdersActivity : BaseActivity<ActivityMyOrdersBinding, MyOrdersViewModel>(),
    BaseRecyclerAdapter.OnITemClickListener<Order> {

    private val mViewModel: MyOrdersViewModel by viewModel()
    private val mAdapter: OrdersAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar(getString(R.string.my_orders))
        initRecyclerView()
        subscribeToLiveData()
    }

    override fun getMyViewModel(): MyOrdersViewModel {
        return mViewModel
    }

    override fun onResume() {
        super.onResume()
        if (!mViewModel.orderList.isNullOrEmpty())
            mViewModel.loadOrders(false)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_orders
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }

    private fun subscribeToLiveData() {
        mViewModel.ordersResponse.observe(this, Observer {
            if (!it.data?.orderList.isNullOrEmpty()) {
                mViewModel.orderList = it.data?.orderList!!
                mAdapter.setData(it.data?.orderList!!)
            }
        })
    }

    private fun initRecyclerView() {
        orders_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun onItemClick(pos: Int, item: Order) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER, Gson().toJson(item))
        startActivity(intent)
    }


}
