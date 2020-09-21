package com.blueMarketing.capsula.ui.forgetPassword

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.AuthenticationRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
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