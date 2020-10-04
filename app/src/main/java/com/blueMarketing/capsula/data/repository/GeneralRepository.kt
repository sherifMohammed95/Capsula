package com.blueMarketing.capsula.data.repository

import com.blueMarketing.base.BaseResponse
import com.blueMarketing.capsula.data.FAQ
import com.blueMarketing.capsula.data.requests.RefreshDeviceRequest
import com.blueMarketing.capsula.data.responses.*
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeneralRepository : BaseRepository() {

    val homeDataResponse = SingleLiveEvent<HomeResponse>()
    val deliveryHomeDataResponse = SingleLiveEvent<DeliveryOrdersResponse>()
    val historyResponse = SingleLiveEvent<DeliveryOrdersResponse>()
    val nationalitiesResponse = SingleLiveEvent<NationalitiesResponse>()
    val carModelsResponse = SingleLiveEvent<NationalitiesResponse>()
    val deliveryRegisterBasicResponse = SingleLiveEvent<DeliveryRegisterBasicResponse>()
    val termsResponse = SingleLiveEvent<String>()
    val aboutResponse = SingleLiveEvent<String>()
    val faqsResponse = SingleLiveEvent<ArrayList<FAQ>>()
    val notificationResponse = SingleLiveEvent<NotificationsResponse>()

    val storesResponse = SingleLiveEvent<BaseResponse<StoresResponse>>()

    suspend fun getStores(showLoading: Boolean, pageNo: Int) {
        try {
            if (showLoading) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = true
                }
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
                        getStores(showLoading, pageNo)
                    }
                })
            }
        }
    }

    suspend fun getHomeData() {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }
            val response = webService.getHomeData()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    homeDataResponse.postValue(response.body()?.data)
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
                        getHomeData()
                    }
                })
            }
        }
    }

    suspend fun getDeliveryHomeData() {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.getDeliveryHomeData()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    deliveryHomeDataResponse.postValue(response.body()?.data)
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
                        getDeliveryHomeData()
                    }
                })
            }
        }
    }

    suspend fun getHistory(pageNo: Int, date: String) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.getHistory(pageNo, Constants.PER_PAGE, date)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    historyResponse.postValue(response.body()?.data)
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
                        getHistory(pageNo, date)
                    }
                })
            }
        }
    }


    suspend fun getNationalities() {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.getNationalities()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    nationalitiesResponse.postValue(response.body()?.data)
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
                        getNationalities()
                    }
                })
            }
        }
    }

    suspend fun getCarModels(carId: Int) {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.getCarModels(carId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    carModelsResponse.postValue(response.body()?.data)
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
                        getCarModels(carId)
                    }
                })
            }
        }
    }

    suspend fun getRegisterBasicData() {
        try {
            withContext(Dispatchers.Main) {
                progressLoading.value = true
            }
            val response = webService.getRegisterBasicData()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    progressLoading.value = false
                    deliveryRegisterBasicResponse.postValue(response.body()?.data)
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
                        getRegisterBasicData()
                    }
                })
            }
        }
    }

    suspend fun getUpdatedCart() {
        try {

            val response = webService.getUpdatedCart()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    if (!response.body()?.data?.list.isNullOrEmpty()) {
                        val user = UserDataSource.getUser()
                        user?.cartContent = response.body()?.data?.list
                        UserDataSource.saveUser(user)
                    } else {
                        val user = UserDataSource.getUser()
                        user?.cartContent = ArrayList()
                        UserDataSource.saveUser(user)
                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    suspend fun getUserData() {
        try {
            val response = webService.getUserData()
            if (response.isSuccessful) {
                withContext(Dispatchers.IO) {
                    if (response.body()?.data != null) {
                        UserDataSource.saveUser(response.body()?.data)
                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    suspend fun getTerms() {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }
            val response = webService.getTerms()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    termsResponse.postValue(response.body()?.data)
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
                        getTerms()
                    }
                })
            }
        }
    }

    suspend fun getAbout() {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }
            val response = webService.getAbout()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    aboutResponse.postValue(response.body()?.data)
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
                        getAbout()
                    }
                })
            }
        }
    }

    suspend fun getFAQs() {
        try {
            withContext(Dispatchers.Main) {
                showLoadingLayout.value = true
            }
            val response = webService.getFAQs()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    faqsResponse.postValue(response.body()?.data?.list)
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
                        getFAQs()
                    }
                })
            }
        }
    }

    suspend fun refreshDevice() {
        try {
            val request = RefreshDeviceRequest()
            val response = webService.refreshDevice(request)
            if (response.isSuccessful) {

            }
        } catch (e: Exception) {

        }
    }

    suspend fun getNotifications(showLoading: Boolean, pageNo: Int) {
        try {
            if (showLoading) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = true
                }
            }

            val response = webService
                .getNotifications(pageNo, Constants.PER_PAGE)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    showLoadingLayout.value = false
                    notificationResponse.postValue(response.body()?.data)
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
                        getNotifications(showLoading, pageNo)
                    }
                })
            }
        }
    }
}