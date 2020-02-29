package com.freelance.capsoula.data.repository

import com.freelance.capsoula.data.requests.AddAddressRequest
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
}