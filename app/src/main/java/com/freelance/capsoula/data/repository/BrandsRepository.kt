package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.Brand
import com.freelance.capsoula.data.responses.BrandsResponse
import com.freelance.capsoula.utils.Constants.PER_PAGE
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrandsRepository : BaseRepository() {

    val brandsResponse = SingleLiveEvent<BaseResponse<BrandsResponse>>()

    suspend fun getBrands(pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getBrands(pageNo,PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    brandsResponse.postValue(response.body()!!)
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
                        getBrands(pageNo)
                    }
                })
            }
        }
    }
}