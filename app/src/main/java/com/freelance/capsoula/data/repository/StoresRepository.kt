package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.responses.StoresResponse
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoresRepository : BaseRepository() {

    val storesResponse = SingleLiveEvent<BaseResponse<StoresResponse>>()

    suspend fun getStores(pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getStores(pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    storesResponse.postValue(response.body()!!)
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
                        getStores(pageNo)
                    }
                })
            }
        }
    }
}