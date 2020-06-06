package com.freelance.capsoula.ui.userTypes

import androidx.databinding.ObservableBoolean
import com.freelance.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserTypesViewModel : BaseViewModel<UserTypesNavigator>() {

    var customerType = ObservableBoolean(false)
    var deliveryType = ObservableBoolean(false)


    fun customerTypeAction() {
        customerType.set(true)
        CoroutineScope(Main).launch {
            start()
        }
    }

    fun deliveryTypeAction() {
        deliveryType.set(true)
        CoroutineScope(Main).launch {
            start()
        }
    }

    private suspend fun start() {
        delay(100)

        if (customerType.get())
            navigator?.openCustomerAuthentication()
        else
            navigator?.openDeliveryAuthentication()

    }
}