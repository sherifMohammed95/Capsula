package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.Cart
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRepository : BaseRepository() {

    val cartResponse = SingleLiveEvent<ArrayList<Product>>()
    val deleteCartResponse = SingleLiveEvent<Void>()

    suspend fun addProductsToCart(cartItems:ArrayList<Cart>) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }
            val response = webService.addProductsToCart(cartItems)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    cartResponse.postValue(response.body()?.data?.productsList)
                }
            } else {
                withContext(Dispatchers.Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        addProductsToCart(cartItems)
                    }
                })
            }
        }
    }

    suspend fun deleteProductFromCart(mainId:Int) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.deleteProductFromCart(mainId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    cartResponse.postValue(response.body()?.data?.productsList)
                }
            } else {
                withContext(Dispatchers.Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteProductFromCart(mainId)
                    }
                })
            }
        }
    }

    suspend fun deleteCart() {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.deleteCart()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    deleteCartResponse.call()
                }
            } else {
                withContext(Dispatchers.Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteCart()
                    }
                })
            }
        }
    }

    suspend fun updateCart(cart: Cart) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.updateCart(cart)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    cartResponse.postValue(response.body()?.data?.productsList)
                }
            } else {
                withContext(Dispatchers.Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        updateCart(cart)
                    }
                })
            }
        }
    }
}