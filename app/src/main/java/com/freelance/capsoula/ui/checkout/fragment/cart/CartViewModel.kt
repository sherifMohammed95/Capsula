package com.freelance.capsoula.ui.checkout.fragment.cart

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Cart
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.CartRepository
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CartViewModel(val repo: CartRepository) : BaseViewModel<CartNavigator>() {

    var itemsNumber = ObservableField<String>("")
    var totalPriceText = ObservableField<String>("")
    var cartResponse = SingleLiveEvent<ArrayList<Product>>()
    var deleteCartResponse = SingleLiveEvent<Void>()
    var emptyCartEvent = SingleLiveEvent<Void>()
    var deleteCartEvent = SingleLiveEvent<Void>()
    var cartList = ArrayList<Product>()
    private var cartItems = ArrayList<Cart>()
    var mProduct = Product()

    init {
        initRepository(repo)
        this.cartResponse = repo.cartResponse
        this.deleteCartResponse = repo.deleteCartResponse
    }


    fun deleteAllAction() {
        if (!cartList.isNullOrEmpty())
            deleteCartEvent.call()
    }

    fun nextAction() {
        if (UserDataSource.getUser() != null) {
            if (!cartList.isNullOrEmpty()) {
                navigator?.openDetailsStep()
            } else {
                emptyCartEvent.call()
            }
        } else
            navigator?.openAuthentication()
    }

    fun fetchCartList() {
        if (UserDataSource.getUser() == null) {
            cartList = UserDataSource.getUserCart()
//            updateDataView()
        } else {
            if (UserDataSource.getUserCartSize() > 0) {
                if (UserDataSource.getUser()?.cartContent!!.size > 0) {
                    addLocalCartToUserCart()
                } else
                    cartList = UserDataSource.getUserCart()
                addProductsToCart()
            } else {
                cartList = UserDataSource.getUser()?.cartContent!!
//                updateDataView()
            }
        }
    }

    private fun addLocalCartToUserCart() {
        if (UserDataSource.getUser()?.cartContent!!.size > UserDataSource.getUserCartSize()) {
            UserDataSource.getUser()?.cartContent!!.forEach { product ->
                val localProd =
                    UserDataSource.getUserCart().find { it.mainId == product.mainId }
                if (localProd != null) {
                    val quantity = localProd.quantity + product.quantity
                    localProd.quantity = quantity
                    cartList.add(localProd)

                } else
                    cartList.add(product)
            }
        } else {
            UserDataSource.getUserCart().forEach { localProduct ->
                val prod =
                    UserDataSource.getUser()?.cartContent!!.find { it.mainId == localProduct.mainId }
                if (prod != null) {
                    val quantity = prod.quantity + localProduct.quantity
                    prod.quantity = quantity
                    cartList.add(prod)

                } else
                    cartList.add(localProduct)
            }
        }
    }

    private fun buildCartItems() {
        cartItems = ArrayList()
        if (!cartList.isNullOrEmpty()) {
            cartList.forEach {
                val cart = Cart()
                cart.mainId = it.mainId
                cart.quantity = it.quantity
                cartItems.add(cart)
            }
        }
    }

    fun addProductsToCart() {
        buildCartItems()
        viewModelScope.launch(IO) {
            repo.addProductsToCart(cartItems)
        }
    }

    fun deleteCart() {
        viewModelScope.launch(IO) {
            repo.deleteCart()
        }
    }

    fun updateCart(actionType: Int) {
        val cart = Cart()
        cart.mainId = mProduct.mainId
        if (actionType == 1)
            cart.quantity = mProduct.quantity + 1
        else
            cart.quantity = mProduct.quantity - 1

        viewModelScope.launch(IO) {
            repo.updateCart(cart)
        }
    }

    fun deleteProductFromCart() {
        viewModelScope.launch(IO) {
            repo.deleteProductFromCart(mProduct.mainId)
        }
    }

    fun calcTotalPrice() {
        var total = 0.0
        var prodPrice: Double
        cartList.forEach {
            prodPrice = if (it.offerType == Constants.DISCOUNT_OFFER)
                it.priceInOffer!!.toDouble()
            else
                it.price.toDouble()

            total += (it.quantity * prodPrice)
        }

        totalPriceText.set(
            Domain.application.getString(R.string.total) + " " +
                    Domain.application.getString(R.string.rsd) + " " + total
        )
    }
}