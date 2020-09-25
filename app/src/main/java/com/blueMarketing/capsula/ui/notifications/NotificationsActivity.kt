package com.blueMarketing.capsula.ui.notifications

import android.content.Intent
import android.os.Bundle
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Notification
import com.blueMarketing.capsula.databinding.ActivityNotificationsBinding
import com.blueMarketing.capsula.ui.notifications.adapters.NotificationsAdapter
import androidx.lifecycle.Observer
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import com.blueMarketing.capsula.ui.home.HomeActivity
import com.blueMarketing.capsula.ui.orderTracking.OrderTrackingActivity
import com.blueMarketing.capsula.ui.productDetails.ProductDetailsActivity
import com.blueMarketing.capsula.ui.splash.SplashActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_notifications.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val notificationsModule = module {
    factory { NotificationsAdapter() }
}

class NotificationsActivity : BaseActivity<ActivityNotificationsBinding, NotificationsViewModel>(),
    NotificationsNavigator, BaseRecyclerAdapter.OnITemClickListener<Notification> {

    private val mViewModel: NotificationsViewModel by viewModel()
    private val mAdapter: NotificationsAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.notificationResponse.observe(this, Observer {
            if (!it.notificationsList.isNullOrEmpty()) {
                mViewModel.haveNotifications.set(true)
                mAdapter.setData(it.notificationsList!!)
            } else {
                mViewModel.haveNotifications.set(false)
            }
        })
    }

    private fun initRecyclerView() {
        notifications_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }


    override fun getMyViewModel(): NotificationsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_notifications
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.navigator = this
    }

    override fun backAction() {
        finish()
    }

    override fun onItemClick(pos: Int, item: Notification) {
        when (item.type) {
            Constants.NEW_CUSTOMER_OFFER -> {
                intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT, Gson().toJson(item.product))
                intent.putExtra(Constants.FROM_WHERE, Constants.FROM_NOTIFICATIONS)
                startActivity(intent)
            }
            Constants.ORDER_TRACKING -> {
                intent = Intent(this, OrderTrackingActivity::class.java)
                intent.putExtra(Constants.EXTRA_ORDER_ID, item.orderId)
                startActivity(intent)
            }
        }
    }


}