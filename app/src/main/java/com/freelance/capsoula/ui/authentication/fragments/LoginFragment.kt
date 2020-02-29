package com.freelance.capsoula.ui.authentication.fragments

import android.os.Bundle
import android.view.View
import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.FragmentLoginBinding
import com.freelance.capsoula.ui.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment:BaseFragment<FragmentLoginBinding,AuthenticationViewModel>() {

    private val mViewModel: AuthenticationViewModel by sharedViewModel()
    override fun getMyViewModel(): AuthenticationViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}