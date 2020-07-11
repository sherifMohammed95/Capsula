package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.AuthenticationRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
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