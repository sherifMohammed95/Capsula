package com.blueMarketing.capsula.data.repository

import com.blueMarketing.base.BaseResponse
import com.blueMarketing.capsula.data.responses.CategoriesResponse
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesRepository : BaseRepository() {

    val categoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    val storeCategoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    val subCategoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()

    suspend fun getCategories(pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                if (pageNo == 1)
                    showLoadingLayout.value = true
                else
                    isPagingLoadingEvent.value = true
            }

            val response = webService.getCategories(pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    if (pageNo == 1)
                        showLoadingLayout.value = false
                    else
                        isPagingLoadingEvent.value = false
                    categoriesResponse.postValue(response.body()!!)
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
                        getCategories(pageNo)
                    }
                })
            }
        }
    }

    suspend fun getStoreCategories(showLoading: Boolean, pageNo: Int, storeId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = showLoading
            }

            val response =
                webService.getStoreCategories(pageNo, Constants.PER_PAGE, storeId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    storeCategoriesResponse.postValue(response.body()!!)
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
                        getStoreCategories(showLoading, pageNo, storeId)
                    }
                })
            }
        }
    }

    suspend fun getSubCategories(pageNo: Int, catId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getSubCategories(pageNo, Constants.PER_PAGE, catId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    subCategoriesResponse.postValue(response.body()!!)
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
                        getSubCategories(pageNo, catId)
                    }
                })
            }
        }
    }


    suspend fun getStoreSubCategories(pageNo: Int, catId: Int, storeId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response =
                webService.getStoreSubCategories(pageNo, Constants.PER_PAGE, catId, storeId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    subCategoriesResponse.postValue(response.body()!!)
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
                        getStoreSubCategories(pageNo, catId, storeId)
                    }
                })
            }
        }
    }
}