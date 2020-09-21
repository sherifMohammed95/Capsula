package com.blueMarketing.capsula.ui.brands

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.BrandsRepository
import com.blueMarketing.capsula.data.responses.BrandsResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class BrandsViewModel(private val mRepository: BrandsRepository) :
    BaseViewModel<BrandsNavigator>() {

    var brandsResponse = SingleLiveEvent<BaseResponse<BrandsResponse>>()
    var pageNo = 1

    init {
        initRepository(mRepository)
        this.brandsResponse = mRepository.brandsResponse
        getBrands()
    }

    private fun getBrands() {
        viewModelScope.launch(IO) {
            mRepository.getBrands(pageNo)
        }
    }
}