package com.freelance.capsoula.ui.productDetails

import android.content.DialogInterface
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.requests.CartRequest
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.ProductsRepository
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.preferencesGateway
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModel(val mRepository: ProductsRepository) :
    BaseViewModel<ProductDetailsNavigator>() {

    var product = Product()
    var fromCart = ObservableBoolean(false)
    var cartResponse = SingleLiveEvent<ArrayList<Product>>()
    var productaddedMsg = SingleLiveEvent<Void>()
    var differentProductMsg = SingleLiveEvent<String>()

    init {
        initRepository(mRepository)
        this.cartResponse = mRepository.cartResponse
    }

    fun addToCartAction() {
        val storeName = UserDataSource.checkCartFromTheSameStore(product)
        if (UserDataSource.getUser() == null) {
            if (storeName.contentEquals("")) {
                val userCart: ArrayList<Product>
                val jsonString = preferencesGateway.load(Constants.USER_CART, "")
                if (!jsonString.isNullOrEmpty()) {
                    userCart = Gson().fromJson(jsonString, UserDataSource.productListType)
                    if (!UserDataSource.checkProductExistInCart(userCart, product))
                        productaddedMsg.call()
                    UserDataSource.addProductToCart(product)
                }
            } else {
                differentProductMsg.value = storeName
            }

        } else {
            if (UserDataSource.checkProductExistInCart(
                    UserDataSource.getUser()?.cartContent!!,
                    product
                )
            ) {
                navigator?.openCheckout()
            } else {
                if (storeName.contentEquals(""))
                    addProductToCart()
                else {
                    differentProductMsg.value = storeName
                }
            }
        }
    }

    fun addProductToCart() {
        val cart = CartRequest()
        cart.mainId = product.mainId
        cart.quantity = 1
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCart(cart)
        }
    }
}