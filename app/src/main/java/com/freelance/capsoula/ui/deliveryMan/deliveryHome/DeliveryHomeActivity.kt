package com.freelance.capsoula.ui.deliveryMan.deliveryHome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.databinding.ActivityDeliveryHomeBinding
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.adapters.DeliveryOrdersAdapter
import com.freelance.capsoula.ui.deliveryMan.deliveryOrderDetails.DeliveryOrderDetailsActivity
import com.freelance.capsoula.ui.more.MoreActivity
import com.freelance.capsoula.ui.orderDetails.OrderDetailsActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_delivery_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val deliveryHomeModule = module {
    factory { DeliveryOrdersAdapter() }
}

class DeliveryHomeActivity : BaseActivity<ActivityDeliveryHomeBinding, DeliveryHomeViewModel>(),
    DeliveryHomeNavigator, BaseRecyclerAdapter.OnITemClickListener<DeliveryOrder>,
    DeliveryOrdersAdapter.OnNavigateClickListener {

    private val mViewModel: DeliveryHomeViewModel by viewModel()
    private val mAdapter: DeliveryOrdersAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        mViewModel.deliveryHomeDataResponse.observe(this, Observer {
            if (!it.ordersList.isNullOrEmpty()) {
                Constants.REFRESH_DELIVERY_ORDER = false
                mAdapter.setData(it.ordersList!!)
            }
        })
    }

    private fun initRecyclerView() {
        delivery_orders_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
        mAdapter.onNavigateClickListener = this
    }

    override fun onResume() {
        super.onResume()
        if (Constants.REFRESH_DELIVERY_ORDER) {
            mViewModel.getHomeData()
        }
    }

    override fun getMyViewModel(): DeliveryHomeViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_delivery_home
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun openMore() {
        startActivity(Intent(this, MoreActivity::class.java))
    }

    override fun onItemClick(pos: Int, item: DeliveryOrder) {
        val intent = Intent(this, DeliveryOrderDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER, Gson().toJson(item))
        intent.putExtra(Constants.FROM_HISTORY, false)
        startActivity(intent)
    }

    override fun onNavigateClick(item: DeliveryOrder) {
        Utils.navigateToLocation(this,item.customerLat.toString(), item.customerLong.toString())
    }
}