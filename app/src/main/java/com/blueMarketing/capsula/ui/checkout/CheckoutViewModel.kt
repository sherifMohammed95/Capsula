package com.blueMarketing.capsula.ui.checkout

import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.Order

class CheckoutViewModel : BaseViewModel<CheckoutNavigator>() {

    var fromWhere = -1
    var mOrder: Order? = null
}