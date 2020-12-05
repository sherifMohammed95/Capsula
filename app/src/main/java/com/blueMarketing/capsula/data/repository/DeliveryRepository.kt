package com.blueMarketing.capsula.data.repository

import android.util.Log
import com.blueMarketing.capsula.data.Wallet
import com.blueMarketing.capsula.data.requests.AddAddressRequest
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryRepository : BaseRepository() {

    val walletResponse = SingleLiveEvent<Wallet>()

    suspend fun getWallet() {

        try {
            withContext(Main) {

                showLoadingLayout.value = true
            }

            val response = webService.getWallet()
            if (response.isSuccessful) {
                withContext(Main) {
                    showLoadingLayout.value = false
                    walletResponse.postValue(response.body()!!.data!!)
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
                        getWallet()
                    }
                })
            }
        }
    }

    suspend fun updateDeliveryCurrentLocation(request: AddAddressRequest) {
        try {
            val response = webService.updateDeliveryCurrentLocation(request)
            if (response.isSuccessful) {
                withContext(Main) {
//                    Log.e("DELIVERY_LOCATION", "current location updated")
                }
            }
        } catch (e: Exception) {
        }
    }
}