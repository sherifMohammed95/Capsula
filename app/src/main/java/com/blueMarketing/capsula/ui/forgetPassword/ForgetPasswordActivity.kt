package com.blueMarketing.capsula.ui.forgetPassword

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityForgetPasswordBinding
import com.blueMarketing.capsula.ui.verification.VerificationActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgetPasswordActivity :
    BaseActivity<ActivityForgetPasswordBinding, ForgetPasswordViewModel>(),
    ForgetPasswordNavigator {

    private val mViewModel: ForgetPasswordViewModel by viewModel()

    override fun getMyViewModel(): ForgetPasswordViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.backImageView!!.setOnClickListener {
            finish()
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.checkUserExistResponse.observe(this, Observer {
            openVerification()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToLiveData()
    }

    override fun openVerification() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(Constants.EXTRA_REGISTER_REQUEST, Gson().toJson(null))
        intent.putExtra(Constants.EXTRA_COMPLETE_PROFILE_REQUEST, Gson().toJson(null))
        intent.putExtra(Constants.EXTRA_PHONE, mViewModel.phoneText.get())
        intent.putExtra(Constants.FROM_WHERE, Constants.FORGET_PASSWORD_SCREEN)
        startActivity(intent)
    }
}
