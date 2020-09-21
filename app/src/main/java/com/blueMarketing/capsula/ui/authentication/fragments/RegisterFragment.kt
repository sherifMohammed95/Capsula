package com.blueMarketing.capsula.ui.authentication.fragments

import android.os.Bundle
import android.view.View
import com.blueMarketing.base.BaseFragment
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.FragmentRegisterBinding
import com.blueMarketing.capsula.ui.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding, AuthenticationViewModel>() {

    private val mViewModel: AuthenticationViewModel by sharedViewModel()
    override fun getMyViewModel(): AuthenticationViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}