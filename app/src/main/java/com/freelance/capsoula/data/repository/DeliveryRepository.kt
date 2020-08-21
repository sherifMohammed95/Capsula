package com.freelance.capsoula.data.repository

import com.freelance.capsoula.data.Wallet
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryRepository : BaseRepository() {

    val walletResponse = SingleLiveEvent<Wallet>()

    suspend fun getWallet() {

        try {
            withContext(Dispatchers.Main) {

                showLoadingLayout.value = true
            }

            val response = webService.getWallet()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    walletResponse.postValue(response.body()!!.data!!)
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
                        getWallet()
                    }
                })
            }
        }
    }

}