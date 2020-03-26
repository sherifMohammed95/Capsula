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

class ProductsRepository : BaseRepository() {


    val productsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()

    suspend fun getBrandProducts(pageNo: Int, brandId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getBrandProducts(pageNo, Constants.PER_PAGE, brandId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    productsResponse.postValue(response.body()!!)
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
                        getBrandProducts(pageNo, brandId)
                    }
                })
            }
        }
    }

    suspend fun getSubCategoryProducts(pageNo: Int, subCatId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getSubCategoryProducts(pageNo, Constants.PER_PAGE, subCatId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    productsResponse.postValue(response.body()!!)
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
                        getSubCategoryProducts(pageNo, subCatId)
                    }
                })
            }
        }
    }

    suspend fun getStoreProducts(pageNo: Int, subCatId: Int, storeId: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response =
                webService.getStoreProducts(pageNo, Constants.PER_PAGE, subCatId, storeId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    productsResponse.postValue(response.body()!!)
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
                        getStoreProducts(pageNo, subCatId, storeId)
                    }
                })
            }
        }
    }

    suspend fun getTopRated(pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getTopRated(pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    productsResponse.postValue(response.body()!!)
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
                        getTopRated(pageNo)
                    }
                })
            }
        }
    }

    suspend fun getBestSellers(pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getBestSellers(pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    productsResponse.postValue(response.body()!!)
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
                        getBestSellers(pageNo)
                    }
                })
            }
        }
    }

    suspend fun getFreeDelivery(pageNo: Int) {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }

            val response = webService.getFreeDelivery(pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    productsResponse.postValue(response.body()!!)
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
                        getFreeDelivery(pageNo)
                    }
                })
            }
        }
    }
}