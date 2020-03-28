package com.freelance.capsoula.ui.completeProfile

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.repository.UserRepository
import com.freelance.capsoula.data.requests.CompleteProfileRequest
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback

class CompleteProfileViewModel(private val mRepository: UserRepository) :
    BaseViewModel<CompleteProfileNavigator>() {

    var nameText = ObservableField<String>("")
    var emailText = ObservableField<String>("")
    var phoneText = ObservableField<String>("")
    var nameErrorText = ObservableField<String>("")

    var nameError = ObservableBoolean(false)
    var emailError = ObservableBoolean(false)
    var phoneError = ObservableBoolean(false)
    val request = CompleteProfileRequest()

    init {
        initRepository(mRepository)
        fillDataView()

        nameText.addCallback {
            nameError.set(false)
        }

        emailText.addCallback {
            emailError.set(false)
        }

        phoneText.addCallback {
            phoneError.set(false)
        }
    }

    private fun fillDataView() {
        val user = UserDataSource.getUser()
        if (!user!!.phone.isNullOrEmpty())
            phoneText.set(user.phone)

        if (!user.email.isNullOrEmpty())
            emailText.set(user.email)

        if (!user.name.isNullOrEmpty())
            nameText.set(user.name)
    }

    fun onSaveClick() {
        if (!validate())
            return
        buildRequest()
        navigator!!.openVerification()
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!ValidationUtils.isValidText(nameText.get())) {
            isValid = false
            nameErrorText.set(Domain.application.getString(R.string.please_enter_a_valid_name))
            nameError.set(true)
        } else if (!ValidationUtils.isValidName(nameText.get())) {
            isValid = false
            nameErrorText.set(Domain.application.getString(R.string.name_should_be))
            nameError.set(true)
        }

        if (!ValidationUtils.isValidEmail(emailText.get()!!.toLowerCase())) {
            isValid = false
            emailError.set(true)
        }

        if (!ValidationUtils.isValidSaudiMobile(phoneText.get()!!)) {
            isValid = false
            phoneError.set(true)
        }

        return isValid
    }

    private fun buildRequest() {
        request.email = emailText.get()!!
        request.name = nameText.get()!!
        request.phone = phoneText.get()!!
    }
}