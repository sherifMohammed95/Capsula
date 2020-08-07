package com.freelance.capsoula.data.repository

import com.freelance.capsoula.data.requests.AddAddressRequest
import com.freelance.capsoula.data.requests.CheckoutDetailsRequest
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository : BaseRepository() {

    val addAddressResponse = SingleLiveEvent<Void>()
    val updateDefaultAddressResponse = SingleLiveEvent<Void>()
    val successEvent = SingleLiveEvent<Void>()
    val deliveryCostResponse = SingleLiveEvent<Double>()
    val checkoutIdResponse = SingleLiveEvent<String>()

    suspend fun addAddress(addressText: String, lat: Double, lng: Double) {

        try {
            withContext(Main) {

                progressLoading.value = true
            }
            val request = AddAddressRequest()
            request.addressDesc = addressText
            request.latitude = lat
            request.longitude = lng
            val response = webService.addAddress(request)
            if (response.isSuccessful) {
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                withContext(Main) {
                    progressLoading.value = false
                    addAddressResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        addAddress(addressText, lat, lng)
                    }
                })
            }
        }
    }

    suspend fun updateDefaultAddress(addressId: Int) {

        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val response = webService.updateDefaultAddress(addressId)
            if (response.isSuccessful) {
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                withContext(Main) {
                    progressLoading.value = false
                    updateDefaultAddressResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        updateDefaultAddress(addressId)
                    }
                })
            }
        }
    }

    suspend fun submitCheckoutDetails(request: CheckoutDetailsRequest) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.submitCheckoutDetails(request)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    successEvent.call()
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
                        submitCheckoutDetails(request)
                    }
                })
            }
        }
    }

    suspend fun getDeliveryCost() {

        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val response = webService.getDeliveryCost()
            if (response.isSuccessful) {
                withContext(Main) {
                    progressLoading.value = false
                }
                deliveryCostResponse.postValue(response.body()!!.data!!)

            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        getDeliveryCost()
                    }
                })
            }
        }
    }

    suspend fun prepareCheckout(paymentMethod: Int) {

        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val response = webService.prepareCheckout(paymentMethod)
            if (response.isSuccessful) {
                withContext(Main) {
                    progressLoading.value = false
                }
                checkoutIdResponse.postValue(response.body()!!.data!!)

            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(Dispatchers.IO).launch {
                        prepareCheckout(paymentMethod)
                    }
                })
            }
        }
    }
}