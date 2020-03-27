package com.freelance.capsoula.data.repository

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRepository : BaseRepository() {

    val cartResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()

//    suspend fun addProductsToCart(pageNo: Int, brandId: Int) {
//        try {
//            withContext(Dispatchers.Main) {
//                showLoadingLayout.value = true
//            }
//
//            val response = webService.getBrandProducts(pageNo, Constants.PER_PAGE, brandId)
//            if (response.isSuccessful) {
//                withContext(Dispatchers.Main) {
//                    showLoadingLayout.value = false
//                    productsResponse.postValue(response.body()!!)
//                }
//            } else {
//                withContext(Dispatchers.Main) {
//                    handleApiError(response.errorBody()!!.string(), response.code())
//                }
//            }
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                handleNetworkError(Action {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        getBrandProducts(pageNo, brandId)
//                    }
//                })
//            }
//        }
//    }
}