package com.freelance.capsoula.ui.resetPassword

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.repository.AuthenticationRepository
import com.freelance.capsoula.data.requests.ChangePasswordRequest
import com.freelance.capsoula.data.requests.ResetPasswordRequest
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ResetPasswordViewModel(private val authRepo: AuthenticationRepository) :
    BaseViewModel<ResetPasswordNavigator>() {

    var passwordText = ObservableField<String>("")
    var currentPasswordText = ObservableField<String>("")
    var confirmPasswordText = ObservableField<String>("")
    var passwordErrorText = ObservableField<String>("")
    var currentPasswordErrorText = ObservableField<String>("")
    var confirmPasswordErrorText = ObservableField<String>("")

    var passwordError = ObservableBoolean(false)
    var currentPasswordError = ObservableBoolean(false)
    var confirmPasswordError = ObservableBoolean(false)

    var fromChangePassword = ObservableBoolean(false)

    var phoneNumber = ""
    var resetPasswordResponse = SingleLiveEvent<Void>()
    var changePasswordResponse = SingleLiveEvent<String>()

    init {
        initRepository(authRepo)
        passwordText.addCallback {
            passwordError.set(false)
        }

        currentPasswordText.addCallback {
            currentPasswordError.set(false)
        }


        confirmPasswordText.addCallback {
            confirmPasswordError.set(false)
        }
        this.resetPasswordResponse = authRepo.resetPasswordResponse
        this.changePasswordResponse = authRepo.changePasswordResponse
    }

    fun onResetClick() {
        if (!validate())
            return

        if (fromChangePassword.get())
            changePassword()
        else
            resetPassword()
    }

    private fun validate(): Boolean {

        var isValid = true

        if (fromChangePassword.get()) {
            if (!ValidationUtils.isValidText(currentPasswordText.get())) {
                isValid = false
                currentPasswordErrorText.set(Domain.application.getString(R.string.please_enter_a_valid_password))
                currentPasswordError.set(true)
            } else if (!ValidationUtils.isValidPassword(currentPasswordText.get())) {
                isValid = false
                currentPasswordErrorText.set(Domain.application.getString(R.string.password_should_be))
                currentPasswordError.set(true)
            }
        }

        if (!ValidationUtils.isValidText(passwordText.get())) {
            isValid = false
            passwordErrorText.set(Domain.application.getString(R.string.please_enter_a_valid_password))
            passwordError.set(true)
        } else if (!ValidationUtils.isValidPassword(passwordText.get())) {
            isValid = false
            passwordErrorText.set(Domain.application.getString(R.string.password_should_be))
            passwordError.set(true)
        }

        if (!ValidationUtils.isValidText(confirmPasswordText.get())) {
            isValid = false
            confirmPasswordErrorText.set(Domain.application.getString(R.string.please_enter_a_valid_password))
            confirmPasswordError.set(true)
        } else if (!confirmPasswordText.get()!!.contentEquals(passwordText.get()!!)) {
            isValid = false
            confirmPasswordErrorText.set(Domain.application.getString(R.string.password_not_match))
            confirmPasswordError.set(true)
        }

        return isValid
    }

    private fun resetPassword() {
        viewModelScope.launch(IO) {
            val request = ResetPasswordRequest()
            request.newPassword = passwordText.get()!!
            request.phoneNumber = phoneNumber
            authRepo.resetPassword(request)
        }
    }

    private fun changePassword() {
        viewModelScope.launch(IO) {
            val request = ChangePasswordRequest()
            request.newPassword = passwordText.get()!!
            request.currentPassword = currentPasswordText.get()!!
            authRepo.changePassword(request)
        }
    }
}