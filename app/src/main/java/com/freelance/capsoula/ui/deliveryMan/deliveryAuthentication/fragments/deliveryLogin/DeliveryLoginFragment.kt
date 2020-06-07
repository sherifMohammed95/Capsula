package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin

import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.FragmentDeliveryLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryLoginFragment : BaseFragment<FragmentDeliveryLoginBinding, DeliveryLoginViewModel>() {

    private val mViewModel: DeliveryLoginViewModel by viewModel()

    override fun getMyViewModel(): DeliveryLoginViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_delivery_login
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }
}