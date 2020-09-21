package com.blueMarketing.capsula.ui.completeProfile

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.repository.UserRepository
import com.blueMarketing.capsula.data.requests.CompleteProfileRequest
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback

class CompleteProfileViewModel(private val mRepository: UserRepository) :
    BaseViewModel<CompleteProfileNavigator>() {

    var nameText = ObservableField<String>("")
    var emailText = ObservableField<String>("")
    var phoneText = ObservableField<String>("")
    var nameErrorText = ObservableField<String>("")

    var nameError = ObservableBoolean(false)
    var emailError = ObservableBoolean(false)
    var phoneError = ObservableBoolean(false)

    var termsAndConditions = ObservableBoolean(false)
    var termsError = ObservableBoolean(false)

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

    fun termsAction() {
        termsAndConditions.set(!termsAndConditions.get())
        termsError.set(false)
    }

    fun onTermsClick() {
        navigator!!.openTerms()
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

        if (!termsAndConditions.get()) {
            isValid = false
            termsError.set(true)
        }

        return isValid
    }

    private fun buildRequest() {
        request.email = emailText.get()!!
        request.name = nameText.get()!!
        request.phone = phoneText.get()!!
    }
}