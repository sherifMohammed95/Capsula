package com.blueMarketing.capsula.data.repository

import com.blueMarketing.base.BaseResponse
import com.blueMarketing.capsula.data.requests.CartRequest
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.responses.ProductsResponse
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchRepository : BaseRepository() {

    val searchResultsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()
    val cartResponse = SingleLiveEvent<ArrayList<Product>>()

    suspend fun getSearchResults(searchText: String, filterType: Int, pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                if (pageNo == 1)
                    progressLoading.value = true
                else
                    isPagingLoadingEvent.value = true
            }

            val response =
                webService.getSearchResults(searchText, filterType, pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    if (pageNo == 1)
                        progressLoading.value = false
                    else
                        isPagingLoadingEvent.value = false
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
                        getSearchResults(searchText, filterType, pageNo)
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
}