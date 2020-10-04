package com.blueMarketing.capsula.ui.checkout.fragment.cart

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.data.requests.CartRequest
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.repository.CartRepository
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.math.round

class CartViewModel(val repo: CartRepository) : BaseViewModel<CartNavigator>() {

    var itemsNumber = ObservableField<String>("")
    var totalPriceText = ObservableField<String>("")
    var cartResponse = SingleLiveEvent<ArrayList<Product>>()
    var deleteCartResponse = SingleLiveEvent<Void>()
    var emptyCartEvent = SingleLiveEvent<Void>()
    var deleteCartEvent = SingleLiveEvent<Void>()
    var validCart = SingleLiveEvent<Void>()
    var cartList = ArrayList<Product>()
    private var cartItems = ArrayList<CartRequest>()
    var mProduct = Product()
    var mOrder: Order? = null
    var differentProductMsg = SingleLiveEvent<String>()

    init {
        initRepository(repo)
        this.cartResponse = repo.cartResponse
        this.deleteCartResponse = repo.deleteCartResponse
        this.validCart = repo.validCart
    }


    fun deleteAllAction() {
        if (!cartList.isNullOrEmpty())
            deleteCartEvent.call()
    }

    fun nextAction() {
        if (UserDataSource.getUser() != null) {
            if (!cartList.isNullOrEmpty()) {
                validateCart()
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
            if (UserDataSource.getUser()?.cartContent?.size!! > 0) {
                cartList = UserDataSource.getUser()?.cartContent!!
            } else {
                if (UserDataSource.getUserCart().size > 0) {
                    cartList = UserDataSource.getUserCart()
                }
            }
            UserDataSource.deleteCart()
//            if (UserDataSource.getUserCartSize() > 0) {
//                if (UserDataSource.getUser()?.cartContent!!.size > 0) {
//                    combineLocalCartWithUserCart()
//                } else
//                    cartList = UserDataSource.getUserCart()
////                addProductsToCart()
//            } else {
//                cartList = UserDataSource.getUser()?.cartContent!!
////                updateDataView()
//            }

            if (mOrder != null) {
                val storeName = mOrder!!.products?.get(0)?.let {
                    UserDataSource.checkCartFromTheSameStore(it)
                }
                if (storeName != null && storeName.contentEquals(""))
                    combineOrderProductsWithUserCart()
                else {
                    differentProductMsg.value = storeName
                    return
                }
            }
            addProductsToCart()
        }
    }

    private fun combineOrderProductsWithUserCart() {
        val productsTemp = ArrayList<Product>()

        mOrder?.products?.forEachIndexed { index, product ->
            val prod =
                cartList.find { it.mainId == product.mainId }
            if (prod != null) {
                val quantity = prod.quantity + product.quantity
                cartList[index].quantity = quantity
            } else
                productsTemp.add(product)
        }

        if (productsTemp.size > 0)
            cartList.addAll(productsTemp)
    }

    private fun combineLocalCartWithUserCart() {
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
                val cart = CartRequest()
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

    private fun validateCart() {
        viewModelScope.launch(IO) {
            repo.validateCart()
        }
    }

    fun updateCart(actionType: Int) {
        val cart = CartRequest()
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
                it.price

            total += (it.quantity * prodPrice)
        }

        total = round(total * 100) / 100
        totalPriceText.set(
            Domain.application.getString(R.string.total) + " " +
                    Domain.application.getString(R.string.rsd) + " " + total
        )
    }
}