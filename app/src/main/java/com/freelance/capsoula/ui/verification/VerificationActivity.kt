package com.freelance.capsoula.ui.verification

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bluecrunch.bluecrunchverification.FCMCallBack
import com.bluecrunch.bluecrunchverification.VerificationIntegration
import com.bluecrunch.bluecrunchverification.WebServiceCallBack
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.requests.CompleteProfileRequest
import com.freelance.capsoula.data.requests.RegisterRequest
import com.freelance.capsoula.databinding.ActivityVerificationBinding
import com.freelance.capsoula.ui.addAddress.AddAddressActivity
import com.freelance.capsoula.ui.resetPassword.ResetPasswordActivity
import com.freelance.capsoula.utils.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_verification.*
import okhttp3.ResponseBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

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

    override fun openResetPassword() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra(Constants.EXTRA_PHONE, mViewModel.phoneNumber)
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
        takeAction()
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

//    private fun verifyPhoneNumberWithCode() {
//        val credential = PhoneAuthProvider
//            .getCredential(mViewModel.verificationId!!, mViewModel.enteredCode)
//        mViewModel.setIsLoading(true)
//        signInWithPhoneAuthCredential(credential)
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
//            mViewModel.setIsLoading(false)
//            if (task.isSuccessful) {
//                mAuth?.signOut()
//                takeAction()
//            } else {
//                mViewModel.isValidVerify.set(false)
//            }
//        }
//    }

//    private fun sendSms() {
//        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                signInWithPhoneAuthCredential(phoneAuthCredential)
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                mViewModel.setIsLoading(false)
//                showPopUp("", e.message!!, getString(android.R.string.ok), false)
//            }
//
//            override fun onCodeSent(
//                id: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken
//            ) {
//                super.onCodeSent(id, forceResendingToken)
//                mViewModel.setIsLoading(false)
//                mViewModel.verificationId = id
//                mViewModel.mResendToken = forceResendingToken
//            }
//        }
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//            "+966" + mViewModel.phoneNumber,
//            60,
//            TimeUnit.SECONDS,
//            this,
//            mCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks
//        )
//
//        mViewModel.setIsLoading(true)
//    }
//
//    private fun reSendSms() {
//        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                mViewModel.setIsLoading(false)
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                mViewModel.setIsLoading(false)
//                showPopUp("", e.message!!, getString(android.R.string.ok), false)
//            }
//
//            override fun onCodeSent(
//                id: String,
//                forceResendingToken: PhoneAuthProvider.ForceResendingToken
//            ) {
//                super.onCodeSent(id, forceResendingToken)
//                mViewModel.setIsLoading(false)
//                mViewModel.verificationId = id
//                mViewModel.mResendToken = forceResendingToken
//                //                showTimer();
//            }
//        }
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//            "+966" + mViewModel.phoneNumber,
//            60,
//            TimeUnit.SECONDS,
//            this,
//            mCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks,
//            mViewModel.mResendToken
//        )
//
//        mViewModel.setIsLoading(true)
//    }

    private fun takeAction() {
        when {
            mViewModel.fromWhere == Constants.REGISTER_SCREEN -> mViewModel.register()
            mViewModel.fromWhere == Constants.COMPLETE_PROFILE_SCREEN -> mViewModel.completeProfile()
            mViewModel.fromWhere == Constants.FORGET_PASSWORD_SCREEN -> openResetPassword()
        }
    }
}
