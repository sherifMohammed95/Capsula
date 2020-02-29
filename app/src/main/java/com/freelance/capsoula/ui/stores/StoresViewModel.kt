package com.freelance.capsoula.ui.stores

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.StoresRepository
import com.freelance.capsoula.data.responses.StoresResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoresViewModel(private val mRepository: StoresRepository) :
    BaseViewModel<StoresNavigator>() {

    var storesResponse = SingleLiveEvent<BaseResponse<StoresResponse>>()

    var pageNo = 1

    init {
        initRepository(mRepository)
        this.storesResponse = mRepository.storesResponse
        getBrands()
    }

    private fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getStores(pageNo)
        }
    }
}