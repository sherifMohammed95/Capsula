package com.freelance.capsoula.ui.checkout.fragment.cart

import androidx.databinding.ObservableField
import com.freelance.base.BaseViewModel

class CartViewModel : BaseViewModel<CartNavigator>() {

    var itemsNumber = ObservableField<String>("")
}