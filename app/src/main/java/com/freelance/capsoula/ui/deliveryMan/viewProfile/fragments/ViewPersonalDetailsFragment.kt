package com.freelance.capsoula.ui.deliveryMan.viewProfile.fragments

import android.os.Bundle
import android.view.View
import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.FragmentViewDeliveryPersonalDetailsBinding
import com.freelance.capsoula.ui.deliveryMan.viewProfile.ViewProfileNavigator
import com.freelance.capsoula.ui.deliveryMan.viewProfile.ViewProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ViewPersonalDetailsFragment :
    BaseFragment<FragmentViewDeliveryPersonalDetailsBinding, ViewProfileViewModel>(){

    private val mViewModel:ViewProfileViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = UserDataSource.getDeliveryUser()
        viewDataBinding?.user = user
    }

    override fun getMyViewModel(): ViewProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_view_delivery_personal_details
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }
}