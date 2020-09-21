package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication

import androidx.databinding.ObservableBoolean
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.requests.DeliveryRegisterRequest

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