package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityDeliveryAuthenticationBinding
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin.DeliveryLoginFragment
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails.CarDetailsFragment
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails.PersonalDetailsFragment
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
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.delivery_container, DeliveryLoginFragment())
            .commit()
    }

    override fun openPersonalDetails() {
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

    }
}
