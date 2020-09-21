package com.blueMarketing.capsula.ui.stores

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.StoresRepository
import com.blueMarketing.capsula.data.responses.StoresResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
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