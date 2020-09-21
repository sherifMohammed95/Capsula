package com.blueMarketing.capsula.ui.deliveryMan.viewProfile.fragments

import android.os.Bundle
import android.view.View
import com.blueMarketing.base.BaseFragment
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.FragmentViewDeliveryCarDetailsBinding
import com.blueMarketing.capsula.databinding.FragmentViewDeliveryPersonalDetailsBinding
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.ViewProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ViewCarDetailsFragment:
    BaseFragment<FragmentViewDeliveryCarDetailsBinding, ViewProfileViewModel>() {

    private val mViewModel:ViewProfileViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val user = UserDataSource.getDeliveryUser()
        viewDataBinding?.user = user
    }

    override fun getMyViewModel(): ViewProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_view_delivery_car_details
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }
}