package com.freelance.capsoula.ui.resetPassword

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityResetPasswordBinding
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>(),
    ResetPasswordNavigator {

    private val mViewModel: ResetPasswordViewModel by viewModel()

    override fun getMyViewModel(): ResetPasswordViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_reset_password
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.backImageView!!.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        subscribeToLiveData()
    }

    private fun getIntentsData() {
        mViewModel.fromChangePassword.set(
            intent.getBooleanExtra(
                Constants.FROM_CHANGE_PASSWORD,
                false
            )
        )
        if (!mViewModel.fromChangePassword.get())
            mViewModel.phoneNumber = intent.getStringExtra(Constants.EXTRA_PHONE)!!

    }

    private fun subscribeToLiveData() {
        mViewModel.resetPasswordResponse.observe(this, Observer {
            showPopUp(
                "", getString(R.string.password_has_changed), getString(android.R.string.ok),
                DialogInterface.OnClickListener { _, _ ->
                    openAuthentication()
                }, false
            )
        })

        mViewModel.changePasswordResponse.observe(this, Observer {
            showPopUp(
                "", getString(R.string.password_has_changed), getString(android.R.string.ok),
                DialogInterface.OnClickListener { _, _ ->
                    finish()
                }, false
            )
        })
    }

    override fun openAuthentication() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
