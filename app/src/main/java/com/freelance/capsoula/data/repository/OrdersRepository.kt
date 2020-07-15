package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.data.responses.OrderTrackingResponse
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
    val orderTrackingResponse = SingleLiveEvent<BaseResponse<OrderTrackingResponse>>()
    val orderDetailsResponse = SingleLiveEvent<BaseResponse<Order>>()
    val deliveryOrderDetailsResponse = SingleLiveEvent<BaseResponse<DeliveryOrder>>()
    val cancelOrderResponse = SingleLiveEvent<String>()
    val startDeliveryResponse = SingleLiveEvent<Void>()
    val finishDeliveryResponse = SingleLiveEvent<Void>()

    suspend fun getOrders(showLoading: Boolean) {
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

    suspend fun getOrderDetails(orderId: Int) {
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

    suspend fun getDeliveryOrderDetails(orderId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getDeliveryOrderDetails(orderId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    deliveryOrderDetailsResponse.postValue(response.body()!!)
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
                        getDeliveryOrderDetails(orderId)
                    }
                })
            }
        }
    }

    suspend fun getOrderTracking(orderId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getOrderTracking(orderId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    orderTrackingResponse.postValue(response.body()!!)
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
                        getOrderTracking(orderId)
                    }
                })
            }
        }
    }


    suspend fun cancelOrder(orderId: Int) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }

            val response = webService.cancelOrder(orderId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    cancelOrderResponse.postValue(response.body()?.data!!)
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
                        cancelOrder(orderId)
                    }
                })
            }
        }
    }

    suspend fun startDelivery(orderId: Int) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }

            val response = webService.startDelivery(orderId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    startDeliveryResponse.call()
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
                        startDelivery(orderId)
                    }
                })
            }
        }
    }

    suspend fun finishDelivery(orderId: Int) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }

            val response = webService.endDelivery(orderId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    finishDeliveryResponse.call()
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
                        finishDelivery(orderId)
                    }
                })
            }
        }
    }
}