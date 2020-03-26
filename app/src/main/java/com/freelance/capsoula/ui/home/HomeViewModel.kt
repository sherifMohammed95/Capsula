package com.freelance.capsoula.ui.home

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.HomeResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: GeneralRepository) : BaseViewModel<HomeNavigator>() {
    var homeDataResponse = SingleLiveEvent<HomeResponse>()

    init {
        initRepository(repo)
        this.homeDataResponse = repo.homeDataResponse
        loadHomeData()
    }

    fun openSearch(){
        navigator?.openSearch()
    }


    fun openAllBrands(){
         navigator?.openAllBrands()
     }

    fun openAllCategories(){
        navigator?.openAllCategories()
    }

    fun openAllStores(){
        navigator?.openAllStores()
    }

    fun openTopRated(){
        navigator?.openTopRated()
    }
    fun openBestSeller(){
        navigator?.openBestSeller()
    }

    fun openFreeDelivery(){
        navigator?.openFreeDelivery()
    }

    private fun loadHomeData() {
        viewModelScope.launch(IO) {
            repo.getHomeData()
        }
    }
}