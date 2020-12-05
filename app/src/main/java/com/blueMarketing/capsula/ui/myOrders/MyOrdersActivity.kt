package com.blueMarketing.capsula.ui.myOrders

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.data.OrderStatus
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.databinding.ActivityMyOrdersBinding
import com.blueMarketing.capsula.ui.myOrders.adapters.OrdersAdapter
import com.blueMarketing.capsula.ui.orderDetails.OrderDetailsActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.activity_products.*
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
        if (Constants.REFRESH_CUSTOMER_ORDERS) {
            Constants.REFRESH_CUSTOMER_ORDERS = false
            if (mViewModel.currentOrderPos != -1) {
                mViewModel.orderList[mViewModel.currentOrderPos].orderStatusId =
                    OrderStatus.CANCELLED
                mAdapter.setData(mViewModel.orderList)
                mViewModel.currentOrderPos = -1
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_orders
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.noDataText = getString(R.string.no_orders_found)
        paginateWithScrollView(orders_scroll_layout, Action {
            mViewModel.pageNo++
            mViewModel.loadOrders(false)
        })

        swipeToRefresh(customer_orders_refresh_layout, Action {
            mViewModel.isLastPage = false
            mViewModel.pageNo = 1
            mViewModel.orderList = ArrayList()
            mViewModel.loadOrders(false)
        })
    }

    private fun subscribeToLiveData() {
        mViewModel.ordersResponse.observe(this, Observer {
            viewModel?.isPagingLoadingEvent?.value = false
            customer_orders_refresh_layout.isRefreshing = false
            if (it.data != null) {
                if (it.data!!.count > 0)
                    mViewModel.hasData.set(true)
                else
                    mViewModel.hasData.set(false)

                if (!it.data!!.orderList.isNullOrEmpty()) {
                    if (mViewModel.pageNo == 1)
                        mViewModel.orderList = (it.data!!.orderList!!)
                    else
                        mViewModel.orderList.addAll(it.data!!.orderList!!)
                    if (mViewModel.orderList.size == it.data!!.count)
                        mViewModel.isLastPage = true
                }
                mAdapter.setData(mViewModel.orderList)
            }
        })
    }

    private fun initRecyclerView() {
        orders_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun onItemClick(pos: Int, item: Order) {
        mViewModel.currentOrderPos = pos
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER, Gson().toJson(item))
        startActivity(intent)
    }


}
