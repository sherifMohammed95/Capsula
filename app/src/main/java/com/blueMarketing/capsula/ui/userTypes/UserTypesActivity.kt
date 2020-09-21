package com.blueMarketing.capsula.ui.userTypes

import android.content.Intent
import android.os.Bundle
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityUserTypesBinding
import com.blueMarketing.capsula.ui.authentication.AuthenticationActivity
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserTypesActivity : BaseActivity<ActivityUserTypesBinding, UserTypesViewModel>(),
    UserTypesNavigator {

    private val mViewModel: UserTypesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getMyViewModel(): UserTypesViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_types
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }

    override fun openCustomerAuthentication() {
        startActivity(Intent(this, AuthenticationActivity::class.java))
    }

    override fun openDeliveryAuthentication() {
        startActivity(Intent(this, DeliveryAuthenticationActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        mViewModel.customerType.set(false)
        mViewModel.deliveryType.set(false)
    }
}
