package com.freelance.capsoula.ui.authentication

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.repository.AuthenticationRepository
import com.freelance.capsoula.data.requests.RegisterRequest
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val mRepository: AuthenticationRepository) :
    BaseViewModel<AuthenticationNavigator>() {

    var loginResponse = SingleLiveEvent<Void>()
    var socialMediaResponse = SingleLiveEvent<Void>()


    var loginTab = ObservableBoolean(false)
    var registerTab = ObservableBoolean(true)

    var nameText = ObservableField<String>("")
    var emailText = ObservableField<String>("")
    var phoneText = ObservableField<String>("")
    var passwordText = ObservableField<String>("")
    var phoneOrEmailText = ObservableField<String>("")

    var passwordErrorText = ObservableField<String>("")
    var nameErrorText = ObservableField<String>("")

    var nameError = ObservableBoolean(false)
    var emailError = ObservableBoolean(false)
    var phoneError = ObservableBoolean(false)
    var passwordError = ObservableBoolean(false)
    var phoneOrEmailError = ObservableBoolean(false)
    var fbToken = ""
    val registerRequest = RegisterRequest()
    var googleSignInAccount: GoogleSignInAccount? = null

    var termsAndConditions = ObservableBoolean(false)
    var termsError = ObservableBoolean(false)

    var fromWhere = -1

    init {
        initRepository(mRepository)
        nameText.addCallback {
            nameError.set(false)
        }

        emailText.addCallback {
            emailError.set(false)
        }

        phoneText.addCallback {
            phoneError.set(false)
        }

        passwordText.addCallback {
            passwordError.set(false)
        }

        phoneOrEmailText.addCallback {
            phoneOrEmailError.set(false)
        }
        this.loginResponse = mRepository.loginResponse
        this.socialMediaResponse = mRepository.socialMediaResponse
    }

    fun onLoginTabClick() {
        loginTab.set(true)
        registerTab.set(false)
        resetRegisterFormData()
        navigator!!.openLogin()
    }

    fun onRegisterTabClick() {
        loginTab.set(false)
        registerTab.set(true)
        resetLoginFormData()
        navigator!!.openRegister()
    }

    fun onRegisterButtonClick() {
        if (!validateRegister()) return
        buildRegisterRequest()
        navigator!!.openVerification()
    }

    fun onLoginButtonClick() {
        if (!validateLogin()) return
        login()
    }

    fun onForgetPasswordClick() {
        navigator!!.openForgetPassword()
    }

    fun onGoogleClick() {
        navigator!!.signInWithGoogle()
    }

    fun onFaceBookClick() {
        navigator!!.signInWithFaceBook()
    }

    fun onTwitterClick() {
        navigator!!.signInWithTwitter()
    }

    fun onSkipClick() {
        navigator!!.openHome()
    }

    fun termsAction() {
        termsAndConditions.set(!termsAndConditions.get())
        termsError.set(false)
    }

    fun onTermsClick() {
        navigator!!.openTerms()
    }

    private fun validateRegister(): Boolean {
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

        if (!ValidationUtils.isValidText(passwordText.get())) {
            isValid = false
            passwordErrorText.set(Domain.application.getString(R.string.please_enter_a_valid_password))
            passwordError.set(true)
        } else if (!ValidationUtils.isValidPassword(passwordText.get())) {
            isValid = false
            passwordErrorText.set(Domain.application.getString(R.string.password_should_be))
            passwordError.set(true)
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

    private fun resetLoginFormData() {
        phoneOrEmailText.set("")
        passwordErrorText.set("")
        passwordText.set("")
        phoneOrEmailError.set(false)
        passwordError.set(false)
    }

    private fun resetRegisterFormData() {
        nameText.set("")
        emailText.set("")
        phoneText.set("")
        passwordText.set("")
        nameErrorText.set("")
        passwordErrorText.set("")
        nameError.set(false)
        emailError.set(false)
        phoneError.set(false)
        passwordError.set(false)
    }

    private fun login() {
        viewModelScope.launch(IO) {
            mRepository.login(phoneOrEmailText.get()!!, passwordText.get()!!)
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch(IO) {
            mRepository.loginWithGoogle(googleSignInAccount?.idToken!!)
        }
    }

    fun loginWithFaceBook() {
        viewModelScope.launch(IO) {
            mRepository.loginWithFacebook(fbToken)
        }
    }

    private fun buildRegisterRequest() {
        registerRequest.password = passwordText.get()!!
        registerRequest.name = nameText.get()!!
        registerRequest.email = emailText.get()!!
        registerRequest.phone = phoneText.get()!!
    }
}