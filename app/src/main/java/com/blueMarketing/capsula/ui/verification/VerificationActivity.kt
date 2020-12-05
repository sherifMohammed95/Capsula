package com.blueMarketing.capsula.ui.verification

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bluecrunch.bluecrunchverification.FCMCallBack
import com.bluecrunch.bluecrunchverification.VerificationIntegration
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.requests.CompleteProfileRequest
import com.blueMarketing.capsula.data.requests.RegisterRequest
import com.blueMarketing.capsula.databinding.ActivityVerificationBinding
import com.blueMarketing.capsula.ui.addAddress.AddAddressActivity
import com.blueMarketing.capsula.ui.resetPassword.ResetPasswordActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_verification.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerificationActivity : BaseActivity<ActivityVerificationBinding, VerificationViewModel>(),
    VerificationNavigator, FCMCallBack {

    //    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mAuth: FirebaseAuth? = null
    private val mViewModel: VerificationViewModel by viewModel()
    private lateinit var integration: VerificationIntegration


    override fun getMyViewModel(): VerificationViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_verification
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
//        mAuth = FirebaseAuth.getInstance()
        back_imageView.setOnClickListener {
            finish()
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.verifyPhoneWithCodeEvent.observe(this, Observer {
            //            verifyPhoneNumberWithCode()
            if (verification_layout.verificationCodeText.isNotEmpty()) {
                mViewModel.setIsLoading(true)
                integration.verifyFCMPhoneNumberWithCode(verification_layout.verificationCodeText)
            }
        })

        mViewModel.resendCodeEvent.observe(this, Observer {
            mViewModel.setIsLoading(true)
            verification_layout.invalidateDigits()
            mViewModel.isValidVerify.set(true)
            integration.reSendFCMSms()
        })

        mViewModel.registerResponse.observe(this, Observer {
            openAddAddress()
        })

        mViewModel.completeProfileResponse.observe(this, Observer {
            openAddAddress()
        })
    }

    override fun openAddAddress() {
        startActivity(Intent(this, AddAddressActivity::class.java))
        finish()
    }

    override fun openResetPassword(idToken: String?) {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra(Constants.EXTRA_PHONE, mViewModel.phoneNumber)
        intent.putExtra(Constants.EXTRA_AUTH_TOKEN, idToken)
        intent.putExtra(
            Constants.IS_DELIVERY,
            intent.getBooleanExtra(Constants.IS_DELIVERY, false)
        )
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        sendSms()
        subscribeToLiveData()
    }

    private fun sendSms() {
        mViewModel.setIsLoading(true)
        integration =
            VerificationIntegration
                .Builder(this)
                .setIsFirebase(true)
                .setCountryCode("+966")
                .setFCMCallBack(this)
                .setMobileNumber(mViewModel.phoneNumber)
                .build()

        integration.sendFCMSms()
    }

    override fun onFCMCodeSent() {
        mViewModel.setIsLoading(false)
    }

    override fun onFCMError(error: String) {
        mViewModel.setIsLoading(false)
        showPopUp("", error, getString(android.R.string.ok), false)
    }

    override fun onFCMVerificationCodeError() {
        mViewModel.setIsLoading(false)
        mViewModel.isValidVerify.set(false)
    }

    override fun onFCMVerifiedSuccess(idToken: String?) {
        mViewModel.setIsLoading(false)
        takeAction(idToken)
    }

    private fun getIntentsData() {
        mViewModel.registerRequest = Gson().fromJson(
            intent.getStringExtra(Constants.EXTRA_REGISTER_REQUEST)!!,
            RegisterRequest::class.java
        )

        mViewModel.completeProfileRequest = Gson().fromJson(
            intent.getStringExtra(Constants.EXTRA_COMPLETE_PROFILE_REQUEST)!!,
            CompleteProfileRequest::class.java
        )
        mViewModel.phoneNumber = intent.getStringExtra(Constants.EXTRA_PHONE)!!

        if (mViewModel.registerRequest != null)
            mViewModel.phoneNumber = mViewModel.registerRequest?.phone!!
        else if (mViewModel.completeProfileRequest != null)
            mViewModel.phoneNumber = mViewModel.completeProfileRequest?.phone!!

        mViewModel.fromWhere = intent.getIntExtra(Constants.FROM_WHERE, -1)
    }


    private fun takeAction(idToken: String?) {
        when (mViewModel.fromWhere) {
            Constants.REGISTER_SCREEN -> mViewModel.register()
            Constants.COMPLETE_PROFILE_SCREEN -> mViewModel.completeProfile()
            Constants.FORGET_PASSWORD_SCREEN -> openResetPassword(idToken)
        }
    }
}
