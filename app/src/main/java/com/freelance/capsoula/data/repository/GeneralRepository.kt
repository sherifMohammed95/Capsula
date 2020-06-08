package com.freelance.capsoula.data.repository

import com.freelance.capsoula.data.requests.AddAddressRequest
import com.freelance.capsoula.data.responses.HomeResponse
import com.freelance.capsoula.data.responses.NationalitiesResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeneralRepository : BaseRepository() {

    val homeDataResponse = SingleLiveEvent<HomeResponse>()
    val nationalitiesResponse = SingleLiveEvent<NationalitiesResponse>()

    suspend fun getHomeData(){
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

    suspend fun getNationalities(){
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

    suspend fun getUpdatedCart(){
        try {

            val response = webService.getUpdatedCart()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    if(!response.body()?.data?.list.isNullOrEmpty()){
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
}