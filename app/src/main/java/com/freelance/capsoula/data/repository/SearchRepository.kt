package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchRepository : BaseRepository() {

    val searchResultsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()

    suspend fun getSearchResults(searchText: String, filterType: Int, pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }

            val response =
                webService.getSearchResults(searchText, filterType, pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    searchResultsResponse.postValue(response.body()!!)
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
                        getSearchResults(searchText,filterType,pageNo)
                    }
                })
            }
        }
    }
}