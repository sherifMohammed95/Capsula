package com.freelance.capsoula.ui.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.MoreItem
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivityMoreBinding
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.ui.deliveryMan.history.HistoryActivity
import com.freelance.capsoula.ui.home.HomeViewModel
import com.freelance.capsoula.ui.more.adapters.MoreAdapter
import com.freelance.capsoula.ui.myOrders.MyOrdersActivity
import com.freelance.capsoula.utils.AnimationUtils
import com.freelance.capsoula.utils.Constants
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_more.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MoreActivity : BaseActivity<ActivityMoreBinding, MoreViewModel>(), MoreNavigator,
    BaseRecyclerAdapter.OnITemClickListener<MoreItem> {

    private val mViewModel: MoreViewModel by viewModel()
    private val guestList: ArrayList<MoreItem> by inject(named(GUEST_MORE_LIST))
    private val loggedList: ArrayList<MoreItem> by inject(named(LOGGED_MORE_LIST))
    private val deliveryList: ArrayList<MoreItem> by inject(named(DELIVERY_MORE_LIST))
    private val mAdapter: MoreAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            AnimationUtils
                .circularTransition(viewDataBinding?.parent!!)
        }, 50)

        initRecyclerView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.deleteCartResponse.observe(this, Observer {
            logout()
        })
    }

    override fun onResume() {
        super.onResume()
        when {
            UserDataSource.getDeliveryUser() != null -> mAdapter.setData(deliveryList)
            UserDataSource.getUser() == null -> mAdapter.setData(guestList)
            else -> mAdapter.setData(loggedList)
        }
    }

    private fun initRecyclerView() {
        more_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun getMyViewModel(): MoreViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_more
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun onBackPressed() {
        closeMore()
    }

    override fun onItemClick(pos: Int, item: MoreItem) {
        mViewModel.navigate(item)
    }

    override fun closeMore() {
        Handler().postDelayed({
            AnimationUtils
                .circularReverseTransition(viewDataBinding?.parent!!, Action {
                    more_recyclerView.visibility = View.INVISIBLE
                    finish()
                })
        }, 50)
    }

    override fun openMyOrders() {
        startActivity(Intent(this, MyOrdersActivity::class.java))
    }

    override fun openPersonalDetails() {

    }

    override fun openLogin() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_MORE)
        startActivity(intent)
    }

    override fun logout() {
        UserDataSource.saveUser(null)
        UserDataSource.saveDeliveryUser(null)
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun openHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    override fun openMyWallet() {
    }
}
