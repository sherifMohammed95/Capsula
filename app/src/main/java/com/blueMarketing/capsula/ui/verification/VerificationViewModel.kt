package com.blueMarketing.capsula.ui.verification

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.AuthenticationRepository
import com.blueMarketing.capsula.data.requests.CompleteProfileRequest
import com.blueMarketing.capsula.data.requests.RegisterRequest
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rx.functions.Action1

class VerificationViewModel(private val authRepo: AuthenticationRepository) :
    BaseViewModel<VerificationNavigator>() {

    val textCode = arrayOfNulls<ObservableField<String>>(6)
    var enteredCode = ""
    var isValidVerify = ObservableBoolean(true)
    var fromWhere = -1
    var verificationId: String? = null
    var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    var verifyPhoneWithCodeEvent = SingleLiveEvent<Void>()
    var resendCodeEvent = SingleLiveEvent<Void>()

    var registerResponse = SingleLiveEvent<Void>()
    var completeProfileResponse = SingleLiveEvent<Void>()
    var registerRequest: RegisterRequest? = null
    var completeProfileRequest: CompleteProfileRequest? = null

    var phoneNumber = ""
    var isDelivery = false

    init {
        initRepository(authRepo)
        for (i in 0..5) {
            textCode[i] = ObservableField("")
        }

        this.registerResponse = authRepo.registerResponse
        this.completeProfileResponse = authRepo.completeProfileResponse
    }


    fun onSubmitClick() {
        verifyPhoneWithCodeEvent.call()
    }

    fun onSendAgainClick() {
        resendCodeEvent.call()
    }

    val onTextChanged: Action1<String> = Action1 {
        var tempStr = it
        enteredCode = it
        isValidVerify.set(true)
        while (tempStr.length < 6) {
            tempStr += " "
        }
        for (i in 0..5) {
            textCode[i]?.set((tempStr[i].toString()))
        }
    }

    fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepo.register(registerRequest!!)
        }
    }

    fun completeProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepo.completeProfile(completeProfileRequest!!)
        }
    }
}