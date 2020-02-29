package com.freelance.capsoula.ui.brands

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Brand
import com.freelance.capsoula.data.repository.BrandsRepository
import com.freelance.capsoula.data.responses.BrandsResponse
import com.freelance.capsoula.utils.SingleLiveEvent
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