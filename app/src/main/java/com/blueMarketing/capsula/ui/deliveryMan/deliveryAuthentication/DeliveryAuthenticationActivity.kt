package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityDeliveryAuthenticationBinding
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin.DeliveryLoginFragment
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails.CarDetailsFragment
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails.PersonalDetailsFragment
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.requiredDocuments.RequiredDocumentsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryAuthenticationActivity :
    BaseActivity<ActivityDeliveryAuthenticationBinding, DeliveryAuthenticationViewModel>(),
    DeliveryAuthenticationNavigator {

    val mViewModel: DeliveryAuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPersonalDetails()
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
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.delivery_container, DeliveryLoginFragment())
            .commit()
    }

    override fun openPersonalDetails() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.delivery_container, PersonalDetailsFragment())
            .commit()
    }

    override fun openCarDetails() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.delivery_container, CarDetailsFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun openRequiredDocuments() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.delivery_container, RequiredDocumentsFragment())
            .addToBackStack(null)
            .commit()
    }
}
