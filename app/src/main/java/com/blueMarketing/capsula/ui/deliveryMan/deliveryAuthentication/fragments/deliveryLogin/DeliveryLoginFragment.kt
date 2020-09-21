package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseFragment
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.FragmentDeliveryLoginBinding
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryLoginFragment : BaseFragment<FragmentDeliveryLoginBinding, DeliveryLoginViewModel>(),
    DeliveryLoginNavigator {

    private val mViewModel: DeliveryLoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.loginResponse.observe(viewLifecycleOwner, Observer {
          openHome()
        })
    }


    override fun getMyViewModel(): DeliveryLoginViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_delivery_login
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }

    override fun openHome() {
        val intent = Intent(activity, DeliveryHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}