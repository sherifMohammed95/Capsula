package com.freelance.capsoula.ui.categories

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.CategoriesRepository
import com.freelance.capsoula.data.responses.CategoriesResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CategoriesViewModel(private val mRepository: CategoriesRepository) :
    BaseViewModel<CategoriesNavigator>() {

    var categoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    var storeCategoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    var storeId = -1
    var pageNo = 1

    init {
        initRepository(mRepository)
        this.categoriesResponse = mRepository.categoriesResponse
        this.storeCategoriesResponse = mRepository.storeCategoriesResponse
    }

    fun getCategories() {
        viewModelScope.launch(IO) {
            mRepository.getCategories(1)
        }
    }

    fun getStoreCategories() {
        viewModelScope.launch(IO) {
            mRepository.getStoreCategories(pageNo, storeId)
        }
    }

}