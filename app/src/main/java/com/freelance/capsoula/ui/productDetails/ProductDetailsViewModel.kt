package com.freelance.capsoula.ui.productDetails

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Cart
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.ProductsRepository
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModel(val mRepository: ProductsRepository) :
    BaseViewModel<ProductDetailsNavigator>() {

    var product = Product()
    var fromCart = ObservableBoolean(false)
    var cartResponse = SingleLiveEvent<ArrayList<Product>>()
    var productaddedMsg = SingleLiveEvent<Void>()

    init {
        initRepository(mRepository)
        this.cartResponse = mRepository.cartResponse
    }

    fun addToCartAction() {
        if (UserDataSource.getUser() == null){
            UserDataSource.addProductToCart(product)
            productaddedMsg.call()
        }
        else{
            if(UserDataSource.checkProductExistInCart(UserDataSource.getUser()?.cartContent!!,
                    product)) {
                navigator?.openCheckout()
            } else
                addProductToCart()
        }
    }

   private fun addProductToCart() {
        val cart = Cart()
        cart.mainId = product.mainId
        cart.quantity = 1
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCart(cart)
        }
    }
}