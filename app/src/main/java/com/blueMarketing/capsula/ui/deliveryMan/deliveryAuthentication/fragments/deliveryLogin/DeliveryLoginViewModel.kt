package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.AuthenticationRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeliveryLoginViewModel(val mRepository: AuthenticationRepository) :
    BaseViewModel<DeliveryLoginNavigator>() {


    var loginResponse = SingleLiveEvent<Void>()
    var passwordText = ObservableField<String>("")
    var phoneOrEmailText = ObservableField<String>("")

    var passwordError = ObservableBoolean(false)
    var phoneOrEmailError = ObservableBoolean(false)


    init {
        initRepository(mRepository)
        this.loginResponse = mRepository.loginResponse

        passwordText.addCallback {
            passwordError.set(false)
        }

        phoneOrEmailText.addCallback {
            phoneOrEmailError.set(false)
        }
    }


    fun onLoginButtonClick() {
        if (!validateLogin()) return
        login()
    }

    fun onForgetPasswordClick() {
        navigator!!.openForgetPassword()
    }

    private fun validateLogin(): Boolean {
        var isValid = true

        if (!ValidationUtils.isValidText(phoneOrEmailText.get())) {
            isValid = false
            phoneOrEmailError.set(true)
        }

        if (!ValidationUtils.isValidText(passwordText.get())) {
            isValid = false
            passwordError.set(true)
        }

        return isValid
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deliveryLogin(phoneOrEmailText.get()!!, passwordText.get()!!)
        }
    }

}