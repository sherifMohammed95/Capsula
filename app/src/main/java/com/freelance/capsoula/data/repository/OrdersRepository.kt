package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.data.responses.OrdersResponse
import com.freelance.capsoula.data.responses.StoresResponse
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersRepository : BaseRepository() {

    val ordersResponse = SingleLiveEvent<BaseResponse<OrdersResponse>>()
    val orderDetailsResponse = SingleLiveEvent<BaseResponse<Order>>()

    suspend fun getOrders(showLoading:Boolean) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = showLoading
            }

            val response = webService.getOrders()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    ordersResponse.postValue(response.body()!!)
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
                        getOrders(showLoading)
                    }
                })
            }
        }
    }

    suspend fun getOrderDetails(orderId:Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getOrderDetails(orderId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    orderDetailsResponse.postValue(response.body()!!)
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
                        getOrderDetails(orderId)
                    }
                })
            }
        }
    }
}