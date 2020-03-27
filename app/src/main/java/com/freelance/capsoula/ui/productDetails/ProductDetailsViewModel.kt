package com.freelance.capsoula.ui.productDetails

import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.source.local.UserDataSource

class ProductDetailsViewModel : BaseViewModel<ProductDetailsNavigator>() {

    var product = Product()

    fun addToCartAction() {
        if (UserDataSource.getUser() == null)
            UserDataSource.addProductToCart(product)
    }
}