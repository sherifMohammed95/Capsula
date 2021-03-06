package com.blueMarketing.capsula.ui.deliveryMan.viewProfile.fragments

import android.os.Bundle
import android.view.View
import com.blueMarketing.base.BaseFragment
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.FragmentViewDeliveryRequiredDocumentsBinding
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.ViewProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ViewRequiredDocumentsFragment :
    BaseFragment<FragmentViewDeliveryRequiredDocumentsBinding, ViewProfileViewModel>() {

    private val mViewModel: ViewProfileViewModel by sharedViewModel()

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
        return R.layout.fragment_view_delivery_required_documents
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }
}