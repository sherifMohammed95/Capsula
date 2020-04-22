package com.freelance.capsoula.ui.checkout

import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Order

class CheckoutViewModel : BaseViewModel<CheckoutNavigator>() {

    var fromWhere = -1
    var mOrder: Order? = null
}