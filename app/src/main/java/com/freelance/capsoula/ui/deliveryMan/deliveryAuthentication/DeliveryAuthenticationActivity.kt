package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityDeliveryAuthenticationBinding
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin.DeliveryLoginFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryAuthenticationActivity :
    BaseActivity<ActivityDeliveryAuthenticationBinding, DeliveryAuthenticationViewModel>(),
    DeliveryAuthenticationNavigator {

    private val mViewModel: DeliveryAuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getMyViewModel(): DeliveryAuthenticationViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_delivery_authentication
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }


    override fun openLogin() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.delivery_container, DeliveryLoginFragment())
            .commit()
    }

    override fun openPersonalDetails() {

    }

    override fun openCarDetails() {

    }

    override fun openRequiredDocuments() {

    }
}
