package com.freelance.capsoula.data.repository

import com.freelance.capsoula.data.requests.CartRequest
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRepository : BaseRepository() {

    val cartResponse = SingleLiveEvent<ArrayList<Product>>()
    val deleteCartResponse = SingleLiveEvent<Void>()
    val validCart = SingleLiveEvent<Void>()

    suspend fun addProductsToCart(cartRequestItems:ArrayList<CartRequest>) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }
            val response = webService.addProductsToCart(cartRequestItems)
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
                        addProductsToCart(cartRequestItems)
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

    suspend fun updateCart(cartRequest: CartRequest) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.updateCart(cartRequest)
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
                        updateCart(cartRequest)
                    }
                })
            }
        }
    }


    suspend fun validateCart() {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.validateCart()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    validCart.call()
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
                        validateCart()
                    }
                })
            }
        }
    }
}