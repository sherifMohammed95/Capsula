package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication

import androidx.databinding.ObservableBoolean
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.requests.DeliveryRegisterRequest

class DeliveryAuthenticationViewModel : BaseViewModel<DeliveryAuthenticationNavigator>() {

    var request = DeliveryRegisterRequest()
     var loginTab = ObservableBoolean(false)
     var registerTab = ObservableBoolean(true)


    fun onLoginTabClick() {
        loginTab.set(true)
        registerTab.set(false)
        navigator!!.openLogin()
    }

    fun onRegisterTabClick() {
        loginTab.set(false)
        registerTab.set(true)
        navigator!!.openPersonalDetails()
    }
}