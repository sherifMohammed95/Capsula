package com.blueMarketing.capsula.data.source.local

import com.blueMarketing.capsula.data.DeliveryUser
import com.blueMarketing.capsula.data.MessageEvent
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.User
import com.blueMarketing.capsula.utils.Constants.DELIVERY_USER
import com.blueMarketing.capsula.utils.Constants.OPEN_CHECKOUT
import com.blueMarketing.capsula.utils.Constants.TOKEN
import com.blueMarketing.capsula.utils.Constants.UPDATE_CART_NUMBER
import com.blueMarketing.capsula.utils.Constants.USER
import com.blueMarketing.capsula.utils.Constants.USER_CART
import com.blueMarketing.capsula.utils.preferencesGateway
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus


class UserDataSource {
    companion object {

        val productListType = object : TypeToken<List<Product>>() {}.type

        fun saveUser(user: User?) {
            preferencesGateway.save(USER, Gson().toJson(user))
        }

        fun saveDeliveryUser(user: DeliveryUser?) {
            preferencesGateway.save(DELIVERY_USER, Gson().toJson(user))
        }


        fun getUser(): User? {
            return Gson().fromJson(
                preferencesGateway.load(USER, ""),
                User::class.java
            )
        }

        fun getDeliveryUser(): DeliveryUser? {
            return Gson().fromJson(
                preferencesGateway.load(DELIVERY_USER, ""),
                DeliveryUser::class.java
            )
        }

        fun saveUserToken(token: String) {
            preferencesGateway.save(TOKEN, token)
        }

        fun getToken(): String {
            return preferencesGateway.load(TOKEN, "")
        }

        fun getUserCart(): ArrayList<Product> {
            val jsonString = preferencesGateway.load(USER_CART, "")
            return Gson().fromJson(jsonString, productListType)
        }

        fun getUserCartSize(): Int {
            var userCart = ArrayList<Product>()
            val jsonString = preferencesGateway.load(USER_CART, "")
            if (!jsonString.isNullOrEmpty())
                userCart = Gson().fromJson(jsonString, productListType)
            return userCart.size
        }

        fun addProductToCart(product: Product) {
            product.quantity = 1
            var userCart = ArrayList<Product>()
            val jsonString = preferencesGateway.load(USER_CART, "")
            if (!jsonString.isNullOrEmpty()) {
                userCart = Gson().fromJson(jsonString, productListType)

                if (checkProductExistInCart(userCart, product)) {
                    EventBus.getDefault().postSticky(MessageEvent(OPEN_CHECKOUT))
                } else {
                    userCart.add(product)
                    preferencesGateway.save(USER_CART, Gson().toJson(userCart, productListType))
                    EventBus.getDefault().postSticky(MessageEvent(UPDATE_CART_NUMBER))
                }
            } else {
                userCart.add(product)
                preferencesGateway.save(USER_CART, Gson().toJson(userCart, productListType))
                EventBus.getDefault().postSticky(MessageEvent(UPDATE_CART_NUMBER))
            }
        }

        fun checkCartFromTheSameStore(product: Product): String {
            val userCart: ArrayList<Product>
            if (getUser() == null) {
                val jsonString = preferencesGateway.load(USER_CART, "")
                if (!jsonString.isNullOrEmpty()) {
                    userCart = Gson().fromJson(jsonString, productListType)
                    for (i in 0 until userCart.size) {
                        if (!userCart[i].storeName!!.contentEquals(product.storeName!!))
                            return userCart[i].storeName!!
                    }
                }
            } else {
                userCart = getUser()?.cartContent!!
                for (i in 0 until userCart.size) {
                    if (!userCart[i].storeName!!.contentEquals(product.storeName!!))
                        return userCart[i].storeName!!
                }
            }
            return ""
        }

        fun checkProductExistInCart(
            userCart: ArrayList<Product>,
            product: Product
        ): Boolean {
            return userCart.find { it.mainId == product.mainId } != null
        }

        fun increaseProductQuantity(product: Product) {
            val userCart = getUserCart()
            if (!userCart.isNullOrEmpty())
                run loop@{
                    userCart.forEach {
                        if (it.mainId == product.mainId) {
                            ++it.quantity
                            return@loop
                        }
                    }
                }
            preferencesGateway.save(USER_CART, Gson().toJson(userCart, productListType))
        }

        fun decreaseProductQuantity(product: Product) {
            val userCart = getUserCart()
            if (!userCart.isNullOrEmpty())
                run loop@{
                    userCart.forEach {
                        if (it.mainId == product.mainId) {
                            --it.quantity
                            return@loop
                        }
                    }
                }
            preferencesGateway.save(USER_CART, Gson().toJson(userCart, productListType))
        }

        fun deleteProduct(product: Product) {
            val userCart = getUserCart()
            if (!userCart.isNullOrEmpty())
                run loop@{
                    userCart.forEach {
                        if (it.mainId == product.mainId) {
                            userCart.remove(it)
                            return@loop
                        }
                    }
                }
            preferencesGateway.save(USER_CART, Gson().toJson(userCart, productListType))
        }

        fun deleteCart() {
            preferencesGateway.save(USER_CART, Gson().toJson(ArrayList<Product>(), productListType))
        }
    }
}