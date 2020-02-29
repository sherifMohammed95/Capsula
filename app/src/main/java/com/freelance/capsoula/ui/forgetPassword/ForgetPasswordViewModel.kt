package com.freelance.capsoula.ui.forgetPassword

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.AuthenticationRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private val authRepo: AuthenticationRepository) :
    BaseViewModel<ForgetPasswordNavigator>() {

    var phoneText = ObservableField<String>("")
    var phoneError = ObservableBoolean(false)
    var checkUserExistResponse = SingleLiveEvent<Void>()

    init {
        initRepository(authRepo)
        phoneText.addCallback {
            phoneError.set(false)
        }
        this.checkUserExistResponse = authRepo.checkUserExistResponse
    }

    fun onSendClick() {
        if (!ValidationUtils.isValidSaudiMobile(phoneText.get()!!)) {
            phoneError.set(true)
            return
        }
        checkUserExist()
    }

    private fun checkUserExist() {
        viewModelScope.launch(IO) {
            authRepo.checkUserExist(phoneText.get()!!)
        }
    }

}